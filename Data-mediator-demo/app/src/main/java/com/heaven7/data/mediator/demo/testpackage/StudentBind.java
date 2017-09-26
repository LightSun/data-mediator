package com.heaven7.data.mediator.demo.testpackage;

import com.heaven7.adapter.ISelectable;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IDataMediator;

import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_ARRAY;
import static com.heaven7.java.data.mediator.FieldFlags.COMPLEXT_LIST;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_COPY;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_EXPOSE_DEFAULT;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_EXPOSE_SERIALIZE_FALSE;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_RESET;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_SHARE;
import static com.heaven7.java.data.mediator.FieldFlags.FLAG_SNAP;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@Fields({
        @Field(propName = "name", seriaName = "heaven7", type = String.class),
        @Field(propName = "test_object", seriaName = "test_object",
                flags = FLAG_EXPOSE_DEFAULT | FLAG_EXPOSE_SERIALIZE_FALSE, type = Object.class),
        @Field(propName = "test_Format", seriaName = "test_Format", flags = 1, type = Double.class),
        @Field(propName = "test_int", seriaName = "test_int", type = int.class,
                flags = FLAG_EXPOSE_DEFAULT | FLAG_COPY | FLAG_RESET),
        @Field(propName = "test_list", seriaName = "test_list", type = long.class, complexType = COMPLEXT_LIST,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP),
        @Field(propName = "test_array", seriaName = "test_array", type = String.class,
                complexType = COMPLEXT_ARRAY,
                flags = FLAG_RESET | FLAG_SHARE | FLAG_SNAP
        ),
})
public interface StudentBind extends IDataMediator, ISelectable{
}
