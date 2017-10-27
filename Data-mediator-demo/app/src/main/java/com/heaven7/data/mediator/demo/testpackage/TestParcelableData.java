package com.heaven7.data.mediator.demo.testpackage;

import android.os.Parcelable;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.IResetable;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.SparseArrayPropertyEditor;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.io.Serializable;
import java.util.List;

import static com.heaven7.java.data.mediator.FieldFlags.*;
/**
 * Created by heaven7 on 2017/9/9.
 */

@Fields(value = {
//primitive
        @Field(propName = "test_int", type = int.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_long",  type = long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_byte",  type = byte.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_short",  type = short.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_float", type = float.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_double", type = double.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_boolean", type = boolean.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_char", type = char.class, flags = FLAGS_ALL_SCOPES),
//box
        @Field(propName = "test_Integer", type = Integer.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Long", type = Long.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Short", type = Short.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Byte", type = Byte.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Float", type = Float.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Double", type = Double.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Boolean", type = Boolean.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_Character", type = Character.class, flags = FLAGS_ALL_SCOPES),
//list
        @Field(propName = "test_int_list", type = int.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_LIST),
        @Field(propName = "test_long_list",  type = long.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_LIST),
        @Field(propName = "test_byte_list",  type = byte.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_LIST),
        @Field(propName = "test_short_list",  type = short.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_LIST),
        @Field(propName = "test_float_list", type = float.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_LIST),
        @Field(propName = "test_double_list", type = double.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_boolean_list", type = boolean.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_char_list", type = char.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),

        @Field(propName = "test_Integer_list", type = Integer.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Long_list", type = Long.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Short_list", type = Short.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_Byte_list", type = Byte.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Float_list", type = Float.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Double_list", type = Double.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Boolean_list", type = Boolean.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_LIST),
        @Field(propName = "test_Character_list", type = Character.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_LIST),
//array
        @Field(propName = "test_int_array", type = int.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_long_array",  type = long.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_byte_array",  type = byte.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_short_array",  type = short.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_short_array2",  type = short.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_float_array", type = float.class, flags = FLAGS_ALL_SCOPES , complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_double_array", type = double.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_boolean_array", type = boolean.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_char_array", type = char.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),

        @Field(propName = "test_Integer_array", type = Integer.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Long_array", type = Long.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Short_array", type = Short.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Byte_array", type = Byte.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Float_array", type = Float.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Double_array", type = Double.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Boolean_array", type = Boolean.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_Character_array", type = Character.class, flags = FLAGS_ALL_SCOPES,complexType = COMPLEXT_ARRAY),

//String, list , array
        @Field(propName = "test_String", type = String.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_String_array", type = String.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_ARRAY),
        @Field(propName = "test_String_list", type = String.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
//any object
        @Field(propName = "test_ResultData", type = ResultData.class, flags = FLAGS_ALL_SCOPES),
        @Field(propName = "test_ResultData_list", type = ResultData.class, flags = FLAGS_ALL_SCOPES, complexType = COMPLEXT_LIST),
        @Field(propName = "test_ResultData_array", type = ResultData.class, flags = FLAGS_MAIN_SCOPES, complexType = COMPLEXT_ARRAY),
// sparseArray
        @Field(propName = "test_SparseArray", type = ResultData.class,
                complexType = COMPLEX_SPARSE_ARRAY, flags = FLAGS_ALL_SCOPES),
}, maxPoolCount = 100)
public interface TestParcelableData extends Parcelable, Serializable, IResetable, DataPools.Poolable {

    Property PROP_test_int = SharedProperties.get("int", "test_int", 0);
    Property PROP_test_long = SharedProperties.get("long", "test_long", 0);
    Property PROP_test_byte = SharedProperties.get("byte", "test_byte", 0);
    Property PROP_test_short = SharedProperties.get("short", "test_short", 0);
    Property PROP_test_float = SharedProperties.get("float", "test_float", 0);
    Property PROP_test_double = SharedProperties.get("double", "test_double", 0);
    Property PROP_test_boolean = SharedProperties.get("boolean", "test_boolean", 0);
    Property PROP_test_char = SharedProperties.get("char", "test_char", 0);
    Property PROP_test_Integer = SharedProperties.get("java.lang.Integer", "test_Integer", 0);
    Property PROP_test_Long = SharedProperties.get("java.lang.Long", "test_Long", 0);
    Property PROP_test_Short = SharedProperties.get("java.lang.Short", "test_Short", 0);
    Property PROP_test_Byte = SharedProperties.get("java.lang.Byte", "test_Byte", 0);
    Property PROP_test_Float = SharedProperties.get("java.lang.Float", "test_Float", 0);
    Property PROP_test_Double = SharedProperties.get("java.lang.Double", "test_Double", 0);
    Property PROP_test_Boolean = SharedProperties.get("java.lang.Boolean", "test_Boolean", 0);
    Property PROP_test_Character = SharedProperties.get("java.lang.Character", "test_Character", 0);
    Property PROP_test_int_list = SharedProperties.get("int", "test_int_list", 2);
    Property PROP_test_long_list = SharedProperties.get("long", "test_long_list", 2);
    Property PROP_test_byte_list = SharedProperties.get("byte", "test_byte_list", 2);
    Property PROP_test_short_list = SharedProperties.get("short", "test_short_list", 2);
    Property PROP_test_float_list = SharedProperties.get("float", "test_float_list", 2);
    Property PROP_test_double_list = SharedProperties.get("double", "test_double_list", 2);
    Property PROP_test_boolean_list = SharedProperties.get("boolean", "test_boolean_list", 2);
    Property PROP_test_char_list = SharedProperties.get("char", "test_char_list", 2);
    Property PROP_test_Integer_list = SharedProperties.get("java.lang.Integer", "test_Integer_list", 2);
    Property PROP_test_Long_list = SharedProperties.get("java.lang.Long", "test_Long_list", 2);
    Property PROP_test_Short_list = SharedProperties.get("java.lang.Short", "test_Short_list", 2);
    Property PROP_test_Byte_list = SharedProperties.get("java.lang.Byte", "test_Byte_list", 2);
    Property PROP_test_Float_list = SharedProperties.get("java.lang.Float", "test_Float_list", 2);
    Property PROP_test_Double_list = SharedProperties.get("java.lang.Double", "test_Double_list", 2);
    Property PROP_test_Boolean_list = SharedProperties.get("java.lang.Boolean", "test_Boolean_list", 2);
    Property PROP_test_Character_list = SharedProperties.get("java.lang.Character", "test_Character_list", 2);
    Property PROP_test_int_array = SharedProperties.get("int", "test_int_array", 1);
    Property PROP_test_long_array = SharedProperties.get("long", "test_long_array", 1);
    Property PROP_test_byte_array = SharedProperties.get("byte", "test_byte_array", 1);
    Property PROP_test_short_array = SharedProperties.get("short", "test_short_array", 1);
    Property PROP_test_short_array2 = SharedProperties.get("short", "test_short_array2", 1);
    Property PROP_test_float_array = SharedProperties.get("float", "test_float_array", 1);
    Property PROP_test_double_array = SharedProperties.get("double", "test_double_array", 1);
    Property PROP_test_boolean_array = SharedProperties.get("boolean", "test_boolean_array", 1);
    Property PROP_test_char_array = SharedProperties.get("char", "test_char_array", 1);
    Property PROP_test_Integer_array = SharedProperties.get("java.lang.Integer", "test_Integer_array", 1);
    Property PROP_test_Long_array = SharedProperties.get("java.lang.Long", "test_Long_array", 1);
    Property PROP_test_Short_array = SharedProperties.get("java.lang.Short", "test_Short_array", 1);
    Property PROP_test_Byte_array = SharedProperties.get("java.lang.Byte", "test_Byte_array", 1);
    Property PROP_test_Float_array = SharedProperties.get("java.lang.Float", "test_Float_array", 1);
    Property PROP_test_Double_array = SharedProperties.get("java.lang.Double", "test_Double_array", 1);
    Property PROP_test_Boolean_array = SharedProperties.get("java.lang.Boolean", "test_Boolean_array", 1);
    Property PROP_test_Character_array = SharedProperties.get("java.lang.Character", "test_Character_array", 1);
    Property PROP_test_String = SharedProperties.get("java.lang.String", "test_String", 0);
    Property PROP_test_String_array = SharedProperties.get("java.lang.String", "test_String_array", 1);
    Property PROP_test_String_list = SharedProperties.get("java.lang.String", "test_String_list", 2);
    Property PROP_test_ResultData = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.ResultData", "test_ResultData", 0);
    Property PROP_test_ResultData_list = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.ResultData", "test_ResultData_list", 2);
    Property PROP_test_ResultData_array = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.ResultData", "test_ResultData_array", 1);
    Property PROP_test_SparseArray = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.ResultData", "test_SparseArray", 3);

    TestParcelableData setTest_int(int test_int1);

    int getTest_int();

    TestParcelableData setTest_long(long test_long1);

    long getTest_long();

    TestParcelableData setTest_byte(byte test_byte1);

    byte getTest_byte();

    TestParcelableData setTest_short(short test_short1);

    short getTest_short();

    TestParcelableData setTest_float(float test_float1);

    float getTest_float();

    TestParcelableData setTest_double(double test_double1);

    double getTest_double();

    TestParcelableData setTest_boolean(boolean test_boolean1);

    boolean isTest_boolean();

    TestParcelableData setTest_char(char test_char1);

    char getTest_char();

    TestParcelableData setTest_Integer(Integer test_Integer1);

    Integer getTest_Integer();

    TestParcelableData setTest_Long(Long test_Long1);

    Long getTest_Long();

    TestParcelableData setTest_Short(Short test_Short1);

    Short getTest_Short();

    TestParcelableData setTest_Byte(Byte test_Byte1);

    Byte getTest_Byte();

    TestParcelableData setTest_Float(Float test_Float1);

    Float getTest_Float();

    TestParcelableData setTest_Double(Double test_Double1);

    Double getTest_Double();

    TestParcelableData setTest_Boolean(Boolean test_Boolean1);

    Boolean getTest_Boolean();

    TestParcelableData setTest_Character(Character test_Character1);

    Character getTest_Character();

    TestParcelableData setTest_int_list(List<Integer> test_int_list1);

    List<Integer> getTest_int_list();

    ListPropertyEditor<? extends TestParcelableData, Integer> beginTest_int_listEditor();

    TestParcelableData setTest_long_list(List<Long> test_long_list1);

    List<Long> getTest_long_list();

    ListPropertyEditor<? extends TestParcelableData, Long> beginTest_long_listEditor();

    TestParcelableData setTest_byte_list(List<Byte> test_byte_list1);

    List<Byte> getTest_byte_list();

    ListPropertyEditor<? extends TestParcelableData, Byte> beginTest_byte_listEditor();

    TestParcelableData setTest_short_list(List<Short> test_short_list1);

    List<Short> getTest_short_list();

    ListPropertyEditor<? extends TestParcelableData, Short> beginTest_short_listEditor();

    TestParcelableData setTest_float_list(List<Float> test_float_list1);

    List<Float> getTest_float_list();

    ListPropertyEditor<? extends TestParcelableData, Float> beginTest_float_listEditor();

    TestParcelableData setTest_double_list(List<Double> test_double_list1);

    List<Double> getTest_double_list();

    ListPropertyEditor<? extends TestParcelableData, Double> beginTest_double_listEditor();

    TestParcelableData setTest_boolean_list(List<Boolean> test_boolean_list1);

    List<Boolean> getTest_boolean_list();

    ListPropertyEditor<? extends TestParcelableData, Boolean> beginTest_boolean_listEditor();

    TestParcelableData setTest_char_list(List<Character> test_char_list1);

    List<Character> getTest_char_list();

    ListPropertyEditor<? extends TestParcelableData, Character> beginTest_char_listEditor();

    TestParcelableData setTest_Integer_list(List<Integer> test_Integer_list1);

    List<Integer> getTest_Integer_list();

    ListPropertyEditor<? extends TestParcelableData, Integer> beginTest_Integer_listEditor();

    TestParcelableData setTest_Long_list(List<Long> test_Long_list1);

    List<Long> getTest_Long_list();

    ListPropertyEditor<? extends TestParcelableData, Long> beginTest_Long_listEditor();

    TestParcelableData setTest_Short_list(List<Short> test_Short_list1);

    List<Short> getTest_Short_list();

    ListPropertyEditor<? extends TestParcelableData, Short> beginTest_Short_listEditor();

    TestParcelableData setTest_Byte_list(List<Byte> test_Byte_list1);

    List<Byte> getTest_Byte_list();

    ListPropertyEditor<? extends TestParcelableData, Byte> beginTest_Byte_listEditor();

    TestParcelableData setTest_Float_list(List<Float> test_Float_list1);

    List<Float> getTest_Float_list();

    ListPropertyEditor<? extends TestParcelableData, Float> beginTest_Float_listEditor();

    TestParcelableData setTest_Double_list(List<Double> test_Double_list1);

    List<Double> getTest_Double_list();

    ListPropertyEditor<? extends TestParcelableData, Double> beginTest_Double_listEditor();

    TestParcelableData setTest_Boolean_list(List<Boolean> test_Boolean_list1);

    List<Boolean> getTest_Boolean_list();

    ListPropertyEditor<? extends TestParcelableData, Boolean> beginTest_Boolean_listEditor();

    TestParcelableData setTest_Character_list(List<Character> test_Character_list1);

    List<Character> getTest_Character_list();

    ListPropertyEditor<? extends TestParcelableData, Character> beginTest_Character_listEditor();

    TestParcelableData setTest_int_array(int[] test_int_array1);

    int[] getTest_int_array();

    TestParcelableData setTest_long_array(long[] test_long_array1);

    long[] getTest_long_array();

    TestParcelableData setTest_byte_array(byte[] test_byte_array1);

    byte[] getTest_byte_array();

    TestParcelableData setTest_short_array(short[] test_short_array1);

    short[] getTest_short_array();

    TestParcelableData setTest_short_array2(short[] test_short_array21);

    short[] getTest_short_array2();

    TestParcelableData setTest_float_array(float[] test_float_array1);

    float[] getTest_float_array();

    TestParcelableData setTest_double_array(double[] test_double_array1);

    double[] getTest_double_array();

    TestParcelableData setTest_boolean_array(boolean[] test_boolean_array1);

    boolean[] getTest_boolean_array();

    TestParcelableData setTest_char_array(char[] test_char_array1);

    char[] getTest_char_array();

    TestParcelableData setTest_Integer_array(Integer[] test_Integer_array1);

    Integer[] getTest_Integer_array();

    TestParcelableData setTest_Long_array(Long[] test_Long_array1);

    Long[] getTest_Long_array();

    TestParcelableData setTest_Short_array(Short[] test_Short_array1);

    Short[] getTest_Short_array();

    TestParcelableData setTest_Byte_array(Byte[] test_Byte_array1);

    Byte[] getTest_Byte_array();

    TestParcelableData setTest_Float_array(Float[] test_Float_array1);

    Float[] getTest_Float_array();

    TestParcelableData setTest_Double_array(Double[] test_Double_array1);

    Double[] getTest_Double_array();

    TestParcelableData setTest_Boolean_array(Boolean[] test_Boolean_array1);

    Boolean[] getTest_Boolean_array();

    TestParcelableData setTest_Character_array(Character[] test_Character_array1);

    Character[] getTest_Character_array();

    TestParcelableData setTest_String(String test_String1);

    String getTest_String();

    TestParcelableData setTest_String_array(String[] test_String_array1);

    String[] getTest_String_array();

    TestParcelableData setTest_String_list(List<String> test_String_list1);

    List<String> getTest_String_list();

    ListPropertyEditor<? extends TestParcelableData, String> beginTest_String_listEditor();

    TestParcelableData setTest_ResultData(ResultData test_ResultData1);

    ResultData getTest_ResultData();

    TestParcelableData setTest_ResultData_list(List<ResultData> test_ResultData_list1);

    List<ResultData> getTest_ResultData_list();

    ListPropertyEditor<? extends TestParcelableData, ResultData> beginTest_ResultData_listEditor();

    TestParcelableData setTest_ResultData_array(ResultData[] test_ResultData_array1);

    ResultData[] getTest_ResultData_array();

    TestParcelableData setTest_SparseArray(SparseArray<ResultData> test_SparseArray1);

    SparseArray<ResultData> getTest_SparseArray();

    SparseArrayPropertyEditor<? extends TestParcelableData, ResultData> beginTest_SparseArrayEditor();/*
================== start super methods =============== */
}
