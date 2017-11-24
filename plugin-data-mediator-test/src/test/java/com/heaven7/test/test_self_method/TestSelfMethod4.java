package com.heaven7.test.test_self_method;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.Student;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields({
        @Field(propName = "test_self4_1")
})
public interface TestSelfMethod4 extends TestSelfMethod3{

    Property PROP_test_self4_1 = SharedProperties.get(String.class.getName(), "test_self4_1", 0);

    TestSelfMethod4 setTest_self4_1(String test_self4_11);

    String getTest_self4_1();/*
================== start methods from super properties ===============
======================================================================= */

    TestSelfMethod4 setTest_1(boolean test_11);

    TestSelfMethod4 setTest_2(String test_21);

    TestSelfMethod4 setId(Student id1);

    TestSelfMethod4 setName(String name1);

    TestSelfMethod4 setTest_self1(String test_self11);

    TestSelfMethod4 setTest_self2(int test_self21);
}
