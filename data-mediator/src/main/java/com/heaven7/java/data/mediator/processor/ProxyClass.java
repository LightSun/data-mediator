package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(mElement.getSimpleName() + "Module")
                .addModifiers(Modifier.PUBLIC);
        if(interfaces != null){
            for(TypeMirror tm : interfaces){
                interfaceBuilder.addSuperinterface(TypeName.get(tm));
            }
        }
       // mPrinter.note("mFields.size() = " + mFields.size());
        for(FieldData field : mFields) {
            Class<?> type = field.getType();
           // mPrinter.note("for >>> type = " + type.getName());
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

    //here: mirror - interface
    private static void applyInterface(TypeSpec.Builder builder, TypeMirror mirror, ProcessorPrinter pp){
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        Name paramType = te.getQualifiedName();
        pp.note("test() >>> paramType = " + paramType.toString());
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        for(Element e: list){
            ExecutableElement ee = (ExecutableElement) e;
            MethodSpec.overriding(ee);
              //TODO  .addStatement();
            pp.note("ee_getSimpleName: " + ee.getSimpleName());
            pp.note("ee_return: " + ee.getReturnType());
            pp.note("ee_getTypeParameters: " + ee.getTypeParameters());
            pp.note("ee_getThrownTypes: " + ee.getThrownTypes());
            pp.note("ee_getReceiverType: " + ee.getReceiverType());
        }
        pp.note("test() >>> getTypeParameters = " + te.getTypeParameters());
        pp.note("test() >>> getEnclosedElements = " + list);
        pp.note("test() >>> getEnclosedElements__2 = " + Arrays.toString(
                list.get(0).getClass().getInterfaces()));
        pp.note("test() >>> getEnclosingElement = " + te.getEnclosingElement());
        // pp.note("test() >>> getInterfaces = " + te.getInterfaces());
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
