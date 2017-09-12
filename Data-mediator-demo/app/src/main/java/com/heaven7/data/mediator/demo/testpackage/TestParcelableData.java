package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.data.mediator.demo.ResultData;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import java.io.Serializable;

import static com.heaven7.java.data.mediator.FieldFlags.*;
/**
 * Created by heaven7 on 2017/9/9.
 */

@Fields({
//primitive
        @Field(propName = "test_int", type = int.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_long",  type = long.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_byte",  type = byte.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_short",  type = short.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_float", type = float.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_double", type = double.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_boolean", type = boolean.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_char", type = char.class, flags = FLAG_PARCEABLE),
//box
        @Field(propName = "test_Integer", type = Integer.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Long", type = Long.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Short", type = Short.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Byte", type = Byte.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Float", type = Float.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Double", type = Double.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Boolean", type = Boolean.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_Character", type = Character.class, flags = FLAG_PARCEABLE),
//list
        @Field(propName = "test_int_list", type = int.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_LIST),
        @Field(propName = "test_long_list",  type = long.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_LIST),
        @Field(propName = "test_byte_list",  type = byte.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_LIST),
        @Field(propName = "test_short_list",  type = short.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_LIST),
        @Field(propName = "test_float_list", type = float.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_LIST),
        @Field(propName = "test_double_list", type = double.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_boolean_list", type = boolean.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_char_list", type = char.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),

        @Field(propName = "test_Integer_list", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Long_list", type = Long.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Short_list", type = Short.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Byte_list", type = Byte.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Float_list", type = Float.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Double_list", type = Double.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Boolean_list", type = Boolean.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Character_list", type = Character.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_LIST),
//array
        @Field(propName = "test_int_array", type = int.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_long_array",  type = long.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_byte_array",  type = byte.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_short_array",  type = short.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_short_array2",  type = short.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_float_array", type = float.class, flags = FLAG_PARCEABLE , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_double_array", type = double.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_boolean_array", type = boolean.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_char_array", type = char.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),

        @Field(propName = "test_Integer_array", type = Integer.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Long_array", type = Long.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Short_array", type = Short.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Byte_array", type = Byte.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Float_array", type = Float.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Double_array", type = Double.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Boolean_array", type = Boolean.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Character_array", type = Character.class, flags = FLAG_PARCEABLE,complexType = COMPLEXT_ARRAY),

//String, list , array
        @Field(propName = "test_String", type = String.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_String_array", type = String.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_String_list", type = String.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
//any object
        @Field(propName = "test_ResultData", type = ResultData.class, flags = FLAG_PARCEABLE),
        @Field(propName = "test_ResultData_list", type = ResultData.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_LIST),
        @Field(propName = "test_ResultData_array", type = ResultData.class, flags = FLAG_PARCEABLE, complexType = COMPLEXT_ARRAY),
})
public interface TestParcelableData extends Parcelable, Serializable{
}
