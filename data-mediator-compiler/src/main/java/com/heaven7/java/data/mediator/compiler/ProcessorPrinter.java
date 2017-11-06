package com.heaven7.java.data.mediator.compiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.List;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public class ProcessorPrinter {

    private static final List<String> sEnableTags = Arrays.asList(
            "DataBindingAnnotationProcessor",
            "DataBindingParser"
    );
    //only debug show info log
    private static final boolean sDebug = true;
    private final Messager mMessager;

    public ProcessorPrinter(Messager mMessager) {
        this.mMessager = mMessager;
    }

    public void note(String tag, String method, Object... objs) {
        if(!sDebug){
            return;
        }
        if(hasTag(tag)) {
            print(Diagnostic.Kind.NOTE, "[ TAG ] = " + tag + ">>> called [ "
                    + method + "() ]: ", objs);
        }
    }
    public void error(String tag, String method, Object... objs) {
        print(Diagnostic.Kind.ERROR, "[ TAG ] = " + tag + ">>> called [ "
                + method + "() ]: ", objs);
    }
    public void warn(String tag, String method, Object... objs) {
        print(Diagnostic.Kind.WARNING, "[ TAG ] = " + tag + ">>> called [ "
                + method + "() ]: ", objs);
    }
    public void print(Diagnostic.Kind kind, Object obj1, Object... objs) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj1 != null ? obj1.toString() : null).append(" ");
        for (Object obj : objs) {
            sb.append(obj != null ? obj.toString() : null).append(" ");
        }
        mMessager.printMessage(kind, sb.toString());
    }
    private static boolean hasTag(String tag){
        return sEnableTags.size() == 0 || sEnableTags.contains(tag);
    }


}
