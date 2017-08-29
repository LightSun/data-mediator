package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
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
    private static final String CACHE_ABLE = "get";

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

        interfaceBuilder.addSuperinterface(TypeName.get(mElement.asType()));

        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                MethodSpec.Builder[] builders = getInterfaceMethodBuilders(mElement.getQualifiedName().toString(),
                        tm, mPrinter, true);
                if(builders != null){
                    for (MethodSpec.Builder builder : builders){
                        interfaceBuilder.addMethod(builder.build());
                    }
                }
                interfaceBuilder.addSuperinterface(TypeName.get(tm));
            }
        }
       // mPrinter.note("mFields.size() = " + mFields.size());
        for(FieldData field : mFields) {
            Class<?> type = field.getType();
           // mPrinter.note("for >>> type = " + type.getName());
            String nameForMethod = getPropNameForMethod(field.getPropertyName());
            //Target get();

            final TypeName typeName;
            final String paramName;
            switch (field.getComplexType()){
                case FieldData.COMPLEXT_ARRAY:
                    typeName = ArrayTypeName.of(type);
                    paramName = "array1";
                    break;

                case FieldData.COMPLEXT_LIST:
                    typeName = ParameterizedTypeName.get(ClassName.get(List.class),
                            WildcardTypeName.get(type).box());
                    paramName = "list1";
                    break;

                default:
                    typeName = TypeName.get(type);
                    paramName = getParamName(type.getSimpleName());
                    break;
            }

            MethodSpec.Builder get = MethodSpec.methodBuilder(GET_PREFIX + nameForMethod)
                    .returns(typeName)
                    .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
            MethodSpec.Builder set = MethodSpec.methodBuilder(SET_PREFIX + nameForMethod)
                    .addParameter(typeName, paramName)
                    .returns(TypeName.VOID)
                    .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
            interfaceBuilder.addMethod(get.build())
                    .addMethod(set.build());
        }
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
        TypeSpec.Builder implBuilder = TypeSpec.interfaceBuilder(mElement.getSimpleName() + "Module__Impl")
                .addModifiers(Modifier.PUBLIC);
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                test(tm, mPrinter);
                implBuilder.addSuperinterface(TypeName.get(tm));
            }
        }
        for(FieldData field : mFields){
            //TODO
            FieldSpec.builder(field.getType(), field.getPropertyName(),
                    Util.getFieldModifier(field ));
        }
        try {
            interfaceFile.writeTo(filer);
        } catch (IOException e) {
            mPrinter.error(Util.toString(e));
        }
    }

    private static class WriterImpl implements IJavaWriter{

        final JavaFile mInterface;
        final JavaFile mImpl;
        final ProcessorPrinter mPrinter;

        public WriterImpl(JavaFile mInterface, JavaFile mImpl, ProcessorPrinter mPrinter) {
            this.mInterface = mInterface;
            this.mImpl = mImpl;
            this.mPrinter = mPrinter;
        }

        @Override
        public void writeTo(Filer filer) {
            try {
                mInterface.writeTo(filer);
                mImpl.writeTo(filer);
            }catch (Exception e){
                mPrinter.error(Util.toString(e));
            }
        }
    }

}
