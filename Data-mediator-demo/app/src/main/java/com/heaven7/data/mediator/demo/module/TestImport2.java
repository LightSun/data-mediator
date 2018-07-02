package com.heaven7.data.mediator.demo.module;

import com.heaven7.java.data.mediator.FamilyDesc;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImportDesc;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import heaven7.test_compiler.ConnectorImpl;
import heaven7.test_compiler.TestImport;

/**
 * Created by heaven7 on 2018/6/30 0030.
 */
@Fields(
        value = {
                @Field(propName = "state2", type = int.class),
                @Field(propName = "enable2", type = boolean.class),
                @Field(propName = "text2")
        }
        , families = {
        @FamilyDesc(master = {"state2"}, slave = {"enable2", "text2"}, connect = ConnectorImpl.class)
})
@ImportDesc(names = {"context2"}, classes = {ConnectorImpl.class})
public interface TestImport2 extends TestImport {

        Property PROP_state2 = SharedProperties.get(int.class.getName(), "state2", 0);
        Property PROP_enable2 = SharedProperties.get(boolean.class.getName(), "enable2", 0);
        Property PROP_text2 = SharedProperties.get(String.class.getName(), "text2", 0);

        TestImport2 setState2(int state21);

        int getState2();

        TestImport2 setEnable2(boolean enable21);

        boolean isEnable2();

        TestImport2 setText2(String text21);

        String getText2();/*
================== start methods from super properties ===============
======================================================================= */

        TestImport2 setState(int state1);

        TestImport2 setEnable(boolean enable1);

        TestImport2 setText(String text1);
}
