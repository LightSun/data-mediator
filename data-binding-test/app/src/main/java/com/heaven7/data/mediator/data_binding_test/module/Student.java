package com.heaven7.data.mediator.data_binding_test.module;

import android.os.Parcelable;

import com.heaven7.adapter.ISelectable;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.io.Serializable;

/**
 * Created by heaven7 on 2017/9/12 0012.
 */

@Fields({
        @Field(propName = "age" , type = int.class),
        @Field(propName = "name" ),
        @Field(propName = "id" , type = long.class),
})
public interface Student extends Serializable, Parcelable, ISelectable, DataPools.Poolable {

    Property PROP_selected = SharedProperties.get(boolean.class.getName(), "selected", 0);
    Property PROP_age = SharedProperties.get(int.class.getName(), "age", 0);
    Property PROP_name = SharedProperties.get(String.class.getName(), "name", 0);
    Property PROP_id = SharedProperties.get(long.class.getName(), "id", 0);

    Student setAge(int age1);

    int getAge();

    Student setName(String name1);

    String getName();

    Student setId(long id1);

    long getId();
}
