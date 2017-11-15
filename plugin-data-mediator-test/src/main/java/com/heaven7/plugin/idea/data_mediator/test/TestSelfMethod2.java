package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.util.TestUtil;
import com.heaven7.plugin.idea.data_mediator.test.util.TestUtil2;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields(value = {
        @Field(propName = "id", type = Student.class),
        @Field(propName = "name" ),
})
@ImplClass(TestUtil2.class)
public interface TestSelfMethod2 extends TestSelfMethod1{

    Property PROP_id = SharedProperties.get(Student.class.getName(), "id", 0);
    Property PROP_name = SharedProperties.get(String.class.getName(), "name", 0);

    @ImplMethod(value = "parseStudent", from = TestUtil.class)
    void parseStudent2(Student stu, int key);

    @ImplMethod()
    int getStudentId2(Student stu, int key);

    TestSelfMethod2 setId(Student id1);

    Student getId();

    TestSelfMethod2 setName(String name1);

    String getName();/*
================== start methods from super properties ===============
======================================================================= */

    TestSelfMethod2 setTest_self1(String test_self11);

    TestSelfMethod2 setTest_self2(int test_self21);
}
