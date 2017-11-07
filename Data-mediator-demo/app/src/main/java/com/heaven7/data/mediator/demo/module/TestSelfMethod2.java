package com.heaven7.data.mediator.demo.module;

import com.heaven7.data.mediator.demo.activity.TestSelfMethodWithImplInterface;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * test self method /impl interface
 */
@Fields({
        @Field(propName = "text2")
})
public interface TestSelfMethod2 extends TestSelfMethodWithImplInterface.TestSelfMethod{

    Property PROP_text2 = SharedProperties.get("java.lang.String", "text2", 0);

    TestSelfMethod2 setText2(String text21);

    String getText2();/*
================== start methods from super properties ===============
======================================================================= */

    TestSelfMethod2 setText(String text1);
}