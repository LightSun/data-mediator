package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.generator.HashEqualsGenerator;
import com.heaven7.java.data.mediator.compiler.generator.ProxyGenerator;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.OutInterfaceManager.getSuperInterfaceFlagForParent;
import static com.heaven7.java.data.mediator.compiler.Util.createToStringBuilderForImpl;
import static com.heaven7.java.data.mediator.compiler.Util.hasFlag;
import static com.heaven7.java.data.mediator.compiler.insert.InsertManager.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class CodeGenerator {

    private static final String TAG = CodeGenerator.class.getSimpleName();
    private static final BaseMemberBuilder sInterfaceBuilder = new BaseMemberBuilder();
    private static final BaseMemberBuilder sClassBuilder = new ClassMemberBuilder();

    private final TypeElement mElement;
    private final Elements mElements;
    private final Types mTypes;
    private final List<FieldData> mFields = new ArrayList<FieldData>();
    private boolean mEnableChain = true;

    private final TargetClassInfo mClassInfo = new TargetClassInfo();
    private int mMaxPoolCount; //max pool size

    public CodeGenerator(Types mTypes, Elements mElementUtils, TypeElement classElement) {
        this.mTypes = mTypes;
        this.mElement = classElement;
        this.mElements = mElementUtils;
    }

    public void addFieldData(FieldData data) {
        mFields.add(data);
    }

    public void setEnableChain(boolean mEnableChain) {
        this.mEnableChain = mEnableChain;
    }
    public  List<FieldData> getFieldDatas(){
        return mFields;
    }

    public void setMaxPoolCount(int maxPoolCount) {
        this.mMaxPoolCount = maxPoolCount;
    }
    /**
     * generate interface, impl and proxy .java files.
     * @param delegate the super field delegate
     * @param filer the filer,
     * @param mPrinter the log printer
     * @return true if generate success.
     */
    public boolean generateJavaFile(ISuperFieldDelegate delegate, Filer filer, ProcessorPrinter mPrinter) {

        final boolean normalJavaBean = !mEnableChain;
        final String log_method = "generateJavaFile";
        //package name
        final String packageName = mElements.getPackageOf(mElement).getQualifiedName().toString();
        mPrinter.note(TAG, log_method,  "fields = "+mFields);
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        mPrinter.note(TAG, log_method,  "super interfaces: " + interfaces);

        OutInterfaceManager.setLogPrinter(mPrinter);
        final Map<String, List<FieldData>> groupMap = OutInterfaceManager.groupFieldByInterface(mFields);

        //super fields
        final Set<FieldData> superFields = new HashSet<>();
        //find super fields.
        if(interfaces != null) {
            for (TypeMirror mirror : interfaces) {
                final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
                Set<FieldData> dependFields = delegate.getDependFields(te);
                if (dependFields != null) {
                    superFields.addAll(dependFields);
                }
            }
        }
        /*
         * for interface.
         */
        //public interface xxxModule extends xx1,xx2{  }
        final String interfaceName = mElement.getSimpleName() + DataMediatorConstants.INTERFACE_SUFFIX;
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC);
        final TypeName selfParamType = TypeVariableName.get(interfaceName);

        //set target class info
        mClassInfo.setPackageName(packageName);
        mClassInfo.setCurrentClassname(interfaceName);
        mClassInfo.setDirectParentInterfaceName(interfaceName);
        mClassInfo.setSuperClass(null);
        mClassInfo.setSuperInterfaces(interfaces);

        //add all Constant RPOP_xxx field on Interface (from proxy moved here)
        ClassName cn_prop = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        ClassName cn_shared_properties = ClassName.get(PKG_SHARED_PROP, SIMPLE_NAME_SHARED_PROP);
        for(FieldData field : mFields){
            interfaceBuilder.addField(FieldSpec.builder(cn_prop,
                    field.getFieldConstantName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.get($S, $S, $L)",
                            cn_shared_properties, field.getTypeCompat().toString(),
                            field.getPropertyName(), field.getComplexType())
                    .build());
        }

        //extends DataPools.Poolable.
        addSuperInterface(interfaceBuilder);
        //handle super interface with method.
        interfaceBuilder.addSuperinterface(TypeName.get(mElement.asType()));
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = OutInterfaceManager.getInterfaceMethodBuilders(mClassInfo,
                        selfParamType, tm, mPrinter);
                if(builders != null){
                    for (MethodSpec.Builder builder : builders){
                        if(builder != null) {
                            interfaceBuilder.addMethod(builder.build());
                        }
                    }
                }
                //replace interface if need
                FieldData.TypeCompat tc = new FieldData.TypeCompat(mTypes, tm);
                tc.replaceIfNeed(mPrinter);
                interfaceBuilder.addSuperinterface(tc.getInterfaceTypeName());
            }
        }
        //add String toString.
        interfaceBuilder.addMethod(MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(String.class)
                .build());

        sInterfaceBuilder.build(interfaceBuilder, mFields, superFields,
                normalJavaBean ? TypeName.VOID : selfParamType, selfParamType);

        /**
         * for impl class:
         step1: generate all field and method(need body).
         step2: class/interface
         step3: package name
         */
        final String className = mElement.getSimpleName() + DataMediatorConstants.IMPL_SUFFIX;
        TypeSpec.Builder implBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

        //set target class info
        mClassInfo.setCurrentClassname(className);
        mClassInfo.setDirectParentInterfaceName(interfaceName);
        mClassInfo.setSuperClass(null);
        mClassInfo.setSuperInterfaces(interfaces);

        // DataPools.preparePool("", 5);
        setClassInfo(mClassInfo);
        addStaticCode(implBuilder,  mMaxPoolCount);

        final int superFlagsForParent = getSuperInterfaceFlagForParent(mElement, mTypes, mPrinter);
        // implBuilder.superclass()
        boolean usedSuperClass = false ;
        boolean hasSelectable = hasFlag(superFlagsForParent, FieldData.FLAG_SELECTABLE);
        implBuilder.addSuperinterface(TypeVariableName.get(interfaceName));
        if(interfaces != null){
            //mPrinter.note("implBuilder >>> start interfaces ");
            for(TypeMirror tm : interfaces){
                //replace interface if need
                FieldData.TypeCompat tc = new FieldData.TypeCompat(mTypes, tm);
                tc.replaceIfNeed(mPrinter);
                implBuilder.addSuperinterface(tc.getInterfaceTypeName());
                //handle super class.
                TypeName superclassType = tc.getSuperClassTypeName();
                if(superclassType != null){
                    if(usedSuperClass){
                        mPrinter.error(TAG, log_method, "implBuilder >> can only have one super class.");
                        return false;
                    }else{
                        implBuilder.superclass(superclassType);
                        mClassInfo.setSuperClass(superclassType);
                        usedSuperClass = true;
                    }
                }
                //handle selectable
                if(!hasSelectable){
                   if(tc.toString().equals(NAME_SELECTABLE)){
                       hasSelectable = true;
                   }
                }
            }
        }
        //====================== interface static filed for some other interface ==================
        if(hasSelectable){
            interfaceBuilder.addField( FieldSpec.builder(cn_prop,
                    FD_SELECTABLE.getFieldConstantName(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.get($S, $S, $L)",
                            cn_shared_properties, FD_SELECTABLE.getTypeCompat().toString(),
                            FD_SELECTABLE.getPropertyName(), FD_SELECTABLE.getComplexType())
                    .build());
        }
        //build interface
        final JavaFile interfaceFile = JavaFile.builder(packageName, interfaceBuilder.build()).build();
        //======================================================================


        //do something for super class/interface
        final List<? extends TypeMirror> mirrors = OutInterfaceManager.getAttentionInterfaces(mElement, mTypes, mPrinter);
        for(TypeMirror temp_tm : mirrors){
            FieldData.TypeCompat temp_tc = new FieldData.TypeCompat(mTypes, temp_tm);
            //normal methods
           MethodSpec.Builder[] builders =  OutInterfaceManager.getImplClassMethodBuilders(mClassInfo,
                    selfParamType, temp_tc, mPrinter,
                   groupMap, usedSuperClass, superFlagsForParent);
            if(builders != null){
                for (MethodSpec.Builder builder : builders){
                    if(builder != null) {
                        implBuilder.addMethod(builder.build());
                    }
                }
            }
            //override super [constructor] for parcelable. and etc.
            // note : super class may not impl Parcelable.
            MethodSpec.Builder[] constructors = OutInterfaceManager.getImplClassConstructBuilders(packageName,
                    className, temp_tc, groupMap, usedSuperClass, superFlagsForParent);
            if(constructors != null ){
                for (MethodSpec.Builder builder : constructors){
                    if(builder != null){
                        implBuilder.addMethod(builder.build());
                    }
                }
            }

            //[fields]
            final FieldSpec.Builder[] fieldBuilders = OutInterfaceManager.getImplClassFieldBuilders(
                    packageName, className, temp_tc, groupMap, superFlagsForParent);
            if(fieldBuilders != null) {
                for (FieldSpec.Builder builder : fieldBuilders) {
                    if(builder != null){
                        implBuilder.addField(builder.build());
                    }
                }
            }
        }
        //add/override for insert interfaces.
        insertOverrideMethods(implBuilder, usedSuperClass, hasSelectable);

        //add String toString().
        implBuilder.addMethod(createToStringBuilderForImpl(mFields, usedSuperClass)
                .build());

        //get/is/set methods
        sClassBuilder.build(implBuilder, mFields, superFields,
                normalJavaBean ? TypeName.VOID : selfParamType, selfParamType);

        // hashCode and equals.
        HashEqualsGenerator.generateForImpl(implBuilder, mFields, superFields, mClassInfo, usedSuperClass);

        //here classFile is a class .java file
        final JavaFile classFile = JavaFile.builder(packageName, implBuilder.build()).build();

        try {
            //interface and impl
            interfaceFile.writeTo(filer);
            classFile.writeTo(filer);

            //handle proxy class.
            mClassInfo.setCurrentClassname(interfaceName + PROXY_SUFFIX);
            mClassInfo.setSuperClass(null);

            //generate some method from super class.
            List<MethodSpec.Builder> builders = OutInterfaceManager.getProxyClassMethodBuilders(
                    mClassInfo, mElement, mTypes, mPrinter);
            //to generate proxy class. with base method for fields.
            superFields.addAll(mFields);
            //add selectable field if need
            if(hasSelectable){
                superFields.add(FD_SELECTABLE);
            }
            //do generate proxy
            if(!ProxyGenerator.generateProxy(mClassInfo, superFields, normalJavaBean ,builders, filer, mPrinter)){
                return false;
            }
        } catch (IOException e) {
            mPrinter.error(TAG, log_method, Util.toString(e));
            return false;
        }finally {
            OutInterfaceManager.reset();
        }
        return true;
    }

    private void insertOverrideMethods(TypeSpec.Builder implBuilder, boolean usedSuperClass, boolean hasSelectable) {
        List<FieldData> list = new ArrayList<>(mFields);
        //super will handle it. sub class should not handle it.
        if(!usedSuperClass && hasSelectable){
            list.add(FD_SELECTABLE);
        }
        overrideMethodsForImpl(implBuilder, list);
    }


}
