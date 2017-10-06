package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Fields;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields(value = {
        @Field(propName = "name", seriaName = "heaven7", type = String.class),
        @Field(propName = "data", seriaName = "result", type = ResultData.class),
        @Field(propName = "cityData", type = ResultData.class, complexType = FieldFlags.COMPLEX_SPARSE_ARRAY),
        @Field(propName = "cityData2", type = ResultData.class, complexType = FieldFlags.COMPLEX_SPARSE_ARRAY),
}, maxPoolCount = 100)
public interface TestBind extends Parcelable{
}
