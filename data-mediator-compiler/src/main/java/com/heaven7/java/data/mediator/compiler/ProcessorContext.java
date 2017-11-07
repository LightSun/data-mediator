package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.ClassName;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Map;

/**
 * Created by heaven7 on 2017/11/5.
 */
public class ProcessorContext {

    private final ProcessorPrinter mPrinter;
    private final Filer mFiler;
    private final Elements mElements;
    private final Types mTypes;

    public ProcessorContext(Filer mFiler, Elements mElements, Types mTypes, ProcessorPrinter pp) {
        this.mFiler = mFiler;
        this.mElements = mElements;
        this.mTypes = mTypes;
        this.mPrinter = pp;
    }
    public ProcessorPrinter getProcessorPrinter() {
        return mPrinter;
    }
    public Filer getFiler() {
        return mFiler;
    }
    public Elements getElements() {
        return mElements;
    }
    public Types getTypes() {
        return mTypes;
    }

    public String getTargetClassName(TypeElement te){
       return Util.getTargetClassName(getElements().getPackageOf(te).getQualifiedName().toString(),
               te.getQualifiedName().toString());
    }
}
