package com.heaven7.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.plugin.idea.data_mediator.test.TestParcelableData;

import java.util.List;

/**
 * Created by heaven7 on 2017/10/25.
 */
@Fields({
        @Field(propName = "testItem3")
})
public interface TestItem3 extends TestItem2{

    Property PROP_testItem3 = SharedProperties.get(String.class.getName(), "testItem3", 0);

    TestItem3 setTestItem3(String testItem31);

    String getTestItem3();/*
================== start methods from super properties ===============
======================================================================= */

    TestItem3 setTestItem2_1(Student testItem2_11);

    TestItem3 setTestItem2_2(String testItem2_21);

    TestItem3 setTestItem2_3(List<String> testItem2_31);

    ListPropertyEditor<? extends TestItem3, String> beginTestItem2_3Editor();

    TestItem3 setTestItem2_4(boolean testItem2_41);

    TestItem3 setTestItem2_5(int testItem2_51);

    TestItem3 setTestItem2_6(TestParcelableData testItem2_61);

    TestItem3 setId(Student id1);

    TestItem3 setName(String name1);

    TestItem3 setDesc(List<String> desc1);

    ListPropertyEditor<? extends TestItem3, String> beginDescEditor();

    TestItem3 setSelected(boolean selected1);

    TestItem3 setXxx1(int xxx11);

    TestItem3 setXxx2(Integer xxx21);

    TestItem3 setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends TestItem3, Integer> beginXxx3Editor();

    TestItem3 setXxx4(int[] xxx41);

    TestItem3 setXxx5(Integer[] xxx51);
}
