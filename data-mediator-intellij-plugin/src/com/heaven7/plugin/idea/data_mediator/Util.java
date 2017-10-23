package com.heaven7.plugin.idea.data_mediator;

import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiPrimitiveType;

import static com.heaven7.plugin.idea.data_mediator.Property.*;

public class Util {

    public static void logError(Object... objs) {
        log(true, objs);
    }
    public static void log(Object... objs) {
        log(false, objs);
    }
    private static void log(boolean error, Object... objs) {
        StringBuilder sb = new StringBuilder();
        if (objs != null) {
            for (Object obj : objs) {
                sb.append(obj != null ? obj.toString() : null);
                sb.append("\r\n");
            }
        }
        if (error) {
            System.err.println(sb.toString());
        } else {
            System.out.println(sb.toString());
        }
    }

    /** make sure the value is boolean. */
    public static boolean getBooleanValue(PsiAnnotationMemberValue value){
         return Boolean.valueOf(value.getText());
    }

    public static String getPropNameForMethod(String prop) {
        if (prop == null || "".equals(prop.trim())) {
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0, 1)
                .toUpperCase()
                .concat(prop.substring(1));
    }
}
