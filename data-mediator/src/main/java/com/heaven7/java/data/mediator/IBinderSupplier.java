package com.heaven7.java.data.mediator;

/**
 * the binder supplier
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public interface IBinderSupplier<T> {

    /**
     * create binder by target data mediator.
     * @param mediator the data mediator.
     * @return the binder.
     */
    Binder<T> create(DataMediator<T> mediator);
}
