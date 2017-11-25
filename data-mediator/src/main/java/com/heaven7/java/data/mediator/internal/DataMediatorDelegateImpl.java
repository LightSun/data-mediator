package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;

/**
 * Created by heaven7 on 2017/10/5.
 */
@Hide
/*public*/ class DataMediatorDelegateImpl extends DataMediatorDelegate {
    @Override
    public <T> Binder<T> createBinder(DataMediator<T> mediator) {
        throw new UnsupportedOperationException();
    }
    @Override
    public ParcelDelegate getParcelDelegate(Object parcel) {
        throw new UnsupportedOperationException();
    }
}
