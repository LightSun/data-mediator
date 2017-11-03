package com.heaven7.java.data.mediator.test.bind;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import java.lang.reflect.Method;
import java.net.BindException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//BaseTarget
public class Mock_databind<T> {

    private final T target;
    private Set<BindInfo> binds = new HashSet<>();

    public Mock_databind(T target){
        this.target = target;
        //start generate code
        //addBindInfo(xxx)
    }

    public T getTarget(){
        return target;
    }
    protected void addBindInfo(Object view, String propName, int index, String methodName, Class<?>[] methodTypes){
        binds.add(new BindInfo(view, propName, index, methodName, methodTypes));
    }

    public <D> Binder<D> bind(D data, int index){
        return bind(data, index, null);
    }

    public <D> Binder<D> bind(D data, int index, PropertyInterceptor interceptor) {
        Binder<D> binder =  DataMediatorFactory.createBinder(data);
        if(interceptor != null) {
            binder.setPropertyInterceptor(interceptor);
        }
        for(BindInfo info : binds){
            if(info.index == index){
                invoke(binder, info);
            }
        }
        return binder;
    }
    public <D> Binder<D> bindAndApply(D data, int index, PropertyInterceptor interceptor) {
        Binder<D> binder = bind(data, index, interceptor);
        binder.applyProperties();
        return binder;
    }

    public void bind(Object data){
        bind(data, 0);
    }

    private static void invoke(Binder<?> binder, BindInfo info){
        try {
            Method method = binder.getClass().getMethod(info.methodName, info.methodTypes);
            method.invoke(binder, new Object[]{ info.propName, info.view });
        } catch (Exception e) {
            throw new RuntimeException("can't find method, name = " + info.methodName + " ,info.methodTypes = "
                    + (info.methodTypes != null ? Arrays.toString(info.methodTypes) : null));
        }
    }
    public static class BindInfo{
        final Object view;
        final String propName;
        final int index;

        final String methodName;
        final Class<?>[] methodTypes;

        public BindInfo(Object view, String propName, int index, String methodName, Class<?>[] methodTypes) {
            this.view = view;
            this.methodName = methodName;
            this.propName = propName;
            this.index = index;
            this.methodTypes = methodTypes;
        }
    }

}
