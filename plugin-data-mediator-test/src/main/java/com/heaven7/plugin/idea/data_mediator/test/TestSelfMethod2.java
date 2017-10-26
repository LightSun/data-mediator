package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.util.TestUtil;
import com.heaven7.plugin.idea.data_mediator.test.util.TestUtil2;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields(value = {
        @Field(propName = "id", type = Student.class),
        @Field(propName = "name" ),
})
@ImplClass(TestUtil2.class)
public interface TestSelfMethod2 extends TestSelfMethod1{

    Property PROP_id = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.Student", "id", 0);
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);

    Student getId();
    TestSelfMethod2 setId(Student id1);

    String getName();
    TestSelfMethod2 setName(String name1);

    @ImplMethod(value = "parseStudent", from = TestUtil.class)
    void parseStudent2(Student stu, int key);

    @ImplMethod()
    int getStudentId2(Student stu, int key);
}
