package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import java.util.List;

import static com.heaven7.java.data.mediator.processor.Util.getParamName;
import static com.heaven7.java.data.mediator.processor.Util.getPropNameForMethod;

/**
 * the base member builder. just build method for interface.
 * Created by heaven7 on 2017/8/30.
 */
public class BaseMemberBuilder {

    public static final String SET_PREFIX = "set";
    public static final String GET_PREFIX = "get";

    public void build(TypeSpec.Builder builder, List<FieldData> mFields) {
        for (FieldData field : mFields) {
            Class<?> type = field.getType();
            String nameForMethod = getPropNameForMethod(field.getPropertyName());

            TypeInfo info = new TypeInfo();
            getTypeName(field, type, info);

            FieldSpec.Builder fieldBuilder = onBuildField(field, info);
            if (fieldBuilder != null) {
                builder.addField(fieldBuilder.build());
            }
            //get and set
            MethodSpec.Builder get = onBuildGet(field, nameForMethod, info);
            MethodSpec.Builder set = onBuildSet(field, nameForMethod, info);
            builder.addMethod(get.build())
                    .addMethod(set.build());

        }
    }

    protected FieldSpec.Builder onBuildField(FieldData field, TypeInfo info) {
        return null;
    }

    protected MethodSpec.Builder onBuildGet(FieldData field,
                                            String nameForMethod, TypeInfo info) {
        MethodSpec.Builder get = MethodSpec.methodBuilder(GET_PREFIX + nameForMethod)
                .returns(info.typeName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return get;
    }

    protected MethodSpec.Builder onBuildSet(FieldData field,
                                            String nameForMethod, TypeInfo info) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return set;
    }

    public static void getTypeName(FieldData field, Class<?> type, TypeInfo info) {
        switch (field.getComplexType()) {
            case FieldData.COMPLEXT_ARRAY:
                info.setTypeName(ArrayTypeName.of(type));
                info.setParamName("array1");
                break;

            case FieldData.COMPLEXT_LIST:
                info.setParamName("list1");
                TypeName typeName = ParameterizedTypeName.get(ClassName.get(List.class),
                        WildcardTypeName.get(type).box());
                info.setTypeName(typeName);
                break;

            default:
                info.setTypeName(TypeName.get(type));
                info.setParamName(getParamName(type.getSimpleName()));
                break;
        }
    }
}
