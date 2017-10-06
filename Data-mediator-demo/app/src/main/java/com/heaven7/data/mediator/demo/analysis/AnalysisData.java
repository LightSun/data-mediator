package com.heaven7.data.mediator.demo.analysis;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ICopyable;
import com.heaven7.java.data.mediator.IResetable;

import static com.heaven7.java.data.mediator.FieldFlags.*;
/**
 * 数据分析实体
 * Created by heaven7 on 2017/10/6.
 */
@Fields({
        @Field(propName = "occurTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "enterTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "exitTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "positionInfo", type = String.class,  flags = FLAGS_ALL_SCOPES),
        @Field(propName = "eventType",  flags = FLAGS_ALL_SCOPES),

        @Field(propName = "apiVersion", flags = FLAGS_ALL_SCOPES & ~FLAG_RESET),
        @Field(propName = "net", flags = FLAGS_ALL_SCOPES & ~FLAG_RESET),
})
public interface AnalysisData extends ICopyable<AnalysisData>, IResetable{
}
