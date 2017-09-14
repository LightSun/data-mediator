package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.PROXY_SUFFIX;
import static com.heaven7.java.data.mediator.compiler.Util.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class CodeGenerator {

    private static final BaseMemberBuilder sInterfaceBuilder = new BaseMemberBuilder();
    private static final BaseMemberBuilder sClassBuilder = new ClassMemberBuilder();

    private final TypeElement mElement;
    private final Elements mElements;
    private final Types mTypes;
    private final List<FieldData> mFields = new ArrayList<FieldData>();
    private final TargetClassInfo mClassInfo = new TargetClassInfo();

    public CodeGenerator(Types mTypes, Elements mElementUtils, TypeElement classElement) {
        this.mTypes = mTypes;
        this.mElement = classElement;
        this.mElements = mElementUtils;
    }

    public void addFieldData(FieldData data) {
        mFields.add(data);
    }

    public  List<FieldData> getFieldDatas(){
        return mFields;
    }

    /**
     *  生成所需要的接口 ， 类
     *  1: 生成正确。
     *  2：重新生成问题
     *  3：parceable. 等处理
     * @param mPrinter
     */
    public boolean generateJavaFile(ISuperFieldDelegate delegate, Filer filer, ProcessorPrinter mPrinter) {
        //package name
        final String packageName = mElements.getPackageOf(mElement).getQualifiedName().toString();
        mPrinter.note(" <<< generateProxy >>> field datas = " + mFields);
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        //final TypeMirror superclass = mElement.getSuperclass();
        mPrinter.note("super interfaces: " + interfaces);

        setLogPrinter(mPrinter);
        final Map<String, List<FieldData>> groupMap = groupFieldByInterface(mFields);
        mPrinter.note("generateProxy >> groupMap = " + groupMap);
        /**
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

        //handle super interface with method.
        interfaceBuilder.addSuperinterface(TypeName.get(mElement.asType()));
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = getInterfaceMethodBuilders(mClassInfo,
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
        sInterfaceBuilder.build(interfaceBuilder, mFields);

        TypeSpec interfaceModule = interfaceBuilder.build();

        final JavaFile interfaceFile = JavaFile.builder(packageName, interfaceModule).build();
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
        mClassInfo.setPackageName(packageName);
        mClassInfo.setCurrentClassname(className);
        mClassInfo.setDirectParentInterfaceName(interfaceName);
        mClassInfo.setSuperClass(null);
        mClassInfo.setSuperInterfaces(interfaces);

       // implBuilder.superclass()
        boolean usedSuperClass = false ;
        implBuilder.addSuperinterface(TypeVariableName.get(interfaceName));
        if(interfaces != null){
            mPrinter.note("implBuilder >>> start interfaces ");
            for(TypeMirror tm : interfaces){
                //replace interface if need
                FieldData.TypeCompat tc = new FieldData.TypeCompat(mTypes, tm);
                tc.replaceIfNeed(mPrinter);
                implBuilder.addSuperinterface(tc.getInterfaceTypeName());
                //handle super class.
                TypeName superclassType = tc.getSuperClassTypeName();
                if(superclassType != null){
                    if(usedSuperClass){
                        mPrinter.error("implBuilder >> can only have one super class.");
                        return false;
                    }else{
                        implBuilder.superclass(superclassType);
                        mClassInfo.setSuperClass(superclassType);
                        usedSuperClass = true;
                    }
                }
                //fields
                FieldSpec.Builder[] fieldBuilders = getImplClassFieldBuilders(packageName,
                        className, tc, groupMap);
                if(fieldBuilders != null){
                    for( FieldSpec.Builder fieldBuilder : fieldBuilders){
                        implBuilder.addField(fieldBuilder.build());
                    }
                }

                //constructor
                MethodSpec.Builder[] constructors = getImplClassConstructBuilders(packageName,
                        className, tc, groupMap, usedSuperClass);
                if(constructors != null ){
                    for (MethodSpec.Builder builder : constructors){
                        if(builder != null){
                            implBuilder.addMethod(builder.build());
                        }
                    }
                }

                //methods
                MethodSpec.Builder[] builders =  getImplClassMethodBuilders(mClassInfo,
                        selfParamType, tc, mPrinter, groupMap, usedSuperClass);
                mPrinter.note("implBuilder >>> start  MethodSpec.Builder[] s: " + tm);
                if(builders != null){
                    mPrinter.note("implBuilder >>> start builders");
                    for (MethodSpec.Builder builder : builders){
                        if(builder != null) {
                            implBuilder.addMethod(builder.build());
                        }
                    }
                    mPrinter.note("implBuilder >>> start  end ...builderss");
                }

                //override the super method of the super interfaces' superinterface(like ICopyable and etc.)
                for(TypeMirror temp_tm : tc.getElementAsType().getInterfaces()){
                    FieldData.TypeCompat temp_tc = new FieldData.TypeCompat(mTypes, temp_tm);
                    builders =  getImplClassMethodBuilders(mClassInfo,
                            selfParamType, temp_tc, mPrinter, groupMap, usedSuperClass);
                    if(builders != null){
                        for (MethodSpec.Builder builder : builders){
                            if(builder != null) {
                                implBuilder.addMethod(builder.build());
                            }
                        }
                    }
                }
            }
        }
        sClassBuilder.build(implBuilder, mFields);
        //here classFile is a class .java file
        final JavaFile classFile = JavaFile.builder(packageName, implBuilder.build()).build();

        try {
            interfaceFile.writeTo(filer);
            classFile.writeTo(filer);

            //handle proxy class.
            mClassInfo.setCurrentClassname(interfaceName + PROXY_SUFFIX);
            mClassInfo.setSuperClass(null);

            //generate some method from super class.
            List<MethodSpec.Builder> builders = Util.getProxyClassMethodBuilders(mClassInfo, mElement, mTypes, mPrinter);
            //generate proxy class. with bese method for fields.
            if(!ProxyGenerator.generateProxy(mClassInfo, mFields, builders, delegate, filer, mPrinter)){
                return false;
            }
        } catch (IOException e) {
            mPrinter.error(Util.toString(e));
            return false;
        }finally {
            Util.reset();
        }
        return true;
    }
}
