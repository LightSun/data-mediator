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
package com.heaven7.java.data.mediator;

import com.heaven7.java.data.mediator.internal.DataMediatorDelegate;

import java.lang.reflect.Constructor;

/**
 * Created by heaven7 on 2017/9/14 0014.
 * @since 1.0.4
 */
public final class DataMediatorFactory {

    /*private*/ static final String  SUFFIX_IMPL   = "_$Impl";
    private static final String  SUFFIX_PROXY  = "_$Proxy";

    /**
     * create group properties recursively as 'Gps'.
     * @param clazz the data/interface class.
     * @return the 'Gps' or null if the target GPS not exist.
     * @since 1.4.5
     */
    public static Gps createGps(Class<?> clazz){
        Class<?> target = clazz;
        do {
            try {
                Class<?> class_gps = Class.forName(target.getName() + "_$GPS");
                return (Gps) class_gps.newInstance();
            }catch (ClassNotFoundException e){
                target = target.getSuperclass();
                if(target == null || target.getName().startsWith("java.")
                        || target.getName().startsWith("android.")){
                    //throw new RuntimeException("create $Gps failed, caused by "+ clazz.getName() + "_$Gps doesn't exists !");
                    return null;
                }
            }catch (InstantiationException | IllegalAccessException e){
                throw new RuntimeException(e);
            }
        }while (true);
    }

    /**
     * create data-binding and bind data to the views.
     * @param target the target object .which is the owner of view.
     * @param data the module data
     * @param <T> the module data type
     * @return the binder of target data.
     * @since 1.4.0
     */
    public static <T> Binder<T> bind(Object target, T data){
        return createDataBinding(target).bind(data, 0, null);
    }

    /**
     * create data-binding and bind data to the views.
     * @param target the target object .which is the owner of view.
     * @param data the module data
     * @param index the index of data. often used to bind multi data in one owner.
     * @param <T> the module data type
     * @return the binder of target data.
     * @since 1.4.0
     */
    public static <T> Binder<T> bind(Object target, T data, int index){
        return createDataBinding(target).bind(data, index, null);
    }

    /**
     * create data-binding and bind data to the views.
     * @param target the target object .which is the owner of view.
     * @param data the module data
     * @param index the index of data. often used to bind multi data in one owner.
     * @param interceptor the property interceptor
     * @param <T> the module data type
     * @return the binder of target data.
     * @since 1.4.0
     */
    public static <T> Binder<T> bind(Object target, T data,
                                     int index, PropertyInterceptor interceptor){
        return createDataBinding(target).bind(data, index, interceptor);
    }

    /**
     * create data-binding for target object.
     * @param target the target
     * @param <T> the target type.
     * @return the data-binding class associate with the target object.
     * @since 1.4.0
     */
    @SuppressWarnings("unchecked")
    public static <T> DataBinding<T> createDataBinding(T target){
        //DataBinding_xxx
        final Class<?> clazz = target.getClass();
        try {
            Class<?> clazz_databinding =  Class.forName(clazz.getName()
                    + "_$DataBinding");
            return (DataBinding<T>) clazz_databinding.getConstructor(clazz).newInstance(target);
        } catch (Exception e) {
            throw new RuntimeException("create data-binding failed, caused by can't bind the relative " +
                    "data-binding class!");
        }
    }

    /**
     * obtain the data for target class type.
     * @param clazz the data interface module
     * @param <T> the data module type
     * @return the data. never null.
     * @since 1.1.0
     */
    public static <T> T obtainData(Class<T> clazz){
        return DataPools.obtain(clazz);
    }

    /**
     * create module data for target interface class.
     * @param clazz the interface class.
     * @param <T> the module type. must be interface.
     * @return the module data.
     */
    public static <T> T createData(Class<T> clazz){
        try {
            return (T) Class.forName(getImplClassName(clazz)).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create module for class("+ clazz.getName()
                    + ")! have you make project or rebuild ? ", e);
        }
    }

    /**
     * create data mediator for target interface class.
     * @param clazz the interface class
     * @param <T> the module type
     * @return the proxy helper for target module type.
     */
    public static <T> DataMediator<T> createDataMediator(Class<T> clazz){
        T t = createData(clazz);
        try {
            Class<?> proxyClazz = Class.forName(getProxyClassName(clazz));
            return new DataMediator<T>((BaseMediator<T>) proxyClazz.getConstructor(clazz).newInstance(t));
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create module proxy for class("+ clazz.getName()
                    + ")! have you make project or rebuild ? " ,e);
        }
    }

