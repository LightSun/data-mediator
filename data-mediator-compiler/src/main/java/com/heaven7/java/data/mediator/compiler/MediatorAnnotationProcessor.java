package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.GlobalConfig;
import com.heaven7.java.data.mediator.ImplClass;
import com.heaven7.java.data.mediator.ImplMethod;
import com.heaven7.java.data.mediator.compiler.generator.GroupPropertyGenerator;
import com.heaven7.java.data.mediator.compiler.generator.StaticLoaderGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@SupportedAnnotationTypes({
        "com.heaven7.java.data.mediator.Fields",
        "com.heaven7.java.data.mediator.GlobalConfig"
})                       //use "*" indicate support all Annotations
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MediatorAnnotationProcessor extends AbstractProcessor implements CodeGeneratorProvider,
        GroupPropertyGenerator.TypeElementDelegate{

    private static final String TAG = "MediatorAnnotationProcessor";
    private Filer mFiler;

    private Elements mElementUtils;
    //private Messager mMessager;
    private ProcessorPrinter mPrinter;
    private Types mTypeUtils;

    private final Map<String, CodeGenerator> mProxyClassMap = new ConcurrentHashMap<>();

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
        final ImplInfoDelegate implInfoDelegate = new ImplInfoDelegate(mTypeUtils, mPrinter);
        //@fields and @ImplXXX
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Fields.class);
        for (Element element : elements) {
            note(":process" ,"@Fields >>> element = " + element);
            if (!isValid(Fields.class, "interface", element)) {
                return true;
            }
            CodeGenerator generator = getProxyClass(element);
            //@Fields
            if(!ElementHelper.processAnnotation(mElementUtils, mTypeUtils, mPrinter,
                    (TypeElement) element, generator)){
                return true;
            }
            //@ImplClass and @ImplMethods.
            if(!implInfoDelegate.parseImplInfo((TypeElement) element, generator)){
                return true;
            }
        }
       //generate module interface / impl /proxy (interface removed)
       for (CodeGenerator generator : mProxyClassMap.values()) {
           if(!generator.generateJavaFile(mFiler, mPrinter, this)){
               return true;
           }
        }
        mProxyClassMap.clear();
        return false;
    }

    private CodeGenerator getProxyClass(Element element) {
        //element which is annotated .
        TypeElement classElement = (TypeElement) element;
        String qualifiedName = classElement.getQualifiedName().toString();
        CodeGenerator proxyClass = mProxyClassMap.get(qualifiedName);
        if (proxyClass == null) {
            proxyClass = new CodeGenerator(mTypeUtils, mElementUtils, classElement);
            mProxyClassMap.put(qualifiedName, proxyClass);
        }
        return proxyClass;
    }

    private boolean isValid(Class<? extends Annotation> annotationClass,
                            String targetThing, Element element) {

        TypeElement enclosingElement = (TypeElement) element;
        //full name which is annotated by @Fields
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        // anno = com.heaven7.java.data.mediator.Fields ,parent element is: com.heaven7.data.mediator.demo.Student
        note("isValid","anno = " + annotationClass.getName()
                + " ,full name is: " + qualifiedName);

        boolean isValid = true;
        // can't be private,
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) ) {
            String msg = String.format("@%s %s must not be private or static. (%s.%s)",
                    annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error("isValid", enclosingElement, msg);
            isValid = false;
        }
        //must be public
        if (!modifiers.contains(PUBLIC)) {
            String msg = String.format("@%s %s must be public. (%s.%s)",
                    annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error("isValid", enclosingElement, msg);
            isValid = false;
        }

        // must be interface
        if (enclosingElement.getKind() != ElementKind.INTERFACE) {
            String msg = String.format("@%s %s may only be contained in interfaces. (%s.%s)",
                    annotationClass.getSimpleName(),
                    targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            error("isValid",enclosingElement, msg);
            isValid = false;
        }

        //can't used to android.**
        if (qualifiedName.startsWith("android.")) {
            String msg = String.format("@%s-annotated class incorrectly in Android framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error("isValid",enclosingElement, msg);
            isValid = false;
        }
        //can't used to java.**
        if (qualifiedName.startsWith("java.")) {
            String msg = String.format("@%s-annotated class incorrectly in Java framework package. (%s)",
                    annotationClass.getSimpleName(), qualifiedName);
            error("isValid",enclosingElement, msg);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public CodeGenerator getCodeGenerator(Element element) {
        return getProxyClass(element);
    }

    @Override
    public TypeElement get(String qualifyName) {
        CodeGenerator cg = mProxyClassMap.get(qualifyName);
        if(cg == null){
            return null;
        }
        return cg.getTypeElement();
    }
    @Override
    public ProcessorContext getContext() {
        return new ProcessorContext(mFiler, mElementUtils, mTypeUtils, mPrinter);
    }
}
