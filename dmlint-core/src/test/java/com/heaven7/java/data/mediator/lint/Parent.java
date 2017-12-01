package com.heaven7.java.data.mediator.lint;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

@Fields({
        @Field(propName = "text1")
})
public interface Parent extends DataPools.Poolable {

    Property PROP_text1 = SharedProperties.get("java.lang.String", "text1", 0);

    @Fields({
            @Field(propName = "text2")
    })
    interface Child extends Parent{

        Property PROP_text2 = SharedProperties.get("java.lang.String", "text2", 0);

        Child setText2(String text21);
        String getText2();

        Child setText1(String text11);
    }
    @Fields({
            @Field(propName = "text3")
    })
    interface Child2  extends Child{
        Property PROP_text3 = SharedProperties.get("java.lang.String", "text3", 0);
        Child2 setText3(String text31);
        String getText3();

        Child2 setText2(String text21);
        Child2 setText1(String text11);
    }

    Parent setText1(String text11);
    String getText1();
}
