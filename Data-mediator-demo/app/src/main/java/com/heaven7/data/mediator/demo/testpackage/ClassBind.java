package com.heaven7.data.mediator.demo.testpackage;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_ARRAY;
import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_LIST;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_ALL_SCOPES;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_COPY;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_RESET;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_SNAP;

/**
 * Created by heaven7 on 2017/9/5 0005.
 */

//only ClassBind extends ICopyable.  FLAG_COPY can used.

@Fields({
      @Field(propName = "student", seriaName = "class_1", type = TestBind.class, flags = FLAGS_ALL_SCOPES),
      @Field(propName = "student2", seriaName = "class_2", type = TestBind.class,
              complexType = COMPLEXT_LIST, flags = FLAG_COPY | FLAG_RESET),
      @Field(propName = "student3", seriaName = "class_3", type = TestBind.class,
              complexType = COMPLEXT_ARRAY, flags = FLAG_RESET | FLAG_SNAP),
      @Field(propName = "student4", seriaName = "class_4", type = TestBind.class)
})
public interface ClassBind extends TestBind2 ,IDataMediator{ //here不能多继承，

}

