package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.Fields;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@SupportedAnnotationTypes({
        "com.heaven7.java.data.mediator.Fields"
})//可以用"*"表示支持所有Annotations
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MediatorAnnotationProcessor extends AbstractProcessor {

    private static final String TAG = "MediatorAnnotationProcessor";
    private Filer mFiler;           //文件相关工具类

    private Elements mElementUtils; //元素相关的工具类
    //private Messager mMessager;     //日志相关的工具类
    private ProcessorPrinter mPrinter; ////日志相关的处理
    private Types mTypeUtils;

    private Map<String, CodeGenerator> mProxyClassMap = new HashMap<>();

    private void note(Object msg, Object... objs) {
        mPrinter.note(msg, objs);
    }

    private void error(Object obj1, Object... objs) {
        mPrinter.error(obj1, objs);
    }
    /**
     * 处理器的初始化方法，可以获取相关的工具类
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mPrinter = new ProcessorPrinter(processingEnv.getMessager());
        mTypeUtils = processingEnv.getTypeUtils();
        mPrinter.note(TAG, "init", processingEnv.getOptions());
    }

    /**
     * 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedOptions() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Fields.class.getName());
        // types.add(OnClick.class.getName());
        return types;
    }

    /**
     * 用来指定你使用的 java 版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 处理器的主方法，用于扫描处理注解，生成java文件
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        note("annotations: " + annotations);

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Fields.class);
        for (Element element : elements) {
            note("@Fields >>> element = " + element);
            if (!isValid(Fields.class, "interface", element)) {
                return true;
            }
            CodeGenerator generator = getProxyClass(element);
            if(!ElementHelper.processAnnotation(mTypeUtils, mPrinter, element.getAnnotationMirrors(), generator.getFieldDatas())){
                return true;
            }
        }
       //generate code
       for (CodeGenerator generator : mProxyClassMap.values()) {
           if(!generator.generateProxy(mPrinter, mFiler)){
               return true;
           }
        }
        mProxyClassMap.clear();
        return false;
    }

    /**
     * 生成或获取注解元素所对应的ProxyClass类
     */
    private CodeGenerator getProxyClass(Element element) {
        //被注解的变量所在的类
        TypeElement classElement = (TypeElement) element;
        String qualifiedName = classElement.getQualifiedName().toString();
        CodeGenerator proxyClass = mProxyClassMap.get(qualifiedName);
        if (proxyClass == null) {
            proxyClass = new CodeGenerator(mTypeUtils, mElementUtils, classElement);
            mProxyClassMap.put(qualifiedName, proxyClass);
        }
        return proxyClass;
    }

    private boolean isValid(Class<? extends Annotation> annotationClass, String targetThing, Element element) {

        TypeElement enclosingElement = (TypeElement) element;
        //被注解的类的全名
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        // anno = com.heaven7.java.data.mediator.Fields ,parent element is: com.heaven7.data.mediator.demo.Student
        note("anno = " + annotationClass.getName() + " ,full name is: " + qualifiedName);

        boolean isValid = true;
        // 所在的类不能是private或static修饰
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            String msg = String.format("@%s %s must not be private or static. (%s.%s)", annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error(enclosingElement, msg);
            isValid = false;
        }

        // 父元素必须接口
        if (enclosingElement.getKind() != ElementKind.INTERFACE) {
            String msg = String.format("@%s %s may only be contained in interfaces. (%s.%s)", annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error(enclosingElement, msg);
            isValid = false;
        }

        //不能在Android框架层注解
        if (qualifiedName.startsWith("android.")) {
            String msg = String.format("@%s-annotated class incorrectly in Android framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error(enclosingElement, msg);
            isValid = false;
        }
        //不能在java框架层注解
        if (qualifiedName.startsWith("java.")) {
            String msg = String.format("@%s-annotated class incorrectly in Java framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error(enclosingElement, msg);
            isValid = false;
        }

        return isValid;
    }
}
