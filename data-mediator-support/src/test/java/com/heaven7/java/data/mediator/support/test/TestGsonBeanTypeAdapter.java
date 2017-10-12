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
                GsonProperty.of(TestGsonBean.PROP_car),
                GsonProperty.of(TestGsonBean.PROP_mList),
                GsonProperty.of(TestGsonBean.PROP_carsArr),
                GsonProperty.of(TestGsonBean.PROP_carMap),

                GsonProperty.of(TestGsonBean.PROP_f_val),
                GsonProperty.of(TestGsonBean.PROP_doubles),
                GsonProperty.of(TestGsonBean.PROP_doubleList),
                GsonProperty.of(TestGsonBean.PROP_doubelSparse))
        );
    }

    @Override
    protected TestGsonBean create() {
        return new TestGsonBean();
    }
}
