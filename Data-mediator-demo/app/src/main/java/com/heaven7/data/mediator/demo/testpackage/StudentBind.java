package com.heaven7.data.mediator.demo.testpackage;

import com.heaven7.adapter.ISelectable;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_ARRAY;
import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_LIST;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_COPY;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_EXPOSE_DEFAULT;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_EXPOSE_SERIALIZE_FALSE;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_RESET;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_SHARE;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_SNAP;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@Fields(value = {
        @Field(propName = "name", seriaName = "heaven7", type = String.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_object", seriaName = "test_object",
                flags = FLAG_EXPOSE_DEFAULT | FLAG_EXPOSE_SERIALIZE_FALSE, type = Object.class),
        @Field(propName = "test_Format", seriaName = "test_Format", flags = 1, type = Double.class),
        @Field(propName = "test_int", seriaName = "test_int", type = int.class,
                flags = FLAG_EXPOSE_DEFAULT | FLAG_COPY | FLAG_RESET),
        @Field(propName = "test_list", seriaName = "test_list", type = long.class, complexType = COMPLEXT_LIST,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP),
        @Field(propName = "test_array", seriaName = "test_array", type = String.class,
                complexType = COMPLEXT_ARRAY,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP
        ),
}, maxPoolCount = 10)
public interface StudentBind extends IDataMediator, ISelectable, DataPools.Poolable {

        Property PROP_selected = SharedProperties.get("boolean", "selected", 0);
        Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
        Property PROP_test_object = SharedProperties.get("java.lang.Object", "test_object", 0);
        Property PROP_test_Format = SharedProperties.get("java.lang.Double", "test_Format", 0);
        Property PROP_test_int = SharedProperties.get("int", "test_int", 0);
        Property PROP_test_list = SharedProperties.get("long", "test_list", 2);
        Property PROP_test_array = SharedProperties.get("java.lang.String", "test_array", 1);

        StudentBind setName(String name1);

        String getName();

        StudentBind setTest_object(Object test_object1);

        Object getTest_object();

        StudentBind setTest_Format(Double test_Format1);

        Double getTest_Format();

        StudentBind setTest_int(int test_int1);

        int getTest_int();

        StudentBind setTest_list(List<Long> test_list1);

        List<Long> getTest_list();

        ListPropertyEditor<? extends StudentBind, Long> beginTest_listEditor();

        StudentBind setTest_array(String[] test_array1);

        String[] getTest_array();/*
================== start super methods =============== */
}
