package com.heaven7.data.mediator.demo;

import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ICopyable;

import java.text.Format;

import static com.heaven7.java.data.mediator.processor.FieldData.COMPLEXT_ARRAY;
import static com.heaven7.java.data.mediator.processor.FieldData.COMPLEXT_LIST;
import static com.heaven7.java.data.mediator.processor.FieldData.FLAG_SERIALIZABLE;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
@Fields({
     @Field(propName = "name", seriaName = "heaven7",flags = FLAG_SERIALIZABLE,type = String.class),
     @Field(propName = "test_object", seriaName = "test_object",flags = 1,type = Object.class),
     @Field(propName = "test_Format", seriaName = "test_Format",flags = 1,type = Format.class),
     @Field(propName = "test_int", seriaName = "test_int",flags = 1,type = int.class),
     @Field(propName = "test_list", seriaName = "test_list",flags = 1,type = long.class, complexType = COMPLEXT_LIST),
     @Field(propName = "test_array", seriaName = "test_array",flags = 1,type = String.class, complexType = COMPLEXT_ARRAY),
})
public interface StudentBind extends ICopyable{
}
