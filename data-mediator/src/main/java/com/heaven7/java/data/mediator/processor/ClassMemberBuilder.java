package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static com.heaven7.java.data.mediator.processor.Util.*;
import static com.heaven7.java.data.mediator.processor.FieldData.*;
/**
 * Created by heaven7 on 2017/8/30.
 */
public class ClassMemberBuilder extends BaseMemberBuilder {

    private static final String GOOGLE_GSON_ANNO_PACKAGE ="com.google.gson.annotations";

    @Override
    protected MethodSpec.Builder onBuildGet(FieldData field, String nameForMethod, TypeInfo info) {
        MethodSpec.Builder get = MethodSpec.methodBuilder(GET_PREFIX + nameForMethod)
                .returns(info.typeName)
                .addModifiers(Modifier.PUBLIC)
               .addCode("return $N;\n", field.getPropertyName());
        return get;
    }
    @Override
    protected MethodSpec.Builder onBuildSet(FieldData field, String nameForMethod, TypeInfo info) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this.$N = $N;\n", field.getPropertyName(), info.paramName)
                ;
        return set;
    }
    @Override
    protected FieldSpec.Builder onBuildField(FieldData field, TypeInfo info) {
        FieldSpec.Builder builder = FieldSpec.builder(info.typeName,
                field.getPropertyName(), getFieldModifier(field));
        //check support serialize and deserialize for GSON
        // if no flag  FLAG_SERIALIZABLE. means not use gson.
        if(hasFlag(field.getFlags(), FLAG_SERIALIZABLE)) {
            final String serializeName = field.getSerializeName();
            if (serializeName != null && !serializeName.equals("")) {
                builder.addAnnotation(AnnotationSpec.builder(
                        ClassName.get(GOOGLE_GSON_ANNO_PACKAGE, "SerializedName"))
                        .addMember("value","$S", serializeName)
                        .build());
            }else {
                builder.addAnnotation(AnnotationSpec.builder(ClassName.get(GOOGLE_GSON_ANNO_PACKAGE, "Expose"))
                        .addMember("serialize", "$L", false)
                        .addMember("deserialize", "$L", false)
                        .build()
                );
            }
        }
        return builder;
    }
}
