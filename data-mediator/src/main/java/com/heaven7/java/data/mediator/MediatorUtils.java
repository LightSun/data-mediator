package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */
public class MediatorUtils {

    public static boolean hasFlag(int flags, int require){
        return (flags & require) == require;
    }
}
