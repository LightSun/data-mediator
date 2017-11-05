package com.heaven7.java.data.mediator;

/**
 * the binder factory which is used to create binder.<br>
 * Created by heaven7 on 2017/11/5.
 * @author heaven7
 * @since 1.3.1
 */
public interface BinderFactory {

    /**
     * create binder by target data mediator.
     * @param <T> the module data type
     * @param dm the data mediator.
     * @return the binder which is used for data-binding. return null means use default binder.
     */
    <T> Binder<T> createBinder(DataMediator<T>  dm);
}
