package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/9/14 0014.
 * @since 1.0.4
 */
public final class DataMediators {

    private static final String  SUFFIX_INTERFACE = "Module";
    private static final String  SUFFIX_IMPL   = "_Impl";
    private static final String  SUFFIX_PROXY  = "_Proxy";

    /**
     * create module data for target interface class.
     * @param clazz the interface class.
     * @param <T> the module type. must be interface.
     * @return the module data.
     */
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

    /**
     * create proxy helper by target interface class.
     * @param clazz the interface class
     * @param <T> the module type
     * @return the proxy helper for target module type.
     */
    public static <T> ProxyHelper<T> createProxyHelper(Class<T> clazz){
        String name = clazz.getName();
        if(name.endsWith(SUFFIX_INTERFACE)){
            name = name + SUFFIX_PROXY;
        }
        T t = createModule(clazz);
        try {
            Class<?> proxyClazz = Class.forName(name);
            return new ProxyHelper<T>((BaseMediator<T>) proxyClazz.getConstructor(clazz).newInstance(t));
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create module proxt for class("+ clazz.getName()
                    + ")! have you make project or rebuild ? " ,e);
        }
    }



}
