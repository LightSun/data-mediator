package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.bind.*;
import com.heaven7.java.data.mediator.compiler.util.CheckUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by heaven7 on 2017/11/5.
 *
 * @since 1.4.0
 */
public class DataBindingAnnotationProcessor extends AbstractProcessor {

    private final HashMap<String, DataBindingInfo> mInfoMap = new HashMap<>();
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
        set.add(BindsTextViewRes.class.getName());
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
        Set<? extends Element> targets = roundEnv.getElementsAnnotatedWith(BinderClass.class);
        parseBinderClass(targets);
        targets = roundEnv.getElementsAnnotatedWith(BinderFactoryClass.class);
        parseBinderFactoryClass(targets);

        return false;
    }

    private boolean parseBinderFactoryClass(Set<? extends Element> targets) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        for (Element e : targets) {
            if (!CheckUtils.isValidClass(BinderClass.class, e, pp)) {
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
        }
        return info;
    }
}
