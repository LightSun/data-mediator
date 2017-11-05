package com.heaven7.java.data.mediator.internal;

import java.util.List;

/**
 * Created by heaven7 on 2017/11/5.
 * @since 1.4.0
 */
public class InternalUtils {

    /**
     * convert to classes
     * @param types the type strings
     * @return the classes.
     */
    public static Class<?>[] convert2Classes(List<String> types){
        final int size = types.size();
        Class<?>[] arr = new Class[size];
        try {
            for (int i = 0; i < size; i++) {
                arr[i] = Class.forName(types.get(i));
            }
            return arr;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
