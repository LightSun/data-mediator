package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/9/11 0011.
 */
public class Property{

     private static final String TYPE_int      = "int";
     private static final String TYPE_long     = "long";
     private static final String TYPE_short    = "short";
     private static final String TYPE_byte     = "byte";
     private static final String TYPE_boolean  = "boolean";
     private static final String TYPE_float    = "float";
     private static final String TYPE_double   = "double";
     private static final String TYPE_char     = "char";

     private final String type;
     private final String name;
     private final int complexType;

     public Property(String type, String name, int complexType) {
          this.type = type;
          this.name = name;
          this.complexType = complexType;
     }

     public Class<?> getType() {
          switch (type){
               case TYPE_int:
                    return int.class;
               case TYPE_long:
                    return long.class;
               case TYPE_short:
                    return short.class;
               case TYPE_byte:
                    return byte.class;
               case TYPE_boolean:
                    return boolean.class;
               case TYPE_float:
                    return float.class;
               case TYPE_double:
                    return double.class;
               case TYPE_char:
                    return char.class;
          }
          try {
               return Class.forName(type);
          } catch (ClassNotFoundException e) {
               throw new RuntimeException(e);
          }
     }
     public String getName() {
          return name;
     }
     public int getComplexType() {
          return complexType;
     }
}
