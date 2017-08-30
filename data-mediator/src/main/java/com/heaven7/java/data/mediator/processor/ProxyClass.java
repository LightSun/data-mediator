package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.heaven7.java.data.mediator.processor.Util.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public class ProxyClass {

    private static final String SET_PREFIX = "set";
    private static final String GET_PREFIX = "get";
    private static final BaseMemberBuilder sInterfaceBuilder = new BaseMemberBuilder();
    private static final BaseMemberBuilder sClassBuilder = new ClassMemberBuilder();

    public final TypeElement mElement;
    public final Elements mElements;
    public final List<FieldData> mFields = new ArrayList<>();

    public ProxyClass(TypeElement classElement, Elements mElementUtils) {
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
    public void generateProxy(ProcessorPrinter mPrinter, Filer filer) {
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        mPrinter.note("super interfaces: " + interfaces);

        /**
         * for interface.
         */
        //public interface xxxModule extends xx1,xx2{  }
        final String interfaceName = mElement.getSimpleName() + "Module";
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC);
        final TypeName selfParamType = TypeVariableName.get(interfaceName);

        interfaceBuilder.addSuperinterface(TypeName.get(mElement.asType()));
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = getInterfaceMethodBuilders(
                        selfParamType, tm, mPrinter, true);
                if(builders != null){
                    for (MethodSpec.Builder builder : builders){
                        interfaceBuilder.addMethod(builder.build());
                    }
                }
                interfaceBuilder.addSuperinterface(TypeName.get(tm));
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
        TypeSpec.Builder implBuilder = TypeSpec.classBuilder(mElement.getSimpleName() + "Module__Impl")
                .addModifiers(Modifier.PUBLIC);
        implBuilder.addSuperinterface(TypeVariableName.get(interfaceName));
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = getInterfaceMethodBuilders(
                        selfParamType, tm, mPrinter, false);
                if(builders != null){
                    //TODO impl of super method
                    for (MethodSpec.Builder builder : builders){
                        implBuilder.addMethod(builder.build());
                    }
                }
                implBuilder.addSuperinterface(TypeName.get(tm));
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
        }
    }
}
