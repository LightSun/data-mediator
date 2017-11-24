package com.heaven7.plugin.idea.data_mediator.test;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void test(TestItem item){

    }
    public static void test2(TestItem item){

    }

    public static class Test{
        private String name;
    }

    public interface IStudent {
    }
    static void checkArgument(boolean condition, String format, Object... args) {
        if (!condition) throw new IllegalArgumentException(String.format(format, args));
    }

    //resolve inner class
    private static Class<?> bestGuessClass(String classNameString) {
        List<String> names = new ArrayList<>();

        // Add the package name, like "java.util.concurrent", or "" for no package.
        int p = 0;
        while (p < classNameString.length() && Character.isLowerCase(classNameString.codePointAt(p))) {
            p = classNameString.indexOf('.', p) + 1;
            checkArgument(p != 0, "couldn't make a guess for %s", classNameString);
        }
        names.add(p != 0 ? classNameString.substring(0, p - 1) : "");

        // Add the class names, like "Map" and "Entry".
        for (String part : classNameString.substring(p).split("\\.", -1)) {
            checkArgument(!part.isEmpty() && Character.isUpperCase(part.codePointAt(0)),
                    "couldn't make a guess for %s", classNameString);
            names.add(part);
        }

        final int size = names.size();
        checkArgument(size >= 2, "couldn't make a guess for %s", classNameString);
        //System.out.println(names);
        StringBuilder sb = new StringBuilder();
        sb.append(names.get(0))
                .append(".")
                .append(names.get(1));
        if(size > 2){
            for(int i = 2 ; i < size; i ++){
                sb.append("$")
                        .append(names.get(i));
            }
        }
        try {
            return Class.forName(sb.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        String str = "com.heaven7.plugin.idea.data_mediator.test.Student.Test";
        System.out.println(str);
       /* try {
            System.out.println(Class.forName(str));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        System.out.println(bestGuessClass(str));
    }

    public static class TestParcelableData_$Impl {

        @SerializedName("test_int")
        private int test_int;

        @Expose(deserialize = false)
        private long test_long;

        @Since(1.1)
        private byte test_byte;

        @Until(1.8)
        private short test_short;

        private float test_float;

        private double test_double;

        private boolean test_boolean;

        private char test_char;

        private Integer test_Integer;

        private Long test_Long;

        private Short test_Short;

        private Byte test_Byte;

        private Float test_Float;

        private Double test_Double;

        private Boolean test_Boolean;

        private Character test_Character;

        private List<Integer> test_int_list;

        private List<Long> test_long_list;

        private List<Byte> test_byte_list;

        private List<Short> test_short_list;

        private List<Float> test_float_list;

        private List<Double> test_double_list;

        private List<Boolean> test_boolean_list;

        private List<Character> test_char_list;

        private List<Integer> test_Integer_list;

        private List<Long> test_Long_list;

        private List<Short> test_Short_list;

        private List<Byte> test_Byte_list;

        private List<Float> test_Float_list;

        private List<Double> test_Double_list;

        private List<Boolean> test_Boolean_list;

        private List<Character> test_Character_list;

        private int[] test_int_array;

        private long[] test_long_array;

        private byte[] test_byte_array;

        private short[] test_short_array;

        private short[] test_short_array2;

        private float[] test_float_array;

        private double[] test_double_array;

        private boolean[] test_boolean_array;

        private char[] test_char_array;

        private Integer[] test_Integer_array;

        private Long[] test_Long_array;

        private Short[] test_Short_array;

        private Byte[] test_Byte_array;

        private Float[] test_Float_array;

        private Double[] test_Double_array;

        private Boolean[] test_Boolean_array;

        private Character[] test_Character_array;

        private String test_String;

        private String[] test_String_array;

        private List<String> test_String_list;

        private Student.Test test_ResultData;

        private List<Student.Test> test_ResultData_list;

        private Student.Test[] test_ResultData_array;

        private SparseArray<Test> test_SparseArray;

    }

    @Fields(value = {@Field(propName = "test_int", seriaName = "test_int", type = int.class),
            @Field(propName = "test_long", type = long.class, flags = FieldFlags.FLAGS_MAIN_SCOPES_2 | FieldFlags.FLAG_EXPOSE_DEFAULT | FieldFlags.FLAG_EXPOSE_DESERIALIZE_FALSE),
            @Field(propName = "test_byte", type = byte.class, since = 1.1),
            @Field(propName = "test_short", type = short.class, until = 1.8),
            @Field(propName = "test_float", type = float.class),
            @Field(propName = "test_double", type = double.class),
            @Field(propName = "test_boolean", type = boolean.class),
            @Field(propName = "test_char", type = char.class),
            @Field(propName = "test_Integer", type = Integer.class),
            @Field(propName = "test_Long", type = Long.class),
            @Field(propName = "test_Short", type = Short.class),
            @Field(propName = "test_Byte", type = Byte.class),
            @Field(propName = "test_Float", type = Float.class),
            @Field(propName = "test_Double", type = Double.class),
            @Field(propName = "test_Boolean", type = Boolean.class),
            @Field(propName = "test_Character", type = Character.class),
            @Field(propName = "test_int_list", type = Integer.class, complexType = 2),
            @Field(propName = "test_long_list", type = Long.class, complexType = 2),
            @Field(propName = "test_byte_list", type = Byte.class, complexType = 2),
            @Field(propName = "test_short_list", type = Short.class, complexType = 2),
            @Field(propName = "test_float_list", type = Float.class, complexType = 2),
            @Field(propName = "test_double_list", type = Double.class, complexType = 2),
            @Field(propName = "test_boolean_list", type = Boolean.class, complexType = 2),
            @Field(propName = "test_char_list", type = Character.class, complexType = 2),
            @Field(propName = "test_Integer_list", type = Integer.class, complexType = 2),
            @Field(propName = "test_Long_list", type = Long.class, complexType = 2),
            @Field(propName = "test_Short_list", type = Short.class, complexType = 2),
            @Field(propName = "test_Byte_list", type = Byte.class, complexType = 2),
            @Field(propName = "test_Float_list", type = Float.class, complexType = 2),
            @Field(propName = "test_Double_list", type = Double.class, complexType = 2),
            @Field(propName = "test_Boolean_list", type = Boolean.class, complexType = 2),
            @Field(propName = "test_Character_list", type = Character.class, complexType = 2),
            @Field(propName = "test_int_array", type = int.class, complexType = 1),
            @Field(propName = "test_long_array", type = long.class, complexType = 1),
            @Field(propName = "test_byte_array", type = byte.class, complexType = 1),
            @Field(propName = "test_short_array", type = short.class, complexType = 1),
            @Field(propName = "test_short_array2", type = short.class, complexType = 1),
            @Field(propName = "test_float_array", type = float.class, complexType = 1),
            @Field(propName = "test_double_array", type = double.class, complexType = 1),
            @Field(propName = "test_boolean_array", type = boolean.class, complexType = 1),
            @Field(propName = "test_char_array", type = char.class, complexType = 1),
            @Field(propName = "test_Integer_array", type = Integer.class, complexType = 1),
            @Field(propName = "test_Long_array", type = Long.class, complexType = 1),
            @Field(propName = "test_Short_array", type = Short.class, complexType = 1),
            @Field(propName = "test_Byte_array", type = Byte.class, complexType = 1),
            @Field(propName = "test_Float_array", type = Float.class, complexType = 1),
            @Field(propName = "test_Double_array", type = Double.class, complexType = 1),
            @Field(propName = "test_Boolean_array", type = Boolean.class, complexType = 1),
            @Field(propName = "test_Character_array", type = Character.class, complexType = 1),
            @Field(propName = "test_String"),
            @Field(propName = "test_String_array", complexType = 1),
            @Field(propName = "test_String_list", complexType = 2),
            @Field(propName = "test_ResultData", type = Test.class),
            @Field(propName = "test_ResultData_list", type = Test.class, complexType = 2),
            @Field(propName = "test_ResultData_array", type = Test.class, complexType = 1),
            @Field(propName = "test_SparseArray", type = Test.class, complexType = 3)
    }, generateJsonAdapter = false)
    public interface ITestParcelableData_$Impl extends DataPools.Poolable {
        Property PROP_test_int = SharedProperties.get(int.class.getName(), "test_int", 0);
        Property PROP_test_long = SharedProperties.get(long.class.getName(), "test_long", 0);
        Property PROP_test_byte = SharedProperties.get(byte.class.getName(), "test_byte", 0);
        Property PROP_test_short = SharedProperties.get(short.class.getName(), "test_short", 0);
        Property PROP_test_float = SharedProperties.get(float.class.getName(), "test_float", 0);
        Property PROP_test_double = SharedProperties.get(double.class.getName(), "test_double", 0);
        Property PROP_test_boolean = SharedProperties.get(boolean.class.getName(), "test_boolean", 0);
        Property PROP_test_char = SharedProperties.get(char.class.getName(), "test_char", 0);
        Property PROP_test_Integer = SharedProperties.get(Integer.class.getName(), "test_Integer", 0);
        Property PROP_test_Long = SharedProperties.get(Long.class.getName(), "test_Long", 0);
        Property PROP_test_Short = SharedProperties.get(Short.class.getName(), "test_Short", 0);
        Property PROP_test_Byte = SharedProperties.get(Byte.class.getName(), "test_Byte", 0);
        Property PROP_test_Float = SharedProperties.get(Float.class.getName(), "test_Float", 0);
        Property PROP_test_Double = SharedProperties.get(Double.class.getName(), "test_Double", 0);
        Property PROP_test_Boolean = SharedProperties.get(Boolean.class.getName(), "test_Boolean", 0);
        Property PROP_test_Character = SharedProperties.get(Character.class.getName(), "test_Character", 0);
        Property PROP_test_int_list = SharedProperties.get(Integer.class.getName(), "test_int_list", 2);
        Property PROP_test_long_list = SharedProperties.get(Long.class.getName(), "test_long_list", 2);
        Property PROP_test_byte_list = SharedProperties.get(Byte.class.getName(), "test_byte_list", 2);
        Property PROP_test_short_list = SharedProperties.get(Short.class.getName(), "test_short_list", 2);
        Property PROP_test_float_list = SharedProperties.get(Float.class.getName(), "test_float_list", 2);
        Property PROP_test_double_list = SharedProperties.get(Double.class.getName(), "test_double_list", 2);
        Property PROP_test_boolean_list = SharedProperties.get(Boolean.class.getName(), "test_boolean_list", 2);
        Property PROP_test_char_list = SharedProperties.get(Character.class.getName(), "test_char_list", 2);
        Property PROP_test_Integer_list = SharedProperties.get(Integer.class.getName(), "test_Integer_list", 2);
        Property PROP_test_Long_list = SharedProperties.get(Long.class.getName(), "test_Long_list", 2);
        Property PROP_test_Short_list = SharedProperties.get(Short.class.getName(), "test_Short_list", 2);
        Property PROP_test_Byte_list = SharedProperties.get(Byte.class.getName(), "test_Byte_list", 2);
        Property PROP_test_Float_list = SharedProperties.get(Float.class.getName(), "test_Float_list", 2);
        Property PROP_test_Double_list = SharedProperties.get(Double.class.getName(), "test_Double_list", 2);
        Property PROP_test_Boolean_list = SharedProperties.get(Boolean.class.getName(), "test_Boolean_list", 2);
        Property PROP_test_Character_list = SharedProperties.get(Character.class.getName(), "test_Character_list", 2);
        Property PROP_test_int_array = SharedProperties.get(int.class.getName(), "test_int_array", 1);
        Property PROP_test_long_array = SharedProperties.get(long.class.getName(), "test_long_array", 1);
        Property PROP_test_byte_array = SharedProperties.get(byte.class.getName(), "test_byte_array", 1);
        Property PROP_test_short_array = SharedProperties.get(short.class.getName(), "test_short_array", 1);
        Property PROP_test_short_array2 = SharedProperties.get(short.class.getName(), "test_short_array2", 1);
        Property PROP_test_float_array = SharedProperties.get(float.class.getName(), "test_float_array", 1);
        Property PROP_test_double_array = SharedProperties.get(double.class.getName(), "test_double_array", 1);
        Property PROP_test_boolean_array = SharedProperties.get(boolean.class.getName(), "test_boolean_array", 1);
        Property PROP_test_char_array = SharedProperties.get(char.class.getName(), "test_char_array", 1);
        Property PROP_test_Integer_array = SharedProperties.get(Integer.class.getName(), "test_Integer_array", 1);
        Property PROP_test_Long_array = SharedProperties.get(Long.class.getName(), "test_Long_array", 1);
        Property PROP_test_Short_array = SharedProperties.get(Short.class.getName(), "test_Short_array", 1);
        Property PROP_test_Byte_array = SharedProperties.get(Byte.class.getName(), "test_Byte_array", 1);
        Property PROP_test_Float_array = SharedProperties.get(Float.class.getName(), "test_Float_array", 1);
        Property PROP_test_Double_array = SharedProperties.get(Double.class.getName(), "test_Double_array", 1);
        Property PROP_test_Boolean_array = SharedProperties.get(Boolean.class.getName(), "test_Boolean_array", 1);
        Property PROP_test_Character_array = SharedProperties.get(Character.class.getName(), "test_Character_array", 1);
        Property PROP_test_String = SharedProperties.get(String.class.getName(), "test_String", 0);
        Property PROP_test_String_array = SharedProperties.get(String.class.getName(), "test_String_array", 1);
        Property PROP_test_String_list = SharedProperties.get(String.class.getName(), "test_String_list", 2);
        Property PROP_test_ResultData = SharedProperties.get(Test.class.getName(), "test_ResultData", 0);
        Property PROP_test_ResultData_list = SharedProperties.get(Test.class.getName(), "test_ResultData_list", 2);
        Property PROP_test_ResultData_array = SharedProperties.get(Test.class.getName(), "test_ResultData_array", 1);
        Property PROP_test_SparseArray = SharedProperties.get(Test.class.getName(), "test_SparseArray", 3);

        ITestParcelableData_$Impl setTest_int(int test_int1);

        int getTest_int();

        ITestParcelableData_$Impl setTest_long(long test_long1);

        long getTest_long();

        ITestParcelableData_$Impl setTest_byte(byte test_byte1);

        byte getTest_byte();

        ITestParcelableData_$Impl setTest_short(short test_short1);

        short getTest_short();

        ITestParcelableData_$Impl setTest_float(float test_float1);

        float getTest_float();

        ITestParcelableData_$Impl setTest_double(double test_double1);

        double getTest_double();

        ITestParcelableData_$Impl setTest_boolean(boolean test_boolean1);

        boolean isTest_boolean();

        ITestParcelableData_$Impl setTest_char(char test_char1);

        char getTest_char();

        ITestParcelableData_$Impl setTest_Integer(Integer test_Integer1);

        Integer getTest_Integer();

        ITestParcelableData_$Impl setTest_Long(Long test_Long1);

        Long getTest_Long();

        ITestParcelableData_$Impl setTest_Short(Short test_Short1);

        Short getTest_Short();

        ITestParcelableData_$Impl setTest_Byte(Byte test_Byte1);

        Byte getTest_Byte();

        ITestParcelableData_$Impl setTest_Float(Float test_Float1);

        Float getTest_Float();

        ITestParcelableData_$Impl setTest_Double(Double test_Double1);

        Double getTest_Double();

        ITestParcelableData_$Impl setTest_Boolean(Boolean test_Boolean1);

        Boolean getTest_Boolean();

        ITestParcelableData_$Impl setTest_Character(Character test_Character1);

        Character getTest_Character();

        ITestParcelableData_$Impl setTest_int_list(List<Integer> test_int_list1);

        List<Integer> getTest_int_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Integer> beginTest_int_listEditor();

        ITestParcelableData_$Impl setTest_long_list(List<Long> test_long_list1);

        List<Long> getTest_long_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Long> beginTest_long_listEditor();

        ITestParcelableData_$Impl setTest_byte_list(List<Byte> test_byte_list1);

        List<Byte> getTest_byte_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Byte> beginTest_byte_listEditor();

        ITestParcelableData_$Impl setTest_short_list(List<Short> test_short_list1);

        List<Short> getTest_short_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Short> beginTest_short_listEditor();

        ITestParcelableData_$Impl setTest_float_list(List<Float> test_float_list1);

        List<Float> getTest_float_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Float> beginTest_float_listEditor();

        ITestParcelableData_$Impl setTest_double_list(List<Double> test_double_list1);

        List<Double> getTest_double_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Double> beginTest_double_listEditor();

        ITestParcelableData_$Impl setTest_boolean_list(List<Boolean> test_boolean_list1);

        List<Boolean> getTest_boolean_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Boolean> beginTest_boolean_listEditor();

        ITestParcelableData_$Impl setTest_char_list(List<Character> test_char_list1);

        List<Character> getTest_char_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Character> beginTest_char_listEditor();

        ITestParcelableData_$Impl setTest_Integer_list(List<Integer> test_Integer_list1);

        List<Integer> getTest_Integer_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Integer> beginTest_Integer_listEditor();

        ITestParcelableData_$Impl setTest_Long_list(List<Long> test_Long_list1);

        List<Long> getTest_Long_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Long> beginTest_Long_listEditor();

        ITestParcelableData_$Impl setTest_Short_list(List<Short> test_Short_list1);

        List<Short> getTest_Short_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Short> beginTest_Short_listEditor();

        ITestParcelableData_$Impl setTest_Byte_list(List<Byte> test_Byte_list1);

        List<Byte> getTest_Byte_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Byte> beginTest_Byte_listEditor();

        ITestParcelableData_$Impl setTest_Float_list(List<Float> test_Float_list1);

        List<Float> getTest_Float_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Float> beginTest_Float_listEditor();

        ITestParcelableData_$Impl setTest_Double_list(List<Double> test_Double_list1);

        List<Double> getTest_Double_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Double> beginTest_Double_listEditor();

        ITestParcelableData_$Impl setTest_Boolean_list(List<Boolean> test_Boolean_list1);

        List<Boolean> getTest_Boolean_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Boolean> beginTest_Boolean_listEditor();

        ITestParcelableData_$Impl setTest_Character_list(List<Character> test_Character_list1);

        List<Character> getTest_Character_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Character> beginTest_Character_listEditor();

        ITestParcelableData_$Impl setTest_int_array(int[] test_int_array1);

        int[] getTest_int_array();

        ITestParcelableData_$Impl setTest_long_array(long[] test_long_array1);

        long[] getTest_long_array();

        ITestParcelableData_$Impl setTest_byte_array(byte[] test_byte_array1);

        byte[] getTest_byte_array();

        ITestParcelableData_$Impl setTest_short_array(short[] test_short_array1);

        short[] getTest_short_array();

        ITestParcelableData_$Impl setTest_short_array2(short[] test_short_array21);

        short[] getTest_short_array2();

        ITestParcelableData_$Impl setTest_float_array(float[] test_float_array1);

        float[] getTest_float_array();

        ITestParcelableData_$Impl setTest_double_array(double[] test_double_array1);

        double[] getTest_double_array();

        ITestParcelableData_$Impl setTest_boolean_array(boolean[] test_boolean_array1);

        boolean[] getTest_boolean_array();

        ITestParcelableData_$Impl setTest_char_array(char[] test_char_array1);

        char[] getTest_char_array();

        ITestParcelableData_$Impl setTest_Integer_array(Integer[] test_Integer_array1);

        Integer[] getTest_Integer_array();

        ITestParcelableData_$Impl setTest_Long_array(Long[] test_Long_array1);

        Long[] getTest_Long_array();

        ITestParcelableData_$Impl setTest_Short_array(Short[] test_Short_array1);

        Short[] getTest_Short_array();

        ITestParcelableData_$Impl setTest_Byte_array(Byte[] test_Byte_array1);

        Byte[] getTest_Byte_array();

        ITestParcelableData_$Impl setTest_Float_array(Float[] test_Float_array1);

        Float[] getTest_Float_array();

        ITestParcelableData_$Impl setTest_Double_array(Double[] test_Double_array1);

        Double[] getTest_Double_array();

        ITestParcelableData_$Impl setTest_Boolean_array(Boolean[] test_Boolean_array1);

        Boolean[] getTest_Boolean_array();

        ITestParcelableData_$Impl setTest_Character_array(Character[] test_Character_array1);

        Character[] getTest_Character_array();

        ITestParcelableData_$Impl setTest_String(String test_String1);

        String getTest_String();

        ITestParcelableData_$Impl setTest_String_array(String[] test_String_array1);

        String[] getTest_String_array();

        ITestParcelableData_$Impl setTest_String_list(List<String> test_String_list1);

        List<String> getTest_String_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, String> beginTest_String_listEditor();

        ITestParcelableData_$Impl setTest_ResultData(Test test_ResultData1);

        Test getTest_ResultData();

        ITestParcelableData_$Impl setTest_ResultData_list(List<Test> test_ResultData_list1);

        List<Test> getTest_ResultData_list();

        ListPropertyEditor<? extends ITestParcelableData_$Impl, Test> beginTest_ResultData_listEditor();

        ITestParcelableData_$Impl setTest_ResultData_array(Test[] test_ResultData_array1);

        Test[] getTest_ResultData_array();

        ITestParcelableData_$Impl setTest_SparseArray(SparseArray<Test> test_SparseArray1);

        SparseArray<Test> getTest_SparseArray();

        SparseArrayPropertyEditor<? extends ITestParcelableData_$Impl, Test> beginTest_SparseArrayEditor();
    }
}
