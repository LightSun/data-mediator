package com.heaven7.java.data.mediator.processor;

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

import static com.heaven7.java.data.mediator.processor.Util.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public class ProxyClass {

    private static final BaseMemberBuilder sInterfaceBuilder = new BaseMemberBuilder();
    private static final BaseMemberBuilder sClassBuilder = new ClassMemberBuilder();

    private final TypeElement mElement;
    private final Elements mElements;
    private final Types mTypes;
    private final List<FieldData> mFields = new ArrayList<>();

    public ProxyClass(Types mTypes, Elements mElementUtils, TypeElement classElement) {
        this.mTypes = mTypes;
        this.mElement = classElement;
        this.mElements = mElementUtils;
    }

    public void addFieldData(FieldData data) {
        mFields.add(data);
    }

    /**
     *  生成所需要的接口 ， 类
     *  1: 生成正确。
     *  2：重新生成问题
     *  3：parceable. 等处理
     * @param mPrinter
     */
    public boolean generateProxy(ProcessorPrinter mPrinter, Filer filer) {
        mPrinter.note(" <<< generateProxy >>> field datas = " + mFields);
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        mPrinter.note("super interfaces: " + interfaces);

        setLogPrinter(mPrinter);
        final Map<String, List<FieldData>> groupMap = groupFieldByInterface(mFields);
        mPrinter.note("generateProxy >> groupMap = " + groupMap);
        /**
         * for interface.
         */
        //public interface xxxModule extends xx1,xx2{  }
        final String interfaceName = mElement.getSimpleName() + Util.INTERFACE_SUFFIX;
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC);
        final TypeName selfParamType = TypeVariableName.get(interfaceName);

        interfaceBuilder.addSuperinterface(TypeName.get(mElement.asType()));
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = getInterfaceMethodBuilders(
                        selfParamType, tm, mPrinter);
                if(builders != null){
                    for (MethodSpec.Builder builder : builders){
                        interfaceBuilder.addMethod(builder.build());
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
        //package name
        String packageName = mElements.getPackageOf(mElement).getQualifiedName().toString();
        final JavaFile interfaceFile = JavaFile.builder(packageName, interfaceModule).build();
        /**
         * for impl class:
         step1: generate all field and method(need body).
         step2: class/interface
         step3: package name
         */
        final String className = mElement.getSimpleName() + Util.IMPL_SUFFIX;
        TypeSpec.Builder implBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC);
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
                        usedSuperClass = true;
                    }
                }

                MethodSpec.Builder[] builders =  getImplClassMethodBuilders(packageName,
                        className, selfParamType, tc, mPrinter, groupMap);
                mPrinter.note("implBuilder >>> start  MethodSpec.Builder[] s: " + tm);
                if(builders != null){
                    mPrinter.note("implBuilder >>> start builders");
                    for (MethodSpec.Builder builder : builders){
                        implBuilder.addMethod(builder.build());
                    }
                    mPrinter.note("implBuilder >>> start  end ...builderss");
                }
            }
        }
        sClassBuilder.build(implBuilder, mFields);
        //here classFile is a class .java file
        final JavaFile classFile = JavaFile.builder(packageName, implBuilder.build()).build();
        try {
            interfaceFile.writeTo(filer);
            classFile.writeTo(filer);
        } catch (IOException e) {
            mPrinter.error(Util.toString(e));
            return false;
        }
        return true;
    }
}
