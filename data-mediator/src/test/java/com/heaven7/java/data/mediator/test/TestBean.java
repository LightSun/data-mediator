package com.heaven7.java.data.mediator.test;

import com.heaven7.java.base.util.Objects;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/7.
 */
public class TestBean extends DataKnife{
    private Parcel parcel;
    private List<String> list;
    private String[] array;

    public TestBean(Parcel parcel) {
        this.parcel = parcel;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public void setArray(String[] array) {
        this.array = array;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "parcel=" + parcel +
                ", list=" + list +
                ", array=" + Arrays.toString(array) +
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

        Class<?> aClass = null;
        try {
            aClass = Class.forName("[Ljava.lang.String;");
            System.out.println(aClass.isArray());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            TestBean bean = new TestBean(null);
            Method setArray = TestBean.class.getMethod("setArray",
                    Array.newInstance(String.class,0).getClass());
            setArray.invoke(bean, (Object)new String[]{"123", "456"});

            Method setList = TestBean.class.getMethod("setList",
                    List.class);
            setList.invoke(bean, Arrays.asList("123", "456"));
            System.out.println(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
