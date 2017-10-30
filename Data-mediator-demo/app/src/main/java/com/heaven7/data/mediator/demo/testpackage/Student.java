package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.adapter.ISelectable;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.io.Serializable;

import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;
/**
 * Created by heaven7 on 2017/9/12 0012.
 */

@Fields({
        @Field(propName = "age" , type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name" , type = String.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "id" , type = long.class, flags = FLAGS_ALL_SCOPES),
})
public interface Student extends Serializable, Parcelable, ISelectable, DataPools.Poolable {

    Property PROP_selected = SharedProperties.get("boolean", "selected", 0);
    Property PROP_age = SharedProperties.get("int", "age", 0);
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
    Property PROP_id = SharedProperties.get("long", "id", 0);

    Student setAge(int age1);

    int getAge();

    Student setName(String name1);

    String getName();

    Student setId(long id1);

    long getId();/*
================== start methods from super properties ===============
======================================================================= */
}
