package com.heaven7.data.mediator.demo.module;

import android.graphics.drawable.Drawable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;

/**
 * Created by heaven7 on 2017/9/24.
 */
@Fields({
        @Field(propName = "enable" , type = boolean.class),
        @Field(propName = "backgroundRes" , type = int.class),
        @Field(propName = "backgroundColor" , type = int.class),
        @Field(propName = "background" , type = Drawable.class),
})
public interface ViewBind {
}
