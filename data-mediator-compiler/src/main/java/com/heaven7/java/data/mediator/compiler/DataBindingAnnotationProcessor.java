package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.bind.*;
import com.heaven7.java.data.mediator.compiler.databinding.parser.FieldAnnotationParser;
import com.heaven7.java.data.mediator.compiler.databinding.parser.FieldBindParser;
import com.heaven7.java.data.mediator.compiler.databinding.parser.FieldBindsParser;
import com.heaven7.java.data.mediator.compiler.generator.DataBindingGenerator;
import com.heaven7.java.data.mediator.compiler.util.CheckUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created by heaven7 on 2017/11/5.
 *
 * @since 1.4.0
 */
public class DataBindingAnnotationProcessor extends AbstractProcessor {

    private final String TAG = getClass().getName();
    private final HashMap<String, DataBindingInfo> mInfoMap = new HashMap<>();
    private final HashMap<String, TypeElement> mTargetMap = new HashMap<>();
    private ProcessorContext mContext;
    private DataBindingParser mParser;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        //binder
        set.add(BinderClass.class.getName());
        set.add(BinderFactoryClass.class.getName());
        // array
        set.add(BindsView.class.getName());
        set.add(BindsTextView.class.getName());
        //checkable
        set.add(BindCheckable.class.getName());
        //view
        set.add(BindEnable.class.getName());
        set.add(BindVisibility.class.getName());
        set.add(BindBackground.class.getName());
        set.add(BindBackgroundColor.class.getName());
        set.add(BindBackgroundRes.class.getName());
        //text view
        set.add(BindText.class.getName());
        set.add(BindTextRes.class.getName());
        set.add(BindTextSize.class.getName());
        set.add(BindTextSizeRes.class.getName());
        set.add(BindTextSizePx.class.getName());
        set.add(BindTextColor.class.getName());
        set.add(BindTextColorRes.class.getName());
        //image view
        set.add(BindImageBitmap.class.getName());
        set.add(BindImageDrawable.class.getName());
        set.add(BindImageRes.class.getName());
        set.add(BindImageUrl.class.getName());
        set.add(BindImageUri.class.getName());
        return Collections.unmodifiableSet(set);
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        mContext = new ProcessorContext(env.getFiler(), env.getElementUtils(),
                env.getTypeUtils(), new ProcessorPrinter(env.getMessager()));
        mParser = new DataBindingParser(mContext);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        //binder and binder factory
        Set<? extends Element> targets = roundEnv.getElementsAnnotatedWith(BinderClass.class);
        parseBinderClass(targets);
        targets = roundEnv.getElementsAnnotatedWith(BinderFactoryClass.class);
        parseBinderFactoryClass(targets);

