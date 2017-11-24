package com.heaven7.test;

import java.util.Arrays;

public class LogUtils {

    public static void log(Object obj){
        System.out.println(obj);
    }
    public static void log(String tag,String method , Object...objs){
        System.out.println("TAG = "+ tag + "  called [ "+method+"() ]:" + Arrays.toString(objs));
    }
}
