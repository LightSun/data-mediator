package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

/**
 * Created by heaven7 on 2017/10/25.
 */
@Fields({
        @Field(propName = "testItem100", type = FlowItem.class)
})
@ImplClass(Student.class)
public interface TestItem100 extends TestItem{

    Property PROP_testItem100 = SharedProperties.get(FlowItem.class.getName(), "testItem100", 0);

    TestItem100 setTestItem100(FlowItem testItem1001);

    FlowItem getTestItem100();/*
================== start methods from super properties ===============
======================================================================= */

    TestItem100 setTestItem_1(Student testItem_11);

    TestItem100 setTestItem_2(String testItem_21);

    TestItem100 setTestItem_3(List<String> testItem_31);

    ListPropertyEditor<? extends TestItem100, String> beginTestItem_3Editor();

    TestItem100 setTestItem_4(boolean testItem_41);

    TestItem100 setTestItem_5(int testItem_51);

    TestItem100 setTestItem_6(TestParcelableData testItem_61);

    TestItem100 setId(Student id1);

    TestItem100 setName(String name1);

    TestItem100 setDesc(List<String> desc1);

    ListPropertyEditor<? extends TestItem100, String> beginDescEditor();

    TestItem100 setSelected(boolean selected1);

    TestItem100 setXxx1(int xxx11);

    TestItem100 setXxx2(Integer xxx21);

    TestItem100 setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends TestItem100, Integer> beginXxx3Editor();

    TestItem100 setXxx4(int[] xxx41);

    TestItem100 setXxx5(Integer[] xxx51);
}