        FieldBindsParser bindsParser = new FieldBindsParser();
        //================== Binds ===========================
        if(!parseField(roundEnv, BindsView.class, bindsParser)){
            return true;
        }
        if(!parseField(roundEnv, BindsTextView.class, bindsParser)){
            return true;
        }
        //============== bind =================
        FieldBindParser bindParser = new FieldBindParser();
        if(!parseField(roundEnv, BindVisibility.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindEnable.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindCheckable.class, bindParser)){
            return true;
        }
        //background
        if(!parseField(roundEnv, BindBackground.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindBackgroundColor.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindBackgroundRes.class, bindParser)){
            return true;
        }
        //text view
        if(!parseField(roundEnv, BindText.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextRes.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextSize.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextSizeRes.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextSizePx.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextColor.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindTextColorRes.class, bindParser)){
            return true;
        }
        //image view
        if(!parseField(roundEnv, BindImageBitmap.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindImageDrawable.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindImageRes.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindImageUrl.class, bindParser)){
            return true;
        }
        if(!parseField(roundEnv, BindImageUri.class, bindParser)){
            return true;
        }
        //handle super class
        for(Map.Entry<String, TypeElement> en : mTargetMap.entrySet()){
            String fullName = en.getKey();
            TypeElement te = en.getValue();
            mInfoMap.get(fullName).setSuperClass(getSuperClassForDataBinding(te));
        }
        //do generate
        DataBindingGenerator generator = new DataBindingGenerator(mContext);
        for(Map.Entry<String, TypeElement> en : mTargetMap.entrySet()){
            TypeElement te = en.getValue();
            if(!generator.generate(te, mInfoMap.get( en.getKey()))){
                return true;
            }
        }
        return false;
    }

    private TypeElement getSuperClassForDataBinding(TypeElement te) {
        TypeMirror superclass = te.getSuperclass();
        String superName = superclass.toString();
        if(superclass instanceof NoType){
            //no super.
        }else if(superName.startsWith("java.") || superName.startsWith("android.")){
            // no super too.
        }else{
            TypeElement newTe = new FieldData.TypeCompat(mContext.getTypes(), superclass).getElementAsType();
            DataBindingInfo info = mInfoMap.get(superName);
            if(info == null){
                //-------------- handle cross modules ------------
                //by class annotation
                if(hasDataBindingClassAnnotation(newTe)){
                    return newTe;
                }
                //by field annotation
                List<VariableElement> elements = ElementFilter.fieldsIn(newTe.getEnclosedElements());
                if(elements.size() > 0){
                    for(VariableElement ve: elements){
                        if(hasDataBindingFieldAnnotation(ve)){
                            return newTe;
                        }
                    }
                }
                //may super's N class is using data-binding
                return getSuperClassForDataBinding(newTe);
            }else{
                //found
                return newTe;
            }
        }
        return null;
    }

    private boolean hasDataBindingFieldAnnotation(VariableElement ve) {
        List<? extends AnnotationMirror> mirrors = ve.getAnnotationMirrors();
        for(AnnotationMirror am : mirrors){
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            //ignore private, static ,final
            Set<Modifier> modifiers = e1.getModifiers();
            if(modifiers.contains(Modifier.PRIVATE) ||
                    modifiers.contains(Modifier.STATIC) ||
                    modifiers.contains(Modifier.FINAL)){
                continue;
            }
            final String rootAnnoName = e1.getQualifiedName().toString();
            if(rootAnnoName.equals(BindVisibility.class.getName())
                    || rootAnnoName.equals(BindEnable.class.getName())
                    || rootAnnoName.equals(BindCheckable.class.getName())
                    || rootAnnoName.equals(BindBackground.class.getName())
                    || rootAnnoName.equals(BindBackgroundColor.class.getName())
                    || rootAnnoName.equals(BindBackgroundRes.class.getName())

                    || rootAnnoName.equals(BindsView.class.getName())
                    || rootAnnoName.equals(BindsTextView.class.getName())

                    || rootAnnoName.equals(BindText.class.getName())
                    || rootAnnoName.equals(BindTextRes.class.getName())
                    || rootAnnoName.equals(BindTextSize.class.getName())
                    || rootAnnoName.equals(BindTextSizePx.class.getName())
                    || rootAnnoName.equals(BindTextSizeRes.class.getName())
                    || rootAnnoName.equals(BindTextColorRes.class.getName())
                    || rootAnnoName.equals(BindTextColor.class.getName())

                    || rootAnnoName.equals(BindImageBitmap.class.getName())
                    || rootAnnoName.equals(BindImageDrawable.class.getName())
                    || rootAnnoName.equals(BindImageRes.class.getName())
                    || rootAnnoName.equals(BindImageUrl.class.getName())
                    || rootAnnoName.equals(BindImageUri.class.getName())
                    ){
                return true;
            }
        }
        return false;
    }

    private boolean hasDataBindingClassAnnotation(TypeElement newTe) {
        List<? extends AnnotationMirror> mirrors = newTe.getAnnotationMirrors();
        for(AnnotationMirror am : mirrors){
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            final String rootAnnoName = e1.getQualifiedName().toString();
            if(rootAnnoName.equals(BinderClass.class.getName()) || rootAnnoName.equals(BinderFactoryClass.class.getName())){
                 return true;
            }
        }
        return false;
    }

    private boolean parseBinderFactoryClass(Set<? extends Element> targets) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        for (Element e : targets) {
            if (!CheckUtils.isValidClass(BinderFactoryClass.class, e, pp)) {
                return false;
            }
            TypeElement te = (TypeElement) e;
            final DataBindingInfo info = getDataBindingInfo(te);
            if(!mParser.parseBinderFactoryClass(te, info)){
                return false;
            }
        }
        return true;
    }

    private boolean parseBinderClass(Set<? extends Element> targets) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        for (Element e : targets) {
            if (!CheckUtils.isValidClass(BinderClass.class, e, pp)) {
                return false;
            }
            TypeElement te = (TypeElement) e;
            final DataBindingInfo info = getDataBindingInfo(te);
            if(!mParser.parseBinderClass(te, info)){
                return false;
            }
        }
        return true;
    }
    private DataBindingInfo getDataBindingInfo(TypeElement e) {
        final String fullName = e.getQualifiedName().toString();
        DataBindingInfo info = mInfoMap.get(fullName);
        if (info == null) {
            info = new DataBindingInfo();
            mInfoMap.put(fullName, info);
            mTargetMap.put(fullName, e);
        }
        return info;
    }
    private boolean parseField(RoundEnvironment env, Class<? extends Annotation> clazz, FieldAnnotationParser parser) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        for (Element e : env.getElementsAnnotatedWith(clazz)) {
            if (!CheckUtils.isValidField(clazz, e, pp)) {
                return false;
            }
            TypeElement te = (TypeElement) e.getEnclosingElement();
            pp.note(TAG, "parseField", "field parent element is : " + te.getQualifiedName().toString());
            final DataBindingInfo info = getDataBindingInfo(te);
            if(!parser.parse(e, mParser, info)){
                return false;
            }
        }
        return true;
    }
}
