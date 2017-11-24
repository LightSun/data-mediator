package com.heaven7.plugin.idea.data_mediator.test.convert;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

public class Outter {

    public static class Inner{
        private String name;
    }

    public static class TestInner1 extends Outter2.Inner2_1.Inner2_2{
        private String testInner1;
    }

    @Fields(value = {@Field(propName = "testInner1")
    }, generateJsonAdapter = false)
    public interface ITestInner1 extends Outter2.Inner2_1.IInner2_2 {
        Property PROP_testInner1 = SharedProperties.get(String.class.getName(), "testInner1", 0);

        ITestInner1 setTestInner1(String testInner11);

        String getTestInner1();/*
================== start methods from super properties ===============
======================================================================= */

        ITestInner1 setText(String text1);
    }

}
