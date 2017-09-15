package com.heaven7.java.data.mediator.test;

import com.heaven7.java.base.util.Objects;

/**
 * Created by heaven7 on 2017/9/7.
 */
public class TestBean extends DataKnife{
    private Parcel parcel;

    public TestBean(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "parcel=" + parcel +
                "} " + super.toString();
    }

    public static void main(String[] args){
        String s = Objects.toStringHelper(TestBean.class)
                .addValue("hhh")
                .add("name", "heaven7")
                .add("age", 18)
                .addValue("ggggg")
                .toString();
        System.out.println(s);
    }
}
