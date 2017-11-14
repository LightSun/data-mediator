package com.heaven7.plugin.idea.data_mediator.test;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Fields;

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

    @Fields({})
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
    public interface ITestParcelableData_$Impl {
    }
}
