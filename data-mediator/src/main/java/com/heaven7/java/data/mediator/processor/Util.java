package com.heaven7.java.data.mediator.processor;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.heaven7.java.data.mediator.processor.FieldData.FLAG_TRANSIENT;
import static com.heaven7.java.data.mediator.processor.FieldData.FLAG_VOLATILE;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class Util {

    public static void test(TypeMirror mirror, ProcessorPrinter pp){
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        Name paramType = te.getQualifiedName();
        pp.note("test() >>> paramType = " + paramType.toString());

        final List<? extends Element> list = te.getEnclosedElements();
        for(Element e: list){
            ExecutableElement ee = (ExecutableElement) e;
            pp.note("ee_getSimpleName: " + ee.getSimpleName());
            pp.note("ee_return: " + ee.getReturnType());
            pp.note("ee_getTypeParameters: " + ee.getTypeParameters());
            pp.note("ee_getThrownTypes: " + ee.getThrownTypes());
            pp.note("ee_getReceiverType: " + ee.getReceiverType());
        }
        pp.note("test() >>> getTypeParameters = " + te.getTypeParameters());
        pp.note("test() >>> getEnclosedElements = " + list);
        pp.note("test() >>> getEnclosedElements__2 = " + Arrays.toString(
                list.get(0).getClass().getInterfaces()));
        pp.note("test() >>> getEnclosingElement = " + te.getEnclosingElement());
       // pp.note("test() >>> getInterfaces = " + te.getInterfaces());
    }

    //serializeable.
    static javax.lang.model.element.Modifier[] getFieldModifier(FieldData fieldData){
        final int flags = fieldData.getFlags();
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PRIVATE);
        if( (flags & FLAG_TRANSIENT) == FLAG_TRANSIENT){
            modifiers.add(Modifier.TRANSIENT);
        }
        //TODO
        if( (flags & FLAG_VOLATILE) == FLAG_VOLATILE){
            modifiers.add(Modifier.VOLATILE);
        }
        return modifiers.toArray(new Modifier[modifiers.size()]);
    }
    /**
     * get property name. eg: name to Name(get, set)
     * @param prop the target name
     * @return the changed name for get/set method
     */
    public static String getPropNameForMethod(String prop) {
        if(prop == null || "".equals(prop.trim())){
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0,1).toUpperCase().concat(prop.substring(1));
    }

    /**
     * get parameter name . eg: int to int1 Object to Object1
     * @param name the raw name of param
     * @return the changed parameter name.
     */
    public static String getParamName(String name) {
        return name.substring(0,1).toLowerCase()
                .concat(name.substring(1))
                .concat("1");
    }

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        t.printStackTrace(pw);
        Throwable cause = t.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.flush();
        String data = sw.toString();
        pw.close();
        return data;
    }
}
