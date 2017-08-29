package com.heaven7.data.mediator.demo;

import android.annotation.TargetApi;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ICacheable;
import com.heaven7.java.data.mediator.ICopyable;

import java.text.Format;

/**
 * Created by Administrator on 2017/8/28 0028.
 */
@Fields({
     @Field(propName = "name", seriaName = "heaven7",flags = 1,type = String.class),
     @Field(propName = "test_object", seriaName = "test_object",flags = 1,type = Object.class),
     @Field(propName = "test_Format", seriaName = "test_Format",flags = 1,type = Format.class),
     @Field(propName = "test_int", seriaName = "test_int",flags = 1,type = int.class),
     @Field(propName = "test_double", seriaName = "test_double",flags = 1,type = double.class)
})
@TargetApi(14)
public interface StudentBind extends ICopyable, ICacheable{
}
