package com.heaven7.java.data.mediator.compiler.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * @author heaven7
 * @since 1.4.0
 */
public class TypeUtils {

    public static TypeName getTypeName(String type){
        switch (type){
            case NAME_int:
                return TypeName.INT;

            case NAME_long:
                return TypeName.LONG;

            case NAME_short:
                return TypeName.SHORT;

            case NAME_byte:
                return TypeName.BYTE;

            case NAME_boolean:
                return TypeName.BOOLEAN;

            case NAME_float:
                return TypeName.FLOAT;

            case NAME_double:
                return TypeName.DOUBLE;

            case NAME_char:
                return TypeName.CHAR;

            case NAME_void:
                return TypeName.VOID;
        }
        return ClassName.bestGuess(type);
    }
}
