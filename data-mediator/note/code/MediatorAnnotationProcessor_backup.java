package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.Fields;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;

import static com.heaven7.java.data.mediator.processor.Util.*;
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
    private static final String TARGET_PACKAGE = "com.heaven7.java.data.mediator";
    private Filer mFiler;           //文件相关工具类

    private Elements mElementUtils; //元素相关的工具类
    //private Messager mMessager;     //日志相关的工具类
    private ProcessorPrinter mPrinter; ////日志相关的处理
    private Types mTypeUtils;

    private Map<String, ProxyClass> mProxyClassMap = new HashMap<>();

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
            ProxyClass proxyClass = getProxyClass(element);
            if(!ElementHelper.processAnnotation(mTypeUtils, mPrinter, element.getAnnotationMirrors(), proxyClass.getFieldDatas())){
                return true;
            }
           /* if (!parseModuleByFields(element)) {
                return true;
            }*/
        }
       //generate code
       for (ProxyClass proxyClass_ : mProxyClassMap.values()) {
            proxyClass_.generateProxy(mPrinter, mFiler);
        }
        mProxyClassMap.clear();
        return false;
    }

    //element 我们要的注解元素 (Fields)
    private boolean parseModuleByFields(Element element) {

        ProxyClass proxyClass = getProxyClass(element);
        //获得该元素上的注解
        List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        for (AnnotationMirror am : mirrors) {
            Element e1 = am.getAnnotationType().asElement();
            //note("am.getAnnotationType().getTypeArguments(): " + am.getAnnotationType().getTypeArguments());
           // note("am.getAnnotationType().getEnclosingType(): " + am.getAnnotationType().getEnclosingType()); //none
            Element e1_enclosing = e1.getEnclosingElement();
            TypeKind kind = e1.asType().getKind();
            note("e1.kind = " + kind.name()); //DECLARED
            if(!(e1_enclosing instanceof QualifiedNameable)){
                continue;
            }
            note( "e1_enclosing).getQualifiedName(): " + ((QualifiedNameable) e1_enclosing).getQualifiedName());
            if(!TARGET_PACKAGE.equals( ((QualifiedNameable) e1_enclosing).getQualifiedName().toString() )){
                continue;
            }

           // note(e1.getEnclosingElement() instanceof TypeElement);         //false
           // note(">>>>>>> test: " + e1.getEnclosedElements());             //value()
           // note(">>>>>>> test: " + e1_enclosing);                                 //com.heaven7.java.data.mediator
           // note(">>>>>>> test: " + (e1_enclosing instanceof QualifiedNameable));       //true
           // note(">>>>>>> test: " + (e1.getEnclosingElement() instanceof TypeElement)); //false

            note("e1: " + e1, ",simple name = " + e1.getSimpleName(), ",e1.getName() = "
                    + e1.getClass().getSimpleName());
           // note("e1_is_anno: " + (e1.getKind() == ElementKind.ANNOTATION_TYPE)); //true
           // note("e1_Fields: " + e1.getAnnotation(Fields.class)); //null
          //  note("e1_Field: " + e1.getAnnotation(Field.class));   //null
            note("AnnotationMirror: " + am);

            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
            note("am.getElementValues() = map . is " + map);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
                ExecutableElement key = en.getKey();
                note("key: " + key);//the method of annotation
                AnnotationValue value = en.getValue();
                Object target = value.getValue();
                if (target == null || !(target instanceof List)) {
                    error("@Fields's value() must be a list.");
                    return false;
                }
                List list = (List) target;
                if(list.isEmpty()){
                    error("@Fields's value() must have value list.");
                    return false;
                }
                Object obj = list.get(0);
                if(!(obj instanceof AnnotationMirror)){
                    error("@Fields's value() must have list of @Field.");
                    return false;
                }
                if(!iterateField((List<? extends AnnotationMirror>)list, proxyClass)){
                    return false;
                }
               // proxyClass.setDataModuleFields(list);
            }
        }
        return true;
    }

    private boolean iterateField(List<? extends AnnotationMirror> list, ProxyClass proxyClass) {
        note("=================== start iterateField() ====================");
        for(AnnotationMirror am1 : list) {
            Element element = am1.getAnnotationType().asElement().getEnclosingElement();
            if (!(element instanceof QualifiedNameable)) {
                error("annotation element not instanceof QualifiedNameable)");
                return false;
            }
            if (!TARGET_PACKAGE.equals(((QualifiedNameable) element).getQualifiedName().toString())) {
                return false;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am1.getElementValues();

            FieldData data = new FieldData();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {

                ExecutableElement key = en.getKey();
                AnnotationValue av = en.getValue();
                note("test >>>: " + av.getValue());

                switch (key.getSimpleName().toString()) {
                    case STR_PROP_NAME:
                        data.setPropertyName(av.getValue().toString());
                        break;

                    case STR_SERIA_NAME:
                        data.setSerializeName(av.getValue().toString());
                        break;

                    case STR_FLAGS:
                        data.setFlags(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_COMPLEXT_TYPE:
                        data.setComplexType(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_TYPE:
                        note("STR_TYPE >>> " + av.getValue().toString());
                        FieldData.TypeCompat tc = new FieldData.TypeCompat(mTypeUtils, (TypeMirror) av.getValue());
                        data.setTypeCompat(tc);
                        tc.replaceIfNeed(mPrinter);
                        break;

                    default:
                        note("unsupport name = " + key.getSimpleName().toString());
                }
            }
            proxyClass.addFieldData(data);
        }
        return true;
    }

    /**
     * 生成或获取注解元素所对应的ProxyClass类
     */
    private ProxyClass getProxyClass(Element element) {
        //被注解的变量所在的类
        TypeElement classElement = (TypeElement) element;
        String qualifiedName = classElement.getQualifiedName().toString();
        ProxyClass proxyClass = mProxyClassMap.get(qualifiedName);
        if (proxyClass == null) {
            proxyClass = new ProxyClass(mTypeUtils, mElementUtils, classElement);
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
