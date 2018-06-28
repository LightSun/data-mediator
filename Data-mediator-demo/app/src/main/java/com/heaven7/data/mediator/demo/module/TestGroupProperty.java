package com.heaven7.data.mediator.demo.module;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.GroupDesc;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2018/6/28 0028.
 */
@Fields(
        value = {
                @Field(propName = "state", type = int.class),
                @Field(propName = "state2", type = boolean.class),
        }
        , groups = {
                @GroupDesc(prop = "state", focusVal = 1, oppositeVal = 0),
                @GroupDesc(prop = "state2", focusVal = 1, oppositeVal = 0),
        }
)
public interface TestGroupProperty extends DataPools.Poolable {

    Property PROP_state = SharedProperties.get(int.class.getName(), "state", 0);
    Property PROP_state2 = SharedProperties.get(boolean.class.getName(), "state2", 0);

    TestGroupProperty setState(int state1);

    int getState();

    TestGroupProperty setState2(boolean state21);

    boolean isState2();
}
