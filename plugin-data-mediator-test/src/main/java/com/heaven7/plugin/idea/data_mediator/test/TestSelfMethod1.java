package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.util.TestUtil;

/**
 * Created by heaven7 on 2017/10/26.
 */
@Fields({
        @Field(propName = "test_self1"),
        @Field(propName = "test_self2", type = int.class)
})
@ImplClass(TestUtil.class)
public interface TestSelfMethod1 extends DataPools.Poolable {

    //define a constant field. add annotation @Keep for not effect by idea-plugin(Data-mediator generator)
    @Keep
    int STATE_OK = 1;
    Property PROP_test_self1 = SharedProperties.get(String.class.getName(), "test_self1", 0);
    Property PROP_test_self2 = SharedProperties.get(int.class.getName(), "test_self2", 0);

    @ImplMethod("getStudentId")
    int getId(Student stu, int key);

    //not assigned method name of ImplClass. so use the same name.
    @ImplMethod
    void parseStudent(Student stu, int key);

    TestSelfMethod1 setTest_self1(String test_self11);

    String getTest_self1();

    TestSelfMethod1 setTest_self2(int test_self21);

    int getTest_self2();
}
