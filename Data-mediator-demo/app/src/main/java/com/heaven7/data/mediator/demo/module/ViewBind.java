package com.heaven7.data.mediator.demo.module;

import android.graphics.drawable.Drawable;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/9/24.
 */
@Fields({
        @Field(propName = "enable" , type = boolean.class),
        @Field(propName = "backgroundRes" , type = int.class),
        @Field(propName = "backgroundColor" , type = int.class),
        @Field(propName = "background" , type = Drawable.class),
})
public interface ViewBind extends DataPools.Poolable {
    Property PROP_enable = SharedProperties.get("boolean", "enable", 0);
    Property PROP_backgroundRes = SharedProperties.get("int", "backgroundRes", 0);
    Property PROP_backgroundColor = SharedProperties.get("int", "backgroundColor", 0);
    Property PROP_background = SharedProperties.get("android.graphics.drawable.Drawable", "background", 0);

    boolean isEnable();

    ViewBind setEnable(boolean enable1);

    int getBackgroundRes();

    ViewBind setBackgroundRes(int backgroundRes1);

    int getBackgroundColor();

    ViewBind setBackgroundColor(int backgroundColor1);

    Drawable getBackground();

    ViewBind setBackground(Drawable background1);
}
