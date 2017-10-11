package com.heaven7.java.data.mediator.support;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.data.mediator.Property;

import java.io.IOException;
import java.lang.reflect.Method;

public class SupportUtils {

    public static  TypeAdapter getTypeAdapter(Class<?> itemType){
        return null;
    }

    public static   TypeAdapter getSparseArrayTypeAdapter(Class<?> itemType){
        //TODO

        return null;
    }

    public static Object getValue(Property p, Object obj) {
        String name = p.getName();
        String getMethodName = "get" + name.substring(0, 1)
                .toUpperCase()
                .concat(name.substring(1));
        try {
            Method getXXX = obj.getClass().getMethod(getMethodName);
            return getXXX.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writePrimitiveOrItsBox(JsonWriter writer, Class<?> type, Object value) throws IOException {
        if (type == void.class || type == Void.class) {
            writer.value("\"\"");
        } else if (type == boolean.class || type == Boolean.class) {
            writer.value(((Boolean) value).booleanValue());
        } else if (type == byte.class || type == Byte.class) {
            writer.value(((Byte) value).byteValue());
        } else if (type == short.class || type == Short.class) {
            writer.value(((Short) value).shortValue());
        } else if (type == int.class || type == Integer.class) {
            writer.value(((Integer) value).intValue());
        } else if (type == long.class || type == Long.class) {
            writer.value(((Long) value).longValue());
        } else if (type == char.class || type == Character.class) {
            writer.value((Character) value);
        } else if (type == float.class || type == Float.class) {
            writer.value(((Float) value).floatValue());
        } else if (type == double.class || type == Double.class) {
            writer.value(((Double) value).doubleValue());
        }else{
            throw new IllegalStateException();
        }
    }
}
