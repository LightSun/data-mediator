package com.heaven7.java.data.mediator.compiler;

import javax.lang.model.element.TypeElement; /**
 * Created by heaven7 on 2017/11/5.
 */
public class DataBindingParser {

    private final ProcessorContext mContext;

    public DataBindingParser(ProcessorContext mContext) {
        this.mContext = mContext;
    }

    public boolean parseBinderClass(TypeElement te, DataBindingInfo info) {
        //TODO
        return false;
    }

    public boolean parseBinderFactoryClass(TypeElement te, DataBindingInfo info) {
        return false;
    }
}
