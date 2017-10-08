package com.heaven7.data.mediator.demo.module;

import android.os.Parcelable;

import com.heaven7.adapter.ISelectable;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import static com.heaven7.java.data.mediator.FieldFlags.*;
/**
 * the item of tab
 * Created by heaven7 on 2017/10/8.
 */

@Fields({
        @Field(propName = "id", type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name" , flags = FLAGS_ALL_SCOPES),
        @Field(propName = "desc" , flags = FLAGS_ALL_SCOPES),
})
public interface FlowItem extends Parcelable, ISelectable{
}
