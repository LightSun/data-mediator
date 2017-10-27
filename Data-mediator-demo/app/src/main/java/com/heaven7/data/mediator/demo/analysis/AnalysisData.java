package com.heaven7.data.mediator.demo.analysis;

import com.heaven7.data.mediator.demo.module.FlowItem;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ICopyable;
import com.heaven7.java.data.mediator.IResetable;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

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
public interface AnalysisData extends ICopyable, IResetable, DataPools.Poolable {

    Property PROP_occurTime = SharedProperties.get("long", "occurTime", 0);
    Property PROP_enterTime = SharedProperties.get("long", "enterTime", 0);
    Property PROP_exitTime = SharedProperties.get("long", "exitTime", 0);
    Property PROP_eventType = SharedProperties.get("java.lang.String", "eventType", 0);
    Property PROP_tabIndex = SharedProperties.get("int", "tabIndex", 0);
    Property PROP_itemIndex = SharedProperties.get("int", "itemIndex", 0);
    Property PROP_item = SharedProperties.get("com.heaven7.data.mediator.demo.module.FlowItem", "item", 0);
    Property PROP_apiVersion = SharedProperties.get("java.lang.String", "apiVersion", 0);
    Property PROP_net = SharedProperties.get("java.lang.String", "net", 0);

    AnalysisData setOccurTime(long occurTime1);

    long getOccurTime();

    AnalysisData setEnterTime(long enterTime1);

    long getEnterTime();

    AnalysisData setExitTime(long exitTime1);

    long getExitTime();

    AnalysisData setEventType(String eventType1);

    String getEventType();

    AnalysisData setTabIndex(int tabIndex1);

    int getTabIndex();

    AnalysisData setItemIndex(int itemIndex1);

    int getItemIndex();

    AnalysisData setItem(FlowItem item1);

    FlowItem getItem();

    AnalysisData setApiVersion(String apiVersion1);

    String getApiVersion();

    AnalysisData setNet(String net1);

    String getNet();/*
================== start super methods =============== */
}
