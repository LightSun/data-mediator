package com.heaven7.data.mediator.demo.module;

import android.os.Parcelable;

import com.heaven7.data.mediator.demo.activity.TestSelfMethodWithImplInterface;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class Outter {

    @Fields({
            @Field(propName = "text3")
    })
    public interface TestSelfMethod3 extends TestSelfMethodWithImplInterface.TestSelfMethod, Parcelable{

        Property PROP_text3 = SharedProperties.get("java.lang.String", "text3", 0);

        TestSelfMethod3 setText3(String text31);

        String getText3();/*
================== start methods from super properties ===============
======================================================================= */

        TestSelfMethod3 setText(String text1);
    }
}
