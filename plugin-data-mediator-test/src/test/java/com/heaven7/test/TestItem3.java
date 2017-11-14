package com.heaven7.test;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/10/25.
 */
@Fields({
        @Field(propName = "testItem3")
})
public interface TestItem3 extends TestItem2{
    Property PROP_testItem3 = SharedProperties.get("java.lang.String", "testItem3", 0);

    String getTestItem3();

    TestItem3 setTestItem3(String testItem31);

}