    /**
     * <p><h2>use {@linkplain #createDataMediator(Object)} instead.</h2></p>
     * wrap the module data to data mediator.
     * @param t  the module data.
     * @param <T> the module data type
     * @return the data mediator .
     * @see DataMediator
     */
    @Deprecated
    public static <T> DataMediator<T> wrapToDataMediator(T t){
        return createDataMediator(t);
    }

    /**
     * create date mediator by target module.
     * @param t the module data
     * @param <T> the module data type
     * @return the data mediator.
     * @since 1.1.3
     */
    public static <T> DataMediator<T> createDataMediator(T t){
        return createDataMediator(null, t);
    }

    /**
     * create data mediator for target 'data-impl'. and attach inflated callbacks to the DataMediator if need.
     * @param root the root DataMediator for target data. or null if no need. see {@linkplain DataMediator#inflatePropertyChain(String)}
     * @param t the target data-impl
     * @param <T> the module data type.
     * @return the {@linkplain DataMediator} of target object.
     * @since 1.4.4
     */
    @SuppressWarnings("unchecked")
    public static <T> DataMediator<T> createDataMediator(DataMediator<?> root, T t){
        DataMediator<T> target ;
        if(t instanceof BaseMediator){
            target =  new DataMediator<T>((BaseMediator<T>) t);
        }else {
            String ImplName = t.getClass().getName();
            if (!ImplName.endsWith(SUFFIX_IMPL)) {
                throw new IllegalArgumentException("target object(" + ImplName + ") can't wrap to DataMediator.");
            }
            final String interfaceName = ImplName.substring(0, ImplName.lastIndexOf(SUFFIX_IMPL));
            try {
                Class<?> proxyClazz = Class.forName(interfaceName + SUFFIX_PROXY);
                Constructor<?> constructor = proxyClazz.getConstructor(Class.forName(interfaceName));
                target = new DataMediator<T>((BaseMediator<T>) constructor.newInstance(t));
            } catch (Exception e) {
                throw new IllegalArgumentException("can't create module proxy for class(" + ImplName
                        + ")! have you make project or rebuild ? ", e);
            }
        }
        if(root != null) {
            for (DataMediatorCallback inflateCallback : root.getInflateCallbacks(target.getData())) {
                target.addDataMediatorCallback(inflateCallback);
            }
        }
        return target;
    }

    /**
     * create binder for target data mediator.
     * @param mediator the data mediator.
     * @param <T> the module data type
     * @return the binder.
     * @since 1.0.8
     */
    public static <T> Binder<T> createBinder(DataMediator<T> mediator) {
         return DataMediatorDelegate.getDefault().createBinder(mediator);
    }
    /**
     * create binder for target module class..
     * @param moduleClass the module data class.
     * @param <T> the module data type
     * @return the binder.
     * @since 1.0.8
     */
    public static <T> Binder<T> createBinder(Class<T> moduleClass){
        return createBinder(createDataMediator(moduleClass));
    }
    /**
     * create binder for target module data.
     * @param module the module data.
     * @param <T> the module data type
     * @return the binder.
     * @since 1.4.0
     */
    public static <T> Binder<T> createBinder(T module){
        return createBinder(createDataMediator(module));
    }

    /**
     * get the impl class name of interface which is generate by data-mediator.
     * @param interClazz the interface class.
     * @param <T> the data module type
     * @return the impl class name.
     * @since 1.1.3
     */
    public static <T> String getImplClassName(Class<T> interClazz){
        return  checkAndGetName(interClazz) + SUFFIX_IMPL;
    }
    /**
     * get the impl class name of interface which is generate by data-mediator.
     * @param interClazz the interface class.
     * @param <T> the data module type
     * @return the impl class name.
     * @since 1.1.3
     */
    public static <T> String getProxyClassName(Class<T> interClazz){
        return  checkAndGetName(interClazz) + SUFFIX_PROXY;
    }

    private static <T> String checkAndGetName(Class<T> interClazz) {
        if(!interClazz.isInterface()){
            throw new IllegalArgumentException();
        }
        return interClazz.getName();
    }

}
