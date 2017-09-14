package com.heaven7.data.mediator.demo.util;

import com.heaven7.java.data.mediator.BaseMediator;

/**
 * Created by heaven7 on 2017/9/14 0014.
 */
public final class DataMediators {

    private static final String  SUFFIX_INTERFACE = "Module";
    private static final String  SUFFIX_IMPL   = "_Impl";
    private static final String  SUFFIX_PROXY  = "_Proxy";

    public static <T> T createModule(Class<T> clazz){
        String name = clazz.getName();
        if(name.endsWith(SUFFIX_INTERFACE)){
            name = name + SUFFIX_IMPL;
        }
        try {
            return (T) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create module for class("+ clazz.getName()
                    + ")! have you make project or rebuild ? ", e);
        }
    }
    public static <T> BaseMediator<T> createProxy(Class<T> clazz){
        String name = clazz.getName();
        if(name.endsWith(SUFFIX_INTERFACE)){
            name = name + SUFFIX_PROXY;
        }
        T t = createModule(clazz);
        try {
            Class<?> proxyClazz = Class.forName(name);
            return (BaseMediator<T>) proxyClazz.getConstructor(clazz).newInstance(t);
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create module proxt for class("+ clazz.getName()
                    + ")! have you make project or rebuild ? " ,e);
        }
    }


}
