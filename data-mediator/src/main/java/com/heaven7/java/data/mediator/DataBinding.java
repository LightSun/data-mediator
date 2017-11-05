package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * the super class of data binding.
 * @param <T> the object which whose view will be bind.
 * @author heaven7
 * @since 1.3.1
 */
public abstract class DataBinding<T> {

    private final HashSet<BindInfo> mBinds = new HashSet<>();
    private final T mTarget;
    private BinderFactory mBinderFactory;
    private Class<? extends Binder> mBinderClass;

    /**
     * create data-binding for target object.
     * @param target the target object.
     */
    public DataBinding(T target) {
        Throwables.checkNull(target);
        this.mTarget = target;
    }

    /**
     * set the binder class.
     * @param binderClass the binder class.
     */
    public void setBinderClass(Class<? extends Binder> binderClass) {
        this.mBinderClass = binderClass;
    }
    /**
     * set binder factory
     * @param factory the binder factory.
     */
    public void setBinderFactory(BinderFactory factory) {
        this.mBinderFactory = factory;
    }
    /**
     * get the target
     * @return the target
     */
    public final T getTarget() {
        return mTarget;
    }

    /**
     * add a bind info
     * @param view the view to bind
     * @param propName the property name
     * @param index the index of this bind . this only used to discern multi data bind.
     * @param methodName the method name of binder
     * @param methodTypes the method types of binder
     */
    protected void addBindInfo(Object view, String propName, int index,
                               String methodName, Class<?>[] methodTypes) {
        mBinds.add(new BindInfo(view, propName, index, methodName, methodTypes));
    }

    /**
     * bind data to the all defined  bind views
     * @param data the module data
     * @param <D> the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data) {
        return bind(data, 0);
    }

    /**
     * bind data to the all defined bind views
     * @param data the module data
     * @param index the index
     * @param <D> the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data, int index) {
        return bind(data, index, null);
    }

    /**
     * bind data to the all defined bind views
     * @param data the module data
     * @param index the index
     * @param interceptor the property interceptor
     * @param <D> the module data type
     * @return the binder
     */
    @SuppressWarnings("unchecked")
    public <D> Binder<D> bind(D data, int index, @Nullable PropertyInterceptor interceptor) {
        final DataMediator<D> dm = DataMediatorFactory.createDataMediator(data);
        //create binder . order : binder class-> binderFactor -> default.
        Binder<D> binder = null;
        if(mBinderClass != null){
            try {
                binder = mBinderClass.getConstructor(dm.getClass()).newInstance(dm);
            } catch (Exception e) {
                System.err.println(String.format("can't create binder for target class($s), " +
                        "start use BinderFactory or default Binder.", mBinderClass.getName()) );
            }
        }
        if(binder == null) {
            if (mBinderFactory != null) {
                binder = mBinderFactory.createBinder(getTarget(), dm);
            }
            if (binder == null) {
                binder = DataMediatorFactory.createBinder(dm);
            }
        }
        if (interceptor != null) {
            binder.setPropertyInterceptor(interceptor);
        }
        for (BindInfo info : mBinds) {
            if (info.index == index) {
                bindInternal(binder, info);
            }
        }
        return binder;
    }
    /**
     * bind data to the all defined bind views. and apply it immediately.
     * @param data the module data
     * @param index the index
     * @param interceptor the property interceptor
     * @param <D> the module data type
     * @return the binder
     */
    public <D> Binder<D> bindAndApply(D data, int index, PropertyInterceptor interceptor) {
        Binder<D> binder = bind(data, index, interceptor);
        binder.applyProperties();
        return binder;
    }

    private static void bindInternal(Binder<?> binder, BindInfo info) {
        try {
            Method method = binder.getClass().getMethod(info.methodName, info.methodTypes);
            method.invoke(binder, new Object[]{info.propName, info.view});
        } catch (Exception e) {
            throw new RuntimeException("can't find method, name = " + info.methodName + " ,info.methodTypes = "
                    + (info.methodTypes != null ? Arrays.toString(info.methodTypes) : null));
        }
    }

    /**
     * bind info: which indicate once bind of view .
     * @author heaven7
     * @since 1.3.1
     */
    private static class BindInfo {
        final Object view;
        final String propName;
        final int index;

        final String methodName;
        final Class<?>[] methodTypes;

        BindInfo(Object view, String propName, int index, String methodName, Class<?>[] methodTypes) {
            this.view = view;
            this.methodName = methodName;
            this.propName = propName;
            this.index = index;
            this.methodTypes = methodTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BindInfo bindInfo = (BindInfo) o;
            return Objects.equals(view, bindInfo.view) &&
                    Objects.equals(propName, bindInfo.propName);
        }

        @Override
        public int hashCode() {
            return  Arrays.hashCode(new Object[]{view, propName});
        }
    }
}
