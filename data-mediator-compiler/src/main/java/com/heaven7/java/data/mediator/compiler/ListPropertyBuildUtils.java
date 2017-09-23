package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
/**
 * the list property build helper
 * Created by heaven7 on 2017/9/22.
 */
public final class ListPropertyBuildUtils{

    // ListPropertyEditor<IStudent,String> newTagsEditor();
    public static MethodSpec.Builder buildListEditorWithoutModifier(FieldData field, String nameForMethod,
                                                             TypeInfo info, TypeName curModule) {
        ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_LIST_PROP_EDITOR);
        TypeName returnType = ParameterizedTypeName.get(cn_editor,
                WildcardTypeName.subtypeOf(curModule) , info.getSimpleTypeNameBoxed());
        return MethodSpec.methodBuilder(BEGIN_PREFIX + nameForMethod + EDITOR_SUFFIX)
                .returns(returnType);
    }

   /* public static MethodSpec.Builder buildAddWithoutModifier(FieldData field, String nameForMethod,
                                            TypeInfo info, TypeName typeOfReturn) {
        TypeName pName = ParameterizedTypeName.get(ClassName.get(Collection.class),
                info.getTypeName().box());
        return MethodSpec.methodBuilder(ADD_PREFIX + nameForMethod)
                .returns(typeOfReturn)
                .addParameter(pName, info.paramName);
    }

    public static MethodSpec.Builder buildAddWithIndexWithoutModifier(FieldData field, String nameForMethod,
                                            TypeInfo info, TypeName typeOfReturn) {
        TypeName pName = ParameterizedTypeName.get(ClassName.get(Collection.class),
                info.getTypeName().box());
        return MethodSpec.methodBuilder(ADD_PREFIX + nameForMethod)
                .returns(typeOfReturn)
                .addParameter(TypeName.INT, PARAM_NAME)
                .addParameter(pName, info.paramName);
    }

    public static MethodSpec.Builder buildRemoveWithoutModifier(FieldData field, String nameForMethod,
                                               TypeInfo info, TypeName typeOfReturn) {
        TypeName pName = ParameterizedTypeName.get(ClassName.get(Collection.class),
                info.getTypeName().box());
        return MethodSpec.methodBuilder(REMOVE_PREFIX + nameForMethod)
                .returns(typeOfReturn)
                .addParameter(pName, info.paramName);
    }*/

}
