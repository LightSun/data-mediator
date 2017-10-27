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
================== start super methods =============== */

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
