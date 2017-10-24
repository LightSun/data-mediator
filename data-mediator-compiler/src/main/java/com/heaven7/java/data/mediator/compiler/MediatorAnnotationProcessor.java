package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.GlobalConfig;
import com.heaven7.java.data.mediator.ImplClass;
import com.heaven7.java.data.mediator.ImplMethod;
import com.heaven7.java.data.mediator.compiler.generator.SharedPropertiesNGenerator;
import com.heaven7.java.data.mediator.compiler.generator.StaticLoaderGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@SupportedAnnotationTypes({
        "com.heaven7.java.data.mediator.Fields",
        "com.heaven7.java.data.mediator.GlobalConfig"
})                       //可以用"*"表示支持所有Annotations
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MediatorAnnotationProcessor extends AbstractProcessor {

    private static final String TAG = "MediatorAnnotationProcessor";
    private Filer mFiler;           //文件相关工具类

    private Elements mElementUtils; //元素相关的工具类
    //private Messager mMessager;     //日志相关的工具类
    private ProcessorPrinter mPrinter; ////日志相关的处理
    private Types mTypeUtils;

    private final Map<String, CodeGenerator> mProxyClassMap = new HashMap<>();

    private void note(String method, Object... objs) {
        mPrinter.note(TAG, method, objs);
    }

    private void error(String method, Object... objs) {
        mPrinter.error(TAG ,method, objs);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mPrinter = new ProcessorPrinter(processingEnv.getMessager());
        mTypeUtils = processingEnv.getTypeUtils();

        mPrinter.note(TAG, "init", processingEnv.getOptions());
       /* try {
            final FileObject fo = mFiler.createResource(StandardLocation.CLASS_OUTPUT,
                    "com.heaven7.xxx", "store.txt");
            final Writer writer = fo.openWriter();
            writer.write("key=heaven7");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> types = new HashSet<>();
        types.add(Fields.class.getName());
        types.add(GlobalConfig.class.getName());
        types.add(ImplClass.class.getName());
        types.add(ImplMethod.class.getName());
        return Collections.unmodifiableSet(types);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        note("process","annotations: " + annotations);
        if(annotations.isEmpty()){
            return false;
        }
        note("process","root eles: " + roundEnv.getRootElements());
        //========== start @GlobalConfig ==============
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(GlobalConfig.class);
        if(set.size() >= 2){
            mPrinter.error(TAG, "process", "@GlobalConfig can only have one.");
            return true;
        }
        if(set.size() == 1){
            if(!ElementHelper.processGlobalSetting(mTypeUtils,
                    set.iterator().next().getAnnotationMirrors(), mPrinter)){
                return true;
            }
            final boolean exists = mElementUtils.getTypeElement(StaticLoaderGenerator.CNAME) != null;
            if( com.heaven7.java.data.mediator.compiler.GlobalConfig
                    .getInstance().getVersion() > 1.0){
                if(exists){
                    mPrinter.error(TAG, "process", "gson version from @GlobalConfig can only config once.");
                    return true;
                }else{
                    //generate StaticCodeLoader
                    if(!StaticLoaderGenerator.generateStaticCodeLoader(mFiler, mPrinter)){
                        return true;
                    }
                }
            }
        }
        //========== end GlobalConfig ===================
        //Set<? extends Element> implClasses = roundEnv.getElementsAnnotatedWith(ImplClass.class);
        //Set<? extends Element> implMethods = roundEnv.getElementsAnnotatedWith(ImplMethod.class);

        //fields
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Fields.class);
        for (Element element : elements) {
            note(":process" ,"@Fields >>> element = " + element);
            if (!isValid(Fields.class, "interface", element)) {
                return true;
            }
            CodeGenerator generator = getProxyClass(element);
            if(!ElementHelper.processAnnotation(mTypeUtils, mPrinter,
                    element.getAnnotationMirrors(), generator)){
                return true;
            }
        }
        //generate SharedProperties_N
        final Set<FieldData> fields = new HashSet<>();
        for (CodeGenerator generator : mProxyClassMap.values()) {
            fields.addAll(generator.getFieldDatas());
        }
        if(!SharedPropertiesNGenerator.generateSharedProperties(
                fields, mElementUtils, mFiler, mPrinter)){
            return true;
        }

       //generate module interface / impl /proxy
       for (CodeGenerator generator : mProxyClassMap.values()) {
           if(!generator.generateJavaFile(mSuperDelegate ,mFiler, mPrinter)){
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
        note("isValid","anno = " + annotationClass.getName()
                + " ,full name is: " + qualifiedName);

        boolean isValid = true;
        // 所在的类不能是private或static修饰
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            String msg = String.format("@%s %s must not be private or static. (%s.%s)",
                    annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error("isValid", enclosingElement, msg);
            isValid = false;
        }

        // 父元素必须接口
        if (enclosingElement.getKind() != ElementKind.INTERFACE) {
            String msg = String.format("@%s %s may only be contained in interfaces. (%s.%s)", annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error("isValid",enclosingElement, msg);
            isValid = false;
        }

        //不能在Android框架层注解
        if (qualifiedName.startsWith("android.")) {
            String msg = String.format("@%s-annotated class incorrectly in Android framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error("isValid",enclosingElement, msg);
            isValid = false;
        }
        //不能在java框架层注解
        if (qualifiedName.startsWith("java.")) {
            String msg = String.format("@%s-annotated class incorrectly in Java framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error("isValid",enclosingElement, msg);
            isValid = false;
        }
        return isValid;
    }

    private final ISuperFieldDelegate mSuperDelegate = new ISuperFieldDelegate() {
        @Override
        public Set<FieldData> getDependFields(TypeElement te) {
            Set<FieldData> list = new HashSet<>();

            List<? extends AnnotationMirror> mirrors = te.getAnnotationMirrors();
            for(AnnotationMirror am : mirrors){
                DeclaredType type = am.getAnnotationType();
                if(type.toString().equals(Fields.class.getName())){
                    list.addAll(getProxyClass(te).getFieldDatas());
                }
            }
            //a depend b, b depend c ,,, etc.
            List<? extends TypeMirror> superInterfaces = te.getInterfaces();
            for(TypeMirror tm : superInterfaces){
                final TypeElement newTe = (TypeElement) ((DeclaredType) tm).asElement();
                list.addAll(getDependFields(newTe)); //recursion
            }
            return list;
        }
    };
}
