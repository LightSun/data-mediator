package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.Field;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public class ProxyClass {

    private static final String SET_PREFIX = "set";
    private static final String GET_PREFIX = "get";

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
    public JavaFile generateProxy(ProcessorPrinter mPrinter) {
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        mPrinter.note("super interfaces: " + interfaces);
        /**
         * for interface.
         */
        //public interface xxxModule extends xx1,xx2{  }
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(mElement.getSimpleName() + "Module")
                .addModifiers(Modifier.PUBLIC);
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                interfaceBuilder.addSuperinterface(TypeName.get(tm));
            }
        }
        mPrinter.note("mFields.size() = " + mFields.size());
        for(FieldData field : mFields) {
            Class<?> type = field.getType();
            mPrinter.note("for >>> type = " + type.getName());
            String nameForMethod = getPropNameForMethod(field.getPropertyName());
            //Target get();
            MethodSpec.Builder get = MethodSpec.methodBuilder(GET_PREFIX + nameForMethod)
                    .returns(TypeName.get(type))
                    .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
            MethodSpec.Builder set = MethodSpec.methodBuilder(SET_PREFIX + nameForMethod)
                    .addParameter(type, getParamName(type.getSimpleName()))
                    .returns(TypeName.VOID)
                    .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
            interfaceBuilder.addMethod(get.build())
                    .addMethod(set.build());
        }
        TypeSpec interfaceModule = interfaceBuilder.build();
        //package name
        String packageName = mElements.getPackageOf(mElement).getQualifiedName().toString();

        /**
         * for class:
         */
        //step1: generate all field and method(need body).
        //step2: class/interface
        //step3: package name
        return JavaFile.builder(packageName, interfaceModule).build();
    }

    private static String getPropNameForMethod(String prop) {
        if(prop == null || "".equals(prop.trim())){
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0,1).toUpperCase().concat(prop.substring(1));
    }
    private static String getParamName(String name) {
        return name.substring(0,1).toLowerCase().concat(name.substring(1));
    }

}
