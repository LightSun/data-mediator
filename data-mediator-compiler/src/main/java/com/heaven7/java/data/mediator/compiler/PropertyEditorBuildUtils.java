package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
/**
 * the list property build helper
 * Created by heaven7 on 2017/9/22.
 */
public final class PropertyEditorBuildUtils {

    // ListPropertyEditor<IStudent,String> newTagsEditor();
    public static MethodSpec.Builder buildListEditorWithoutModifier(FieldData field, String nameForMethod,
                                                             TypeInfo info, TypeName curModule) {
        ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_LIST_PROP_EDITOR);
        TypeName returnType = ParameterizedTypeName.get(cn_editor,
                WildcardTypeName.subtypeOf(curModule) , info.getSimpleTypeNameBoxed());
        return MethodSpec.methodBuilder(BEGIN_PREFIX + nameForMethod + EDITOR_SUFFIX)
                .returns(returnType);
    }
    // public SparseArrayPropertyEditor<IStudent, String> beginCityDataEditor();
    public static MethodSpec.Builder buildSparseArrayEditorWithoutModifier(
            FieldData field, String nameForMethod, TypeInfo info, TypeName curModule) {

        ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_SPARSE_ARRAY_EDITOR);
        TypeName returnType = ParameterizedTypeName.get(cn_editor,
                WildcardTypeName.subtypeOf(curModule) , info.getSimpleTypeNameBoxed());
        return MethodSpec.methodBuilder(BEGIN_PREFIX + nameForMethod + EDITOR_SUFFIX)
                .returns(returnType);
    }
}
