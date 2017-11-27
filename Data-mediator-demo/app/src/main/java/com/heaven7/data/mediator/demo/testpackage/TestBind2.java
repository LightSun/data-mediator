package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.*;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields(value = {
        @Field(propName = "name", seriaName = "xxx1", type = Integer.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name2", seriaName = "xxx2", type = Integer.class,
                complexType = COMPLEX_ARRAY, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name3", seriaName = "xxx3", type = Integer.class,
                complexType = COMPLEX_LIST, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "data", seriaName = "result", type = ResultData.class, flags = FLAGS_ALL_SCOPES),
})
public interface TestBind2 extends Parcelable , IDataMediator, DataPools.Poolable {

    Property PROP_name = SharedProperties.get("java.lang.Integer", "name", 0);
    Property PROP_name2 = SharedProperties.get("java.lang.Integer", "name2", 1);
    Property PROP_name3 = SharedProperties.get("java.lang.Integer", "name3", 2);
    Property PROP_data = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.ResultData", "data", 0);

    TestBind2 setName(Integer name1);

    Integer getName();

    TestBind2 setName2(Integer[] name21);

    Integer[] getName2();

    TestBind2 setName3(List<Integer> name31);

    List<Integer> getName3();

    ListPropertyEditor<? extends TestBind2, Integer> beginName3Editor();

    TestBind2 setData(ResultData data1);

    ResultData getData();/*
================== start methods from super properties ===============
======================================================================= */
}
