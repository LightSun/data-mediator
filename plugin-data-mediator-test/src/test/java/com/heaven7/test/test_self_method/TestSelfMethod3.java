package com.heaven7.test.test_self_method;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.plugin.idea.data_mediator.test.TestSelfMethod2;
import com.heaven7.test.TestUtil3;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields(value = {
        @Field(propName = "test_1", type = boolean.class),
        @Field(propName = "test_2" ),
})
@ImplClass(TestUtil3.class)
public interface TestSelfMethod3 extends TestSelfMethod2 {

    Property PROP_test_1 = SharedProperties.get("boolean", "test_1", 0);
    Property PROP_test_2 = SharedProperties.get("java.lang.String", "test_2", 0);

    @ImplMethod
    String getStudentId3(Student stu, int key);

    boolean isTest_1();

    TestSelfMethod3 setTest_1(boolean test_11);

    String getTest_2();

    TestSelfMethod3 setTest_2(String test_21);
}
