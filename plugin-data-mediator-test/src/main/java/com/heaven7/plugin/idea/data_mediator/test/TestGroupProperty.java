package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2018/6/27.
 */
@Fields(value = {
        @Field(propName = "state", type = int.class)
}, groups = {
        @GroupDesc(prop = "state", focusVal = 1, oppositeVal = -1)
})
public interface TestGroupProperty extends DataPools.Poolable {
    Property PROP_state = SharedProperties.get("int", "state", 0);

    TestGroupProperty setState(int state1);

    int getState();/*
================== start methods from super properties ===============
======================================================================= */
}
