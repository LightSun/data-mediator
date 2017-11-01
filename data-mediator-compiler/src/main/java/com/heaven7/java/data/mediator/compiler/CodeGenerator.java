package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.generator.HashEqualsGenerator;
import com.heaven7.java.data.mediator.compiler.generator.ProxyGenerator;
import com.heaven7.java.data.mediator.compiler.generator.TypeAdapterGenerator;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.OutInterfaceManager.getSuperInterfaceFlagForParent;
import static com.heaven7.java.data.mediator.compiler.insert.InsertManager.*;
import static com.heaven7.java.data.mediator.compiler.util.Util.createToStringBuilderForImpl;
import static com.heaven7.java.data.mediator.compiler.util.Util.hasFlag;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class CodeGenerator {

    private static final String TAG = CodeGenerator.class.getSimpleName();
   // replaced by idea-plugin
   // private static final BaseMemberBuilder sInterfaceBuilder = new BaseMemberBuilder();
    private static final BaseMemberBuilder sClassBuilder = new ClassMemberBuilder();

    private final TypeElement mElement;
    private final Elements mElements;
    private final Types mTypes;
    private final List<FieldData> mFields = new ArrayList<FieldData>();
    private boolean mEnableChain = true;

    private final TargetClassInfo mClassInfo = new TargetClassInfo();
    private int mMaxPoolCount; //max pool size
    /** current impl info
     * @since 1.3.0
     * */
    private ImplInfo mImplInfo; //@ImplClass .@ImplMethod
    /**
     * super impl infos
     * @since 1.3.0
     */
    private List<ImplInfo> mSuperImplInfos;
    private boolean mGenerateJsonAdapter = true;

    public CodeGenerator(Types mTypes, Elements mElementUtils, TypeElement classElement) {
        this.mTypes = mTypes;
        this.mElement = classElement;
        this.mElements = mElementUtils;
    }

    public void setGenerateJsonAdapter(boolean generate) {
          this.mGenerateJsonAdapter = generate;
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
    /** @since 1.3.0 */
    public void setCurrentImplInfo(ImplInfo cur_info) {
        this.mImplInfo = cur_info;
    }

    /** @since 1.3.0 */
    public void setSuperImplInfos(List<ImplInfo> superImplInfos) {
        this.mSuperImplInfos = superImplInfos;
    }
    /**
     * generate interface, impl and proxy .java files.
     * @param filer the filer,
     * @param mPrinter the log printer
     * @return true if generate success.
     */
    public boolean generateJavaFile(Filer filer, ProcessorPrinter mPrinter) {

        final boolean normalJavaBean = !mEnableChain;
        final String log_method = "generateJavaFile";
        //package name
        final String packageName = mElements.getPackageOf(mElement).getQualifiedName().toString();
        final  List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        mPrinter.note(TAG, log_method,  "super interfaces: " + interfaces);

        OutInterfaceManager.setLogPrinter(mPrinter);
        final Map<String, List<FieldData>> groupMap = OutInterfaceManager.groupFieldByInterface(mFields);

        //super fields
        final Set<FieldData> superFields = new HashSet<>();
        final String interfaceName = mElement.getSimpleName().toString();
        final TypeName selfParamType = ClassName.get(packageName, interfaceName);
        mPrinter.note(TAG, log_method,  "start element = " +
                packageName + "." + interfaceName, " , superFields = " + superFields);

        //set target class info
        mClassInfo.setPackageName(packageName);
        mClassInfo.setCurrentClassname(interfaceName);
        mClassInfo.setDirectParentInterfaceName(interfaceName);
        mClassInfo.setSuperClass(null);
        mClassInfo.setSuperInterfaces(interfaces);
        mClassInfo.setGenerateJsonAdapter(mGenerateJsonAdapter);

        //to generate impl class and proxy
        final String className = mElement.getSimpleName() + DataMediatorConstants.IMPL_SUFFIX;
        TypeSpec.Builder implBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
        //doc
        implBuilder.addJavadoc(CodeBlock.of(DataMediatorConstants.DOC));

        //set target class info
        mClassInfo.setCurrentClassname(className);

        final int superFlagsForParent = getSuperInterfaceFlagForParent(mElement, mElements, mTypes, mPrinter);
        // implBuilder.superclass()
        boolean usedSuperClass = false ;
        boolean hasSelectable = hasFlag(superFlagsForParent, FieldData.FLAG_SELECTABLE);
        implBuilder.addSuperinterface(selfParamType);

        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                //replace interface if need
                FieldData.TypeCompat tc = new FieldData.TypeCompat(mTypes, tm);
                implBuilder.addSuperinterface(tc.getTypeName());
                tc.replaceIfNeed(mElements, mPrinter);
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
            //handle super fields when in multi module.
            if(usedSuperClass && superFields.isEmpty()){
                MultiModuleSuperFieldDelegate multiDelegate = new
                        MultiModuleSuperFieldDelegate(mElements, mTypes, mPrinter);
                for(TypeMirror tm : interfaces){
                    superFields.addAll(multiDelegate.getDependFields(
                            (TypeElement) mTypes.asElement(tm)));
                }
            }
        }
        //=====================================================================
        //type adapter
        Set<FieldData> allFields = new HashSet<>(mFields);
        allFields.addAll(superFields);
        //type adapter.
        if(!TypeAdapterGenerator.generate(mClassInfo, allFields, filer)){
            return false;
        }
        //annotation and static code
        setClassInfo(mClassInfo);
        addClassAnnotation(implBuilder);
        CodeBlock.Builder staticCodeBuilder = CodeBlock.builder();
        if(addStaticCode(staticCodeBuilder,  mMaxPoolCount)){
            implBuilder.addStaticBlock(staticCodeBuilder.build());
        }
        //======================================================================

        //do something for super class/interface
        final List<? extends TypeMirror> mirrors = OutInterfaceManager.getAttentionInterfaces(
                mElement, mTypes, mPrinter);
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
        //add/override for insert interfaces's methods. (impl)
        insertOverrideMethods(implBuilder, usedSuperClass, hasSelectable);

        //add String toString().
        implBuilder.addMethod(createToStringBuilderForImpl(mFields, usedSuperClass)
                .build());

        //get/is/set methods
        sClassBuilder.build(implBuilder, mFields, superFields,
                normalJavaBean ? TypeName.VOID : selfParamType, selfParamType);

        //implements all methods of  @ImplMethods
        if(mImplInfo != null && mImplInfo.isValid()){
            mImplInfo.addImplMethods(implBuilder);
            mSuperImplInfos.add(mImplInfo);
        }
        // hashCode and equals.
        HashEqualsGenerator.generateForImpl(implBuilder, mFields, superFields, mClassInfo, usedSuperClass);

        //here classFile is a class .java file
        final JavaFile classFile = JavaFile.builder(packageName, implBuilder.build()).build();

        try {
            //impl
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
            if(!ProxyGenerator.generateProxy(mClassInfo, superFields, mSuperImplInfos,
                    normalJavaBean ,builders, filer, mPrinter)){
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
        Set<FieldData> list = new HashSet<>(mFields);
        //super will handle it. sub class should not handle it.
        if(!usedSuperClass && hasSelectable){
            list.add(FD_SELECTABLE);
        }
        overrideMethodsForImpl(implBuilder, list);
    }

}
