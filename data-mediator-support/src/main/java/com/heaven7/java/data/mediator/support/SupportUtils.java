/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator.support;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.data.mediator.Property;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 *
 */
public class SupportUtils {

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

    public static void setValue(Property p, Object obj, Object val){
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
    public static Object readPrimitiveOrItsBox(JsonReader reader, Property p) throws IOException {
        Class<?> type = p.getType();
        if (type == void.class || type == Void.class) {
            return null;
        } else if (type == boolean.class || type == Boolean.class) {
            return reader.nextBoolean();
        } else if (type == byte.class || type == Byte.class) {
            return (byte)reader.nextInt();
        } else if (type == short.class || type == Short.class) {
            return (short)reader.nextInt();
        } else if (type == int.class || type == Integer.class) {
            return reader.nextInt();
        } else if (type == long.class || type == Long.class) {
            return reader.nextLong();
        } else if (type == char.class || type == Character.class) {
            return (char)reader.nextLong();
        } else if (type == float.class || type == Float.class) {
            return (float)reader.nextDouble();
        } else if (type == double.class || type == Double.class) {
            return reader.nextDouble();
        } else {
            throw new IllegalStateException();
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
        } else {
            throw new IllegalStateException();
        }
    }

    public static boolean isBoxedClass(Class<?> clazz) {
        if (clazz == Boolean.class
                || clazz == Void.class
                || clazz == Byte.class
                || clazz == Short.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Character.class
                || clazz == Float.class
                || clazz == Double.class
                ) {
            return true;
        }
        return false;
    }
    /*public static void log(Object obj){
        System.out.println(obj);
    }*/
}
