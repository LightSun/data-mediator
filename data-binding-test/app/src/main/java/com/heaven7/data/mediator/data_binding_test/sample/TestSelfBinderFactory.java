package com.heaven7.data.mediator.data_binding_test.sample;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.BinderFactory;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.bind.BinderFactoryClass;

/**
 * test self binder factory{@linkplain BinderFactoryClass}.
 * Created by heaven7 on 2017/11/13 0013.
 * @see TestSelfBinderActivity
 */
@BinderFactoryClass(TestSelfBinderFactory.BinderFactoryImpl.class)
public class TestSelfBinderFactory  extends BaseSelfBinderActivity{


    public static class BinderFactoryImpl implements BinderFactory{
        @Override
        public <T> Binder<T> createBinder(Object target, DataMediator<T> dm) {
            return new TestSelfBinderActivity.MyBinder<T>(dm);
        }
    }
}
