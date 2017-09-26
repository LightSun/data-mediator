package com.heaven7.data.mediator.demo.module;

import com.heaven7.data.mediator.demo.testpackage.Student;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_LIST;
/**
 * Created by heaven7 on 2017/9/26 0026.
 */

@Fields({
        @Field(propName = "students", type = Student.class, complexType = COMPLEXT_LIST)
})
public interface RecyclerListBind {
}
