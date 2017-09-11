package com.heaven7.data.mediator.demo;

import com.heaven7.data.mediator.demo.testpackage.TestBind;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import static com.heaven7.data.mediator.demo.FieldFlags.COMPLEXT_ARRAY;
import static com.heaven7.data.mediator.demo.FieldFlags.COMPLEXT_LIST;
import static com.heaven7.data.mediator.demo.FieldFlags.FLAG_COPY;
import static com.heaven7.data.mediator.demo.FieldFlags.FLAG_RESET;
import static com.heaven7.data.mediator.demo.FieldFlags.FLAG_SNAP;

/**
 * Created by heaven7 on 2017/8/29 0029.
 */
/*@Fields({
        @Field(propName = "student", seriaName = "class_1", type = TestBind.class),
        @Field(propName = "student2", seriaName = "class_2", type = TestBind.class,
                complexType = COMPLEXT_LIST, flags = FLAG_COPY | FLAG_RESET),
        @Field(propName = "student3", seriaName = "class_3", type = TestBind.class,
                complexType = COMPLEXT_ARRAY, flags = FLAG_RESET | FLAG_SNAP),
        @Field(propName = "student4", seriaName = "class_4", type = TestBind.class)
})*/
public interface TestInterface2 extends StudentBind {
}
