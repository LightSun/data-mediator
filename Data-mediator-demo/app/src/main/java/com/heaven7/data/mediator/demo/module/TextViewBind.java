package com.heaven7.data.mediator.demo.module;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_MAIN_SCOPES;

/**
 * Created by heaven7 on 2017/9/24.
 */
@Fields({
        @Field(propName = "text" , type = String.class, flags = FLAGS_MAIN_SCOPES),
        @Field(propName = "textRes" , type = int.class, flags = FLAGS_MAIN_SCOPES),

        @Field(propName = "textColor" , type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "textColorRes" , type = int.class, flags = FLAGS_ALL_SCOPES),

        @Field(propName = "textSize" , type = float.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "textSizeRes" , type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "textSizeDp" , type = float.class, flags = FLAGS_ALL_SCOPES),
})
public interface TextViewBind extends Parcelable{
}
