package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.data.mediator.demo.ResultData;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ISnapable;
import com.heaven7.java.data.mediator.processor.FieldData;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields({
        @Field(propName = "name", seriaName = "xxx1", type = Integer.class),
        @Field(propName = "name2", seriaName = "xxx2", type = Integer.class, complexType = FieldData.COMPLEXT_ARRAY),
        @Field(propName = "name3", seriaName = "xxx3", type = Integer.class, complexType = FieldData.COMPLEXT_LIST),
        @Field(propName = "data", seriaName = "result", type = ResultData.class, flags = FieldData.FLAG_PARCEABLE),
})
public interface TestBind2 extends Parcelable {
}
