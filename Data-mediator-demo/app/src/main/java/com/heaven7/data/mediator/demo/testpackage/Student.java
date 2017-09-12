package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import java.io.Serializable;
import static com.heaven7.java.data.mediator.FieldFlags.*;
/**
 * Created by heaven7 on 2017/9/12 0012.
 */

@Fields({
        @Field(propName = "age" , type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name" , type = String.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "id" , type = long.class, flags = FLAGS_ALL_SCOPES),
})
public interface Student extends Serializable, Parcelable{
}
