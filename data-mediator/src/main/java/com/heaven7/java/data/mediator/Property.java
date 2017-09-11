package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/9/11 0011.
 */
public class Property{

     private final String type;
     private final String name;
     private final int complexType;

     public Property(String type, String name, int complexType) {
          this.type = type;
          this.name = name;
          this.complexType = complexType;
     }

     public Class<?> getType() {
          //TODO check primitive
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
