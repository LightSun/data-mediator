package com.heaven7.data.mediator.demo.module;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

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
public interface TextViewBind extends Parcelable, DataPools.Poolable {

    Property PROP_text = SharedProperties.get(String.class.getName(), "text", 0);
    Property PROP_textRes = SharedProperties.get(int.class.getName(), "textRes", 0);
    Property PROP_textColor = SharedProperties.get(int.class.getName(), "textColor", 0);
    Property PROP_textColorRes = SharedProperties.get(int.class.getName(), "textColorRes", 0);
    Property PROP_textSize = SharedProperties.get(float.class.getName(), "textSize", 0);
    Property PROP_textSizeRes = SharedProperties.get(int.class.getName(), "textSizeRes", 0);
    Property PROP_textSizeDp = SharedProperties.get(float.class.getName(), "textSizeDp", 0);

    TextViewBind setText(String text1);

    String getText();

    TextViewBind setTextRes(int textRes1);

    int getTextRes();

    TextViewBind setTextColor(int textColor1);

    int getTextColor();

    TextViewBind setTextColorRes(int textColorRes1);

    int getTextColorRes();

    TextViewBind setTextSize(float textSize1);

    float getTextSize();

    TextViewBind setTextSizeRes(int textSizeRes1);

    int getTextSizeRes();

    TextViewBind setTextSizeDp(float textSizeDp1);

    float getTextSizeDp();
}
