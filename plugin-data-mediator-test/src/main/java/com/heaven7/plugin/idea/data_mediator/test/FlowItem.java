package com.heaven7.plugin.idea.data_mediator.test;


import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.lang.String;
import java.util.List;

@Fields(value = {
        @Field(propName = "id", type = Student.class,  since = 1.2, until = 2.0),
        @Field(propName = "name" ),
        @Field(propName = "desc" , complexType = FieldFlags.COMPLEX_LIST),
        @Field(propName = "selected", type = boolean.class),
        @Field(propName = "xxx1", type = int.class),
        @Field(propName = "xxx2", type = Integer.class),
        @Field(propName = "xxx3", type = int.class, complexType = FieldFlags.COMPLEX_SPARSE_ARRAY),
        @Field(propName = "xxx4", type = int.class, complexType = FieldFlags.COMPLEX_ARRAY),
        @Field(propName = "xxx5", type = Integer.class, complexType = FieldFlags.COMPLEX_ARRAY),
})
public interface FlowItem extends DataPools.Poolable {

    Property PROP_id = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.Student", "id", 0);
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
    Property PROP_desc = SharedProperties.get("java.lang.String", "desc", 2);
    Property PROP_selected = SharedProperties.get("boolean", "selected", 0);
    Property PROP_xxx1 = SharedProperties.get("int", "xxx1", 0);
    Property PROP_xxx2 = SharedProperties.get("java.lang.Integer", "xxx2", 0);
    Property PROP_xxx3 = SharedProperties.get("int", "xxx3", 3);
    Property PROP_xxx4 = SharedProperties.get("int", "xxx4", 1);
    Property PROP_xxx5 = SharedProperties.get("java.lang.Integer", "xxx5", 1);

    Student getId();

    FlowItem setId(Student id1);

    String getName();

    FlowItem setName(String name1);

    List<String> getDesc();

    FlowItem setDesc(List<String> desc1);

    ListPropertyEditor<? extends FlowItem, String> beginDescEditor();

    boolean isSelected();

    FlowItem setSelected(boolean selected1);

    int getXxx1();

    FlowItem setXxx1(int xxx11);

    Integer getXxx2();

    FlowItem setXxx2(Integer xxx21);

    SparseArray<Integer> getXxx3();

    FlowItem setXxx3(SparseArray<Integer> xxx31);

    SparseArrayPropertyEditor<? extends FlowItem, Integer> beginXxx3Editor();

    int[] getXxx4();

    FlowItem setXxx4(int[] xxx41);

    Integer[] getXxx5();

    FlowItem setXxx5(Integer[] xxx51);
}
