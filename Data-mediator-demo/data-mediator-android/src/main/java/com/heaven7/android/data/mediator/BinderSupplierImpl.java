package com.heaven7.android.data.mediator;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.IBinderSupplier;
/**
 * the binder supplier
 * Created by heaven7 on 2017/9/24.
 */
public class BinderSupplierImpl<T> implements IBinderSupplier<T> {
    @Override
    public Binder<T> create(DataMediator<T> mediator) {
        return new AndroidBinder<T>(mediator);
    }
}
