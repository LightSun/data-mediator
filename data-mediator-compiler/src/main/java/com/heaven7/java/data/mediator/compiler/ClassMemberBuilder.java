package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.Util.getFieldModifier;
import static com.heaven7.java.data.mediator.compiler.Util.hasFlag;

/**
 * Created by heaven7 on 2017/8/30.
 */
/*public*/ class ClassMemberBuilder extends BaseMemberBuilder {

    private static final String GOOGLE_GSON_ANNO_PACKAGE ="com.google.gson.annotations";

    @Override
    protected MethodSpec.Builder onCreateConstructor() {
        //default public empty constructor
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
    }

    @Override
    protected MethodSpec.Builder onBuildSparseArrayEditor(FieldData field,
                     String nameForMethod, TypeInfo info, TypeName curModule) {
        ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_SPARSE_ARRAY_EDITOR);
        ClassName cn_dm_delegate = ClassName.get(PKG_DM_INTERNAL, SIMPLE_NAME_DM_DELEGATE);
        ClassName cn_sa = ClassName.get(PKG_JAVA_BASE_UTIL, SIMPLE_NAME_SPARSE_ARRAY);

        return PropertyEditorBuildUtils.buildSparseArrayEditorWithoutModifier(
                field, nameForMethod, info, curModule)
                .addModifiers(Modifier.PUBLIC)
                    .beginControlFlow("if ($N == null)", field.getPropertyName())
                    .addStatement("$N = new $T<>()", field.getPropertyName(), cn_sa)
                    .endControlFlow()
                .addStatement("return new $T<$T,$T>(this, " +
                        "$T.getDefault().getSparseArrayDelegate($N)," +
                        " null, null)",
                        cn_editor, curModule, info.getSimpleTypeNameBoxed(),
                        cn_dm_delegate, field.getPropertyName())
                ;
    }

    @Override
    protected MethodSpec.Builder onBuildListEditor(FieldData field, String nameForMethod,
                                                   TypeInfo info, TypeName curModule) {
        ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_LIST_PROP_EDITOR);
        return PropertyEditorBuildUtils.buildListEditorWithoutModifier(
                     field, nameForMethod, info, curModule)
                .addModifiers(Modifier.PUBLIC)
                    .beginControlFlow("if ($N == null)", field.getPropertyName())
                    .addStatement("$N = new $T<>()", field.getPropertyName(), ArrayList.class)
                    .endControlFlow()
                .addStatement("return new $T<$T,$T>(this, $N, null, null)",
                        cn_editor, curModule, info.getSimpleTypeNameBoxed(), field.getPropertyName())
                ;
    }

    @Override
    protected MethodSpec.Builder onBuildGet(FieldData field, String nameForMethod, TypeInfo info) {
        MethodSpec.Builder get = MethodSpec.methodBuilder(field.getGetMethodPrefix() + nameForMethod)
                .returns(info.typeName)
                .addModifiers(Modifier.PUBLIC)
               .addCode("return $N;\n", field.getPropertyName());
        return get;
    }
    @Override
    protected MethodSpec.Builder onBuildSet(FieldData field, String nameForMethod, TypeInfo info, TypeName typeOfReturn) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(DataMediatorConstants.SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(typeOfReturn)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this.$N = $N;\n", field.getPropertyName(), info.paramName)
                ;
        if(typeOfReturn != TypeName.VOID){
            set.addCode("return this;\n");
        }
        return set;
    }

    @Override
    protected MethodSpec.Builder onBuildSuperSet(FieldData field, String nameForMethod, TypeInfo info, TypeName typeOfReturn) {
        final String setMethodName = DataMediatorConstants.SET_PREFIX + nameForMethod;
        MethodSpec.Builder set = MethodSpec.methodBuilder(setMethodName)
                .addParameter(info.typeName, info.paramName)
                .returns(typeOfReturn)
                .addModifiers(Modifier.PUBLIC)
                .addCode("return ($T)super.$N($N);\n", typeOfReturn, setMethodName, info.paramName)
                ;
        return set;
    }

    @Override
    protected FieldSpec.Builder onBuildField(FieldData field, TypeInfo info) {
        final FieldSpec.Builder builder   = FieldSpec.builder(info.typeName,
                field.getPropertyName(), getFieldModifier(field));
        //check serializeName , that support serialize and deserialize for GSON
        final String serializeName = field.getSerializeName();
        if (serializeName != null && !serializeName.equals("")) {
            builder.addAnnotation(AnnotationSpec.builder(
                    ClassName.get(GOOGLE_GSON_ANNO_PACKAGE, "SerializedName"))
                    .addMember("value","$S", serializeName)
                    .build());
        }
        if(hasFlag(field.getFlags(), FieldData.FLAG_EXPOSE_DEFAULT)){
            //if have FLAG_EXPOSE_SERIALIZE_FALSE, serialize = false. same as FLAG_EXPOSE_DESERIALIZE_FALSE.
            boolean falseSerialize =  hasFlag(field.getFlags(), FieldData.FLAG_EXPOSE_SERIALIZE_FALSE);
            boolean falseDeserialize = hasFlag(field.getFlags(), FieldData.FLAG_EXPOSE_DESERIALIZE_FALSE);
            builder.addAnnotation(AnnotationSpec.builder(ClassName.get(GOOGLE_GSON_ANNO_PACKAGE, "Expose"))
                    .addMember("serialize", "$L", !falseSerialize)
                    .addMember("deserialize", "$L", !falseDeserialize)
                    .build()
            );
        }
        return builder;
    }
}
