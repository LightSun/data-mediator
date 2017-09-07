package com.heaven7.data.mediator.demo.testpackage;

import com.heaven7.data.mediator.demo.ResultData;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Fields({
        @Field(propName = "name", seriaName = "heaven7", type = String.class),
        @Field(propName = "data", seriaName = "result", type = ResultData.class),
})
public interface TestBind {
}
