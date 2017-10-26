package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImplClass;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/10/25.
 */
@Fields({
        @Field(propName = "testItem100", type = FlowItem.class)
})
@ImplClass(Student.class)
public interface TestItem100 extends TestItem{

    Property PROP_testItem100 = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.FlowItem", "testItem100", 0);

    FlowItem getTestItem100();

    TestItem100 setTestItem100(FlowItem testItem1001);
}
