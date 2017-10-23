package com.heaven7.plugin.idea.data_mediator.test;


import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Fields;
import java.lang.String;

@Fields({
        @Field(propName = "id", type = Student.class,  since = 1.2, until = 2.0),
        @Field(propName = "name" ),
        @Field(propName = "desc" , type = String.class, complexType = FieldFlags.COMPLEX_LIST),
        @Field(propName = "selected", type = boolean.class),
        @Field(propName = "xxx1", type = int.class),
})
public interface FlowItem {
}
