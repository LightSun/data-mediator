package com.heaven7.plugin.idea.data_mediator.test;


import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

@Fields(value = {
        @Field(propName = "testItem10_1"),
})
public interface TestItem10 extends TestItem{

    Property PROP_testItem10_1 = SharedProperties.get("java.lang.String", "testItem10_1", 0);
    Property PROP_testItem_1 = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.Student", "testItem_1", 0);
    Property PROP_testItem_2 = SharedProperties.get("java.lang.String", "testItem_2", 0);
    Property PROP_testItem_3 = SharedProperties.get("java.lang.String", "testItem_3", 2);
    Property PROP_testItem_4 = SharedProperties.get("boolean", "testItem_4", 0);
    Property PROP_testItem_5 = SharedProperties.get("int", "testItem_5", 0);
    Property PROP_testItem_6 = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.TestParcelableData", "testItem_6", 0);
    Property PROP_id = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.Student", "id", 0);
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
    Property PROP_desc = SharedProperties.get("java.lang.String", "desc", 2);
    Property PROP_selected = SharedProperties.get("boolean", "selected", 0);
    Property PROP_xxx1 = SharedProperties.get("int", "xxx1", 0);
    Property PROP_xxx2 = SharedProperties.get("java.lang.Integer", "xxx2", 0);
    Property PROP_xxx3 = SharedProperties.get("int", "xxx3", 3);
    Property PROP_xxx4 = SharedProperties.get("int", "xxx4", 1);
    Property PROP_xxx5 = SharedProperties.get("java.lang.Integer", "xxx5", 1);

    TestItem10 setTestItem10_1(String testItem10_11);

    String getTestItem10_1();/*
================== start super methods =============== */

    TestItem10 setTestItem_1(Student testItem_11);

    TestItem10 setTestItem_2(String testItem_21);

    TestItem10 setTestItem_3(List<String> testItem_31);

    ListPropertyEditor<? extends TestItem10, String> beginTestItem_3Editor();

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
