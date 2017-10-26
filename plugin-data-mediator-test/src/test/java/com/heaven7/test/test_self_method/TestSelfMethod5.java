package com.heaven7.test.test_self_method;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImplMethod;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.test.TestUtil3;

/**
 * test 'generation-skipping'(隔代遗传) for @ImplMethod
 * Created by heaven7 on 2017/10/26.
 */
@Fields({
        @Field(propName = "test_self5_1")
})
public interface TestSelfMethod5 extends TestSelfMethod4{

    Property PROP_test_self5_1 = SharedProperties.get("java.lang.String", "test_self5_1", 0);

    @ImplMethod(value = "getStudentId3", from = TestUtil3.class)
    String getStudentId5(Student stu, int key);

    String getTest_self5_1();

    TestSelfMethod5 setTest_self5_1(String test_self5_11);
}
