package com.heaven7.plugin.idea.data_mediator.test;


import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

@Fields(value = {
        @Field(propName = "testItem10_1"),
})
public interface TestItem10 extends TestItem{
    Property PROP_testItem10_1 = SharedProperties.get(String.class.getName(), "testItem10_1", 0);

    /*
================== start super methods =============== */

    TestItem10 setTestItem10_1(String testItem10_11);

    String getTestItem10_1();/*
================== start methods from super properties ===============
======================================================================= */

    TestItem10 setTestItem_1(Student testItem_11);

    TestItem10 setTestItem_2(String testItem_21);

    TestItem10 setTestItem_3(List<Student> testItem_31);

    ListPropertyEditor<? extends TestItem10, Student> beginTestItem_3Editor();

    TestItem10 setTestItem_4(boolean testItem_41);

    TestItem10 setTestItem_5(int testItem_51);

    TestItem10 setTestItem_6(TestParcelableData testItem_61);

    TestItem10 setId(Student id1);

    TestItem10 setName(String name1);

    TestItem10 setDesc(List<String> desc1);

    ListPropertyEditor<? extends TestItem10, String> beginDescEditor();

    TestItem10 setSelected(boolean selected1);

    TestItem10 setXxx1(int xxx11);

    TestItem10 setXxx2(Integer xxx21);

    TestItem10 setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends TestItem10, Integer> beginXxx3Editor();

    TestItem10 setXxx4(int[] xxx41);

    TestItem10 setXxx5(Integer[] xxx51);
}
