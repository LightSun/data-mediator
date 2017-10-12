package com.heaven7.java.data.mediator.support.test;

import com.heaven7.java.data.mediator.support.BaseTypeAdapter;
import com.heaven7.java.data.mediator.support.GsonProperty;
import com.heaven7.java.data.mediator.support.TypeHandler;

import java.util.Arrays;

public class TestGsonBeanTypeAdapter extends BaseTypeAdapter<TestGsonBean> {

    static {
        TypeHandler.registerTypeAdapter(Car3.class, new Car3TypeAdapter());
    }

    public TestGsonBeanTypeAdapter() {
        super(Arrays.asList(
                GsonProperty.wrap(TestGsonBean.PROP_car),
                GsonProperty.wrap(TestGsonBean.PROP_mList),
                GsonProperty.wrap(TestGsonBean.PROP_carsArr),
                GsonProperty.wrap(TestGsonBean.PROP_carMap),

                GsonProperty.wrap(TestGsonBean.PROP_f_val),
                GsonProperty.wrap(TestGsonBean.PROP_doubles),
                GsonProperty.wrap(TestGsonBean.PROP_doubleList),
                GsonProperty.wrap(TestGsonBean.PROP_doubelSparse))
        );
    }

    @Override
    protected TestGsonBean create() {
        return new TestGsonBean();
    }
}
