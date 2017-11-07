package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by heaven7 on 2017/11/5.
 * @since 1.4.0
 */
public class BaseGenerator {

    protected final String TAG = getClass().getSimpleName();

    private final ProcessorContext mContext;

    public BaseGenerator(ProcessorContext context) {
        this.mContext = context;
    }

    public ProcessorContext getContext() {
        return mContext;
    }
    public ProcessorPrinter getProcessorPrinter() {
        return mContext.getProcessorPrinter();
    }
    public Filer getFiler() {
        return mContext.getFiler();
    }
    public Elements getElements() {
        return mContext.getElements();
    }
    public Types getTypes() {
        return mContext.getTypes();
    }
}
