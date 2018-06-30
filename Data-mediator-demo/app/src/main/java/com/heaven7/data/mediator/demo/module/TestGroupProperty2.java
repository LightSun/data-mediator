package com.heaven7.data.mediator.demo.module;

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
                @Field(propName = "state3", type = int.class),
        }
        , groups = {
                @GroupDesc(prop = "state3", focusVal = 1, oppositeVal = 0),
        }
)
public interface TestGroupProperty2 extends TestGroupProperty {

    Property PROP_state3 = SharedProperties.get(int.class.getName(), "state3", 0);

    TestGroupProperty2 setState3(int state31);

    int getState3();/*
================== start methods from super properties ===============
======================================================================= */

    TestGroupProperty2 setState(int state1);

    TestGroupProperty2 setState2(boolean state21);
}
