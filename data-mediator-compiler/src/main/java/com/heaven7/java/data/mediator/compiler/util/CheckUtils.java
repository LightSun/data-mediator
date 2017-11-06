package com.heaven7.java.data.mediator.compiler.util;

import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.Set;

import static javax.lang.model.element.Modifier.*;

/**
 * Created by heaven7 on 2017/11/5.
 */
public class CheckUtils {

    private static final String TAG = "CheckUtils";

    public static boolean isValidField(Class<? extends Annotation> clazz,
                                       Element element, ProcessorPrinter pp) {
        VariableElement enclosingElement = (VariableElement) element;
        //full name which is annotated by @Fields
        String qualifiedName = enclosingElement.getSimpleName().toString();
        boolean isValid = true;

        // can't be private, current can't be static
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            String msg = String.format("field '%s' annotated by @%s must not be private or static.",
                    qualifiedName,  clazz.getSimpleName());
            pp.note(TAG, "isValid", msg);
            isValid = false;
        }

        // must be field
        if (element.getKind() != ElementKind.FIELD) {
            String msg = String.format("@%s can only be contained in field.",
                    clazz.getSimpleName());
            pp.note(TAG, "isValid", msg);
            isValid = false;
        }
        return isValid;
    }

    public static boolean isValidClass(Class<? extends Annotation> clazz,
                                  Element element, ProcessorPrinter pp) {

        TypeElement enclosingElement = (TypeElement) element;
        //full name which is annotated by @BinderClass/BinderFactoryClass
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        // anno = com.heaven7.java.data.mediator.Fields ,parent element is: com.heaven7.data.mediator.demo.Student
        pp.note(TAG,"isValid","anno = " + clazz.getName()
                + " ,full name is: " + qualifiedName);

        boolean isValid = true;
        // can't be private, current can't be static
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            String msg = String.format("class %s annotated by @%s must not be private or static.",
                    qualifiedName,  clazz.getSimpleName());
            pp.note(TAG, "isValid", enclosingElement, msg);
            isValid = false;
        }
        //must be public
        if (!modifiers.contains(PUBLIC)) {
            String msg = String.format("class %s annotated by @%s must be public.",
                    qualifiedName, clazz.getSimpleName());
            pp.note(TAG, "isValid", enclosingElement, msg);
            isValid = false;
        }
        // must be class
        if (enclosingElement.getKind() != ElementKind.CLASS) {
            String msg = String.format("@%s from class %scan only be contained in class.",
                    clazz.getSimpleName(), qualifiedName);
            pp.note(TAG, "isValid",enclosingElement, msg);
            isValid = false;
        }

        //can't used to android.**
        if (qualifiedName.startsWith("android.")) {
            String msg = String.format("@%s-annotated class incorrectly in Android framework package. (%s)",
                    clazz.getSimpleName(), qualifiedName);
            pp.note(TAG, "isValid",enclosingElement, msg);
            isValid = false;
        }
        //can't used to java.**
        if (qualifiedName.startsWith("java.")) {
            String msg = String.format("@%s-annotated class incorrectly in Java framework package. (%s)",
                    clazz.getSimpleName(), qualifiedName);
            pp.note(TAG, "isValid",enclosingElement, msg);
            isValid = false;
        }
        return isValid;
    }
}
