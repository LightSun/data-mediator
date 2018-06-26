package com.heaven7.java.data.mediator.test.gdm;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/*@Fields(
        value = {
                @Field(propName = "state", type = int.class)
        },
        generateJsonAdapter = false
)*/
public interface TestState extends DataPools.Poolable {
    Property PROP_state = SharedProperties.get(int.class.getName(), "state", 0);

    TestState setState(int state1);

    int getState();
}