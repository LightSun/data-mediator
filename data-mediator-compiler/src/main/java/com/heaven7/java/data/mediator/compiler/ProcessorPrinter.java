package com.heaven7.java.data.mediator.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class ProcessorPrinter {

    private static final boolean sDebug = false;
    private final Messager mMessager;

    public ProcessorPrinter(Messager mMessager) {
        this.mMessager = mMessager;
    }

    public void note(String tag, String method, Object... objs) {
        print(Diagnostic.Kind.NOTE, "[ TAG ] = " + tag + ">>> called [ "
                + method + "() ]: ", objs);
    }
    public void error(String tag, String method, Object... objs) {
        print(Diagnostic.Kind.ERROR, "[ TAG ] = " + tag + ">>> called [ "
                + method + "() ]: ", objs);
    }
    public void warn(String tag, String method, Object... objs) {
        print(Diagnostic.Kind.WARNING, "[ TAG ] = " + tag + ">>> called [ "
                + method + "() ]: ", objs);
    }
    public void note(Object msg, Object... objs) {
        print(Diagnostic.Kind.NOTE, msg, objs);
    }
    public void error(Object obj1, Object... objs) {
        print(Diagnostic.Kind.ERROR, obj1, objs);
    }
    public void print(Diagnostic.Kind kind, Object obj1, Object... objs) {
        if(!sDebug){
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(obj1 != null ? obj1.toString() : null).append(" ");
        for (Object obj : objs) {
            sb.append(obj != null ? obj.toString() : null).append(" ");
        }
        mMessager.printMessage(kind, sb.toString());
    }


}
