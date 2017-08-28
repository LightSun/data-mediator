package com.heaven7.java.data.mediator.processor;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public class ProcessorPrinter {

    private final Messager mMessager;

    public ProcessorPrinter(Messager mMessager) {
        this.mMessager = mMessager;
    }

    public void note(Object msg, Object... objs) {
        print(Diagnostic.Kind.NOTE, msg, objs);
    }

    public void error(Object obj1, Object... objs) {
        print(Diagnostic.Kind.ERROR, obj1, objs);
    }

    public void print(Diagnostic.Kind kind, Object obj1, Object... objs) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj1.toString()).append(" ");
        for (Object obj : objs) {
            sb.append(obj.toString()).append(" ");
        }
        mMessager.printMessage(kind, sb.toString());
    }

}
