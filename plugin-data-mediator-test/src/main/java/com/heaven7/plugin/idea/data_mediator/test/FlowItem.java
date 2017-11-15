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

    Property PROP_id = SharedProperties.get(Student.class.getName(), "id", 0);
    Property PROP_name = SharedProperties.get(String.class.getName(), "name", 0);
    Property PROP_desc = SharedProperties.get(String.class.getName(), "desc", 2);
    Property PROP_selected = SharedProperties.get(boolean.class.getName(), "selected", 0);
    Property PROP_xxx1 = SharedProperties.get(int.class.getName(), "xxx1", 0);
    Property PROP_xxx2 = SharedProperties.get(Integer.class.getName(), "xxx2", 0);
    Property PROP_xxx3 = SharedProperties.get(int.class.getName(), "xxx3", 3);
    Property PROP_xxx4 = SharedProperties.get(int.class.getName(), "xxx4", 1);
    Property PROP_xxx5 = SharedProperties.get(Integer.class.getName(), "xxx5", 1);

    FlowItem setId(Student id1);

    Student getId();

    FlowItem setName(String name1);

    String getName();

    FlowItem setDesc(List<String> desc1);

    List<String> getDesc();

    ListPropertyEditor<? extends FlowItem, String> beginDescEditor();

    FlowItem setSelected(boolean selected1);

    boolean isSelected();

    FlowItem setXxx1(int xxx11);

    int getXxx1();

    FlowItem setXxx2(Integer xxx21);

    Integer getXxx2();

    FlowItem setXxx3(SparseArray<Integer> xxx31);

    SparseArray<Integer> getXxx3();

    SparseArrayPropertyEditor<? extends FlowItem, Integer> beginXxx3Editor();

    FlowItem setXxx4(int[] xxx41);

    int[] getXxx4();

    FlowItem setXxx5(Integer[] xxx51);

    Integer[] getXxx5();
}
