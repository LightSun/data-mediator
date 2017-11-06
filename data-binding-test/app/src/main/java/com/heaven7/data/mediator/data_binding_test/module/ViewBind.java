package com.heaven7.data.mediator.data_binding_test.module;

import android.graphics.drawable.Drawable;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/9/24.
 */
@Fields(value = {
        @Field(propName = "enable" , type = boolean.class),
        @Field(propName = "backgroundRes" , type = int.class),
        @Field(propName = "backgroundColor" , type = int.class),
        @Field(propName = "background" , type = Drawable.class),
}, generateJsonAdapter = false)
public interface ViewBind extends DataPools.Poolable {

    Property PROP_enable = SharedProperties.get("boolean", "enable", 0);
    Property PROP_backgroundRes = SharedProperties.get("int", "backgroundRes", 0);
    Property PROP_backgroundColor = SharedProperties.get("int", "backgroundColor", 0);
    Property PROP_background = SharedProperties.get("android.graphics.drawable.Drawable", "background", 0);

    ViewBind setEnable(boolean enable1);

    boolean isEnable();

    ViewBind setBackgroundRes(int backgroundRes1);

    int getBackgroundRes();

    ViewBind setBackgroundColor(int backgroundColor1);

    int getBackgroundColor();

    ViewBind setBackground(Drawable background1);

    Drawable getBackground();/*
================== start methods from super properties ===============
======================================================================= */
}
