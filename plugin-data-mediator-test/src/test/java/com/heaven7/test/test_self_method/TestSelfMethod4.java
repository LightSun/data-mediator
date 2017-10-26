package com.heaven7.test.test_self_method;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields({
        @Field(propName = "test_self4_1")
})
public interface TestSelfMethod4 extends TestSelfMethod3{
    Property PROP_test_self4_1 = SharedProperties.get("java.lang.String", "test_self4_1", 0);

    String getTest_self4_1();

    TestSelfMethod4 setTest_self4_1(String test_self4_11);
}
