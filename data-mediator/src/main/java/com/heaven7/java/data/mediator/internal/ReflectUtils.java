package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.Property;

import java.lang.reflect.Method;

/**
 * the reflect utils for data-mediator.
 * @author heaven7
 * @since 1.4.5
 */
public class ReflectUtils {

    public static Object getValue(Property p, Object obj) {
        String name = p.getName();
        final String prefix = p.getType() != boolean.class ? "get" : "is";
        String getMethodName = prefix + name.substring(0, 1)
                .toUpperCase()
                .concat(name.substring(1));
        try {
            Method getXXX = obj.getClass().getMethod(getMethodName);
            return getXXX.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setValue(Property p, Object obj, Object val) {
        String name = p.getName();
        String setMethodName = "set" + name.substring(0, 1)
                .toUpperCase()
                .concat(name.substring(1));
        try {
            Method setXXX = obj.getClass().getMethod(setMethodName, p.getActualType());
            setXXX.invoke(obj, val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
