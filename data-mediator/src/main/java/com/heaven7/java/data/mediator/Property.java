package com.heaven7.java.data.mediator;

/**
 * this is a property class indicate a field of Entity Data.
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

     /**
      * create a property instance by type , name  and complex type
      * @param type the type string. eg: 'int', 'java.lang.Integer'
      * @param name the property name
      * @param complexType the complex type. see {@linkplain FieldFlags#COMPLEXT_ARRAY} and {@linkplain FieldFlags#COMPLEXT_LIST}
      */
     public Property(String type, String name, int complexType) {
          if(type == null || name == null){
               throw new NullPointerException();
          }
          this.type = type;
          this.name = name;
          this.complexType = complexType;
     }

     /**
      * get the property base type.
      * @return the base type.
      * @see #getComplexType()
      */
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

     /**
      * get the property name.
      * @return the property name
      */
     public String getName() {
          return name;
     }

     /**
      * get the complex type
      * @return the complex type
      */
     public int getComplexType() {
          return complexType;
     }

     /**
      * {@inheritDoc}
      * @param o the object
      * @return true if equals.
      * @see Object#equals(Object)
      * @since 1.0.3
      */
     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;

          Property property = (Property) o;

          if (complexType != property.complexType) return false;
          if (!type.equals(property.type)) return false;
          return name.equals(property.name);
     }

     /**
      * {@inheritDoc}
      * @return the hash code.
      * @see Object#hashCode()
      * @since 1.0.3
      */
     @Override
     public int hashCode() {
          int result = type.hashCode();
          result = 31 * result + name.hashCode();
          result = 31 * result + complexType;
          return result;
     }
}
