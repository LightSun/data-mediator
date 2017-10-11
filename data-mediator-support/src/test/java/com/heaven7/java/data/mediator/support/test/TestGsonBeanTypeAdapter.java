package com.heaven7.java.data.mediator.support.test;

import com.google.gson.TypeAdapter;
import com.heaven7.java.data.mediator.support.GsonProperty;
import com.heaven7.java.data.mediator.support.adapter.DoubleSparseArrayTypeAdapter;

public class TestGsonBeanTypeAdapter extends BaseTypeAdapter<TestGsonBean> {

    public TestGsonBeanTypeAdapter() {
        super(
                GsonProperty.wrap(TestGsonBean.PROP_car),
                GsonProperty.wrap(TestGsonBean.PROP_mList),
                GsonProperty.wrap(TestGsonBean.PROP_carsArr),
                GsonProperty.wrap(TestGsonBean.PROP_carMap),

                GsonProperty.wrap(TestGsonBean.PROP_f_val),
                GsonProperty.wrap(TestGsonBean.PROP_doubles),
                GsonProperty.wrap(TestGsonBean.PROP_doubleList),
                GsonProperty.wrap(TestGsonBean.PROP_doubelSparse)
        );
    }

    @Override
    protected TypeAdapter getTypeAdapter(Class<?> itemType) {
        if(itemType == Car3.class){
            return new Car3TypeAdapter();
        }
        return null;
    }

    @Override
    protected TypeAdapter getSparseArrayTypeAdapter(Class<?> itemType) {
        if(itemType == Car3.class){
            return new Car3SparseArrayTypeAdapter();
        }else if(itemType == Double.class){
            return new DoubleSparseArrayTypeAdapter();
        }
        return null;
    }
}
