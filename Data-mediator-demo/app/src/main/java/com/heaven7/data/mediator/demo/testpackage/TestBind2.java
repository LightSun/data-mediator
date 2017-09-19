package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;

import static com.heaven7.java.data.mediator.FieldFlags.*;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields(value = {
        @Field(propName = "name", seriaName = "xxx1", type = Integer.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name2", seriaName = "xxx2", type = Integer.class,
                complexType = COMPLEXT_ARRAY, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "name3", seriaName = "xxx3", type = Integer.class,
                complexType = COMPLEXT_LIST, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "data", seriaName = "result", type = ResultData.class, flags = FLAGS_ALL_SCOPES),
})
public interface TestBind2 extends Parcelable , IDataMediator {
}
