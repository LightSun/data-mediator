package com.heaven7.java.data.mediator.compiler.util;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.data.mediator.ImportDesc;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * @author heaven7
 * @since 1.4.0
 */
public class TypeUtils {

    public static List<String> classesToClassNames(ImportDesc desc){
        List<String> types = null;
        //read Class<?> in compile time is wrong. see https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/.
        try {
            desc.classes();
        }catch (MirroredTypesException mte){
            List<? extends TypeMirror> mirrors = mte.getTypeMirrors();
            types = convertToClassname(mirrors, null);
        }
        return types;
    }

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

    //often is TypeMirror , but primitive may cause ClassCastException
    public static List<String> convertToClassname(List<?> mirrors,@Nullable List<String> out) {
        if(out == null){
            out = new ArrayList<>();
        }else{
            out.clear();
        }
        for(int i = 0 , size = mirrors.size() ; i < size ; i++){
            String type = mirrors.get(i).toString();
            if(type.endsWith(".class")){
                type = type.substring(0, type.lastIndexOf("."));
            }
            out.add(type);
        }
        return out;
    }
}
