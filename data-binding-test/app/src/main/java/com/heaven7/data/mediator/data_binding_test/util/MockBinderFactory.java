package com.heaven7.data.mediator.data_binding_test.util;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.BinderFactory;
import com.heaven7.java.data.mediator.DataMediator;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class MockBinderFactory implements BinderFactory {

    @Override
    public <T> Binder<T> createBinder(Object target, DataMediator<T> dm) {
        return null;
    }
}
