package com.heaven7.data.mediator.data_binding_test.module;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/11/7.
 */
@Fields({
        @Field(propName = "imageUrl"),
        @Field(propName = "imageBitmap", type = Bitmap.class),
        @Field(propName = "imageDrawable", type = Drawable.class),
        @Field(propName = "imageRes", type = int.class),
})
public interface ImageViewBind extends DataPools.Poolable {
    Property PROP_imageUrl = SharedProperties.get("java.lang.String", "imageUrl", 0);
    Property PROP_imageBitmap = SharedProperties.get("android.graphics.Bitmap", "imageBitmap", 0);
    Property PROP_imageDrawable = SharedProperties.get("android.graphics.drawable.Drawable", "imageDrawable", 0);
    Property PROP_imageRes = SharedProperties.get("int", "imageRes", 0);

    ImageViewBind setImageUrl(String imageUrl1);

    String getImageUrl();

    ImageViewBind setImageBitmap(Bitmap imageBitmap1);

    Bitmap getImageBitmap();

    ImageViewBind setImageDrawable(Drawable imageDrawable1);

    Drawable getImageDrawable();

    ImageViewBind setImageRes(int imageRes1);

    int getImageRes();/*
================== start super methods =============== */
}
