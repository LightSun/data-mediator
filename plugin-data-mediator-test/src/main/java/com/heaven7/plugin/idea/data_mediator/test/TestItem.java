package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

@Fields(value = {
        @Field(propName = "testItem_1", type = Student.class,  since = 1.2, until = 2.0),
        @Field(propName = "testItem_2" ),
        @Field(propName = "testItem_3" , complexType = FieldFlags.COMPLEX_LIST),
        @Field(propName = "testItem_4", type = boolean.class),
        @Field(propName = "testItem_5", type = int.class),
        @Field(propName = "testItem_6", type = TestParcelableData.class),
})
@ImplClass(Student.class)
public interface TestItem extends FlowItem{

    Property PROP_testItem_1 = SharedProperties.get(Student.class.getName(), "testItem_1", 0);
    Property PROP_testItem_2 = SharedProperties.get(String.class.getName(), "testItem_2", 0);
    Property PROP_testItem_3 = SharedProperties.get(String.class.getName(), "testItem_3", 2);
    Property PROP_testItem_4 = SharedProperties.get(boolean.class.getName(), "testItem_4", 0);
    Property PROP_testItem_5 = SharedProperties.get(int.class.getName(), "testItem_5", 0);
    Property PROP_testItem_6 = SharedProperties.get(TestParcelableData.class.getName(), "testItem_6", 0);

    @ImplMethod()
    void test();
    @ImplMethod("test2")
    void _test2();

    TestItem setTestItem_1(Student testItem_11);

    Student getTestItem_1();

    TestItem setTestItem_2(String testItem_21);

    String getTestItem_2();

    TestItem setTestItem_3(List<String> testItem_31);

    List<String> getTestItem_3();

    ListPropertyEditor<? extends TestItem, String> beginTestItem_3Editor();

    TestItem setTestItem_4(boolean testItem_41);

    boolean isTestItem_4();

    TestItem setTestItem_5(int testItem_51);

    int getTestItem_5();

    TestItem setTestItem_6(TestParcelableData testItem_61);

    TestParcelableData getTestItem_6();/*
================== start methods from super properties ===============
======================================================================= */

    TestItem setId(Student id1);

    TestItem setName(String name1);

    TestItem setDesc(List<String> desc1);

    ListPropertyEditor<? extends TestItem, String> beginDescEditor();

    TestItem setSelected(boolean selected1);

    TestItem setXxx1(int xxx11);

    TestItem setXxx2(Integer xxx21);

    TestItem setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends TestItem, Integer> beginXxx3Editor();

    TestItem setXxx4(int[] xxx41);

    TestItem setXxx5(Integer[] xxx51);
}
