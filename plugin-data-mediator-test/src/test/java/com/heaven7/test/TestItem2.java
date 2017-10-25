package com.heaven7.test;


import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;
import com.heaven7.plugin.idea.data_mediator.test.FlowItem;
import com.heaven7.plugin.idea.data_mediator.test.Student;
import com.heaven7.plugin.idea.data_mediator.test.TestParcelableData;

import java.util.List;

@Fields(value = {
        @Field(propName = "testItem2_1", type = Student.class,  since = 1.2, until = 2.0),
        @Field(propName = "testItem2_2" ),
        @Field(propName = "testItem2_3" , complexType = FieldFlags.COMPLEX_LIST),
        @Field(propName = "testItem2_4", type = boolean.class),
        @Field(propName = "testItem2_5", type = int.class),
        @Field(propName = "testItem2_6", type = TestParcelableData.class),
})
public interface TestItem2 extends FlowItem{

    Property PROP_testItem2_1 = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.Student", "testItem2_1", 0);
    Property PROP_testItem2_2 = SharedProperties.get("java.lang.String", "testItem2_2", 0);
    Property PROP_testItem2_3 = SharedProperties.get("java.lang.String", "testItem2_3", 2);
    Property PROP_testItem2_4 = SharedProperties.get("boolean", "testItem2_4", 0);
    Property PROP_testItem2_5 = SharedProperties.get("int", "testItem2_5", 0);
    Property PROP_testItem2_6 = SharedProperties.get("com.heaven7.plugin.idea.data_mediator.test.TestParcelableData", "testItem2_6", 0);

    Student getTestItem2_1();

    TestItem2 setTestItem2_1(Student testItem2_11);

    String getTestItem2_2();

    TestItem2 setTestItem2_2(String testItem2_21);

    List<String> getTestItem2_3();

    TestItem2 setTestItem2_3(List<String> testItem2_31);

    ListPropertyEditor<? extends TestItem2, String> beginTestItem2_3Editor();

    boolean isTestItem2_4();

    TestItem2 setTestItem2_4(boolean testItem2_41);

    int getTestItem2_5();

    TestItem2 setTestItem2_5(int testItem2_51);

    TestParcelableData getTestItem2_6();

    TestItem2 setTestItem2_6(TestParcelableData testItem2_61);
}
