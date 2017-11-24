package com.heaven7.test;


import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.FlowItem;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.plugin.idea.data_mediator.test.TestParcelableData;

import java.util.List;

@Fields(value = {
        @Field(propName = "testItem2_1", type = Student.class,  since = 1.2, until = 2.0),
        @Field(propName = "testItem2_2" ),
        @Field(propName = "testItem2_3" , complexType = FieldFlags.COMPLEX_LIST),
        @Field(propName = "testItem2_4", type = boolean.class),
        @Field(propName = "testItem2_5", type = int.class),
        @Field(propName = "testItem2_6", type = TestParcelableData.class),
})
public interface TestItem2 extends FlowItem{

    Property PROP_testItem2_1 = SharedProperties.get(Student.class.getName(), "testItem2_1", 0);
    Property PROP_testItem2_2 = SharedProperties.get(String.class.getName(), "testItem2_2", 0);
    Property PROP_testItem2_3 = SharedProperties.get(String.class.getName(), "testItem2_3", 2);
    Property PROP_testItem2_4 = SharedProperties.get(boolean.class.getName(), "testItem2_4", 0);
    Property PROP_testItem2_5 = SharedProperties.get(int.class.getName(), "testItem2_5", 0);
    Property PROP_testItem2_6 = SharedProperties.get(TestParcelableData.class.getName(), "testItem2_6", 0);

    TestItem2 setTestItem2_1(Student testItem2_11);

    Student getTestItem2_1();

    TestItem2 setTestItem2_2(String testItem2_21);

    String getTestItem2_2();

    TestItem2 setTestItem2_3(List<String> testItem2_31);

    List<String> getTestItem2_3();

    ListPropertyEditor<? extends TestItem2, String> beginTestItem2_3Editor();

    TestItem2 setTestItem2_4(boolean testItem2_41);

    boolean isTestItem2_4();

    TestItem2 setTestItem2_5(int testItem2_51);

    int getTestItem2_5();

    TestItem2 setTestItem2_6(TestParcelableData testItem2_61);

    TestParcelableData getTestItem2_6();/*
================== start methods from super properties ===============
======================================================================= */

    TestItem2 setId(Student id1);

    TestItem2 setName(String name1);

    TestItem2 setDesc(List<String> desc1);

    ListPropertyEditor<? extends TestItem2, String> beginDescEditor();

    TestItem2 setSelected(boolean selected1);

    TestItem2 setXxx1(int xxx11);

    TestItem2 setXxx2(Integer xxx21);

    TestItem2 setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends TestItem2, Integer> beginXxx3Editor();

    TestItem2 setXxx4(int[] xxx41);

    TestItem2 setXxx5(Integer[] xxx51);
}
