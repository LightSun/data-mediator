package com.heaven7.data.mediator.demo.module;

import com.heaven7.data.mediator.demo.activity.TestSelfMethodWithImplInterface;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImplMethod;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * test self method /impl interface
 */
@Fields({
        @Field(propName = "text")
})
public interface TestSelfMethod extends TestSelfMethodWithImplInterface.TextDelegate, DataPools.Poolable {

    Property PROP_text = SharedProperties.get("java.lang.String", "text", 0);

    @ImplMethod(from = HelpUtil.class)
    void changeText(String text);

    TestSelfMethod setText(String text1);

    String getText();/*
================== start methods from super properties ===============
======================================================================= */

    class HelpUtil {
        //compare to  ' void changeText(String text);' , just add a module param at the first.
        public static void changeText(TestSelfMethod module, String text) {
            //just mock text change.
            //module can be real data or data proxy, if is proxy it will auto dispatch text change event.
            module.setText(text);
        }

    }
}