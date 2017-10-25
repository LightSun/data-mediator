package com.heaven7.data.mediator.demo.analysis;

import com.heaven7.data.mediator.demo.module.FlowItem;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ICopyable;
import com.heaven7.java.data.mediator.IResetable;

import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_RESET;
/**
 * 数据分析实体
 * Created by heaven7 on 2017/10/6.
 */
@Fields(value = {
        @Field(propName = "occurTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "enterTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "exitTime", type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "eventType",  flags = FLAGS_ALL_SCOPES),

        @Field(propName = "tabIndex", type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "itemIndex", type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "item", type = FlowItem.class, flags = FLAGS_ALL_SCOPES),

        @Field(propName = "apiVersion", flags = FLAGS_ALL_SCOPES & ~FLAG_RESET),
        @Field(propName = "net", flags = FLAGS_ALL_SCOPES & ~FLAG_RESET),
},maxPoolCount = 5)
public interface AnalysisData extends ICopyable, IResetable{
}
