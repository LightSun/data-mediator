package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

import static com.heaven7.java.data.mediator.processor.Util.getParamName;
import static com.heaven7.java.data.mediator.processor.Util.getPropNameForMethod;

/**
 * the base member builder. just build method for interface.
 * often used to build common member(field and method.)
 * Created by heaven7 on 2017/8/30.
 */
public class BaseMemberBuilder {

    public static final String SET_PREFIX = "set";
    public static final String GET_PREFIX = "get";


    public void build(TypeSpec.Builder builder, List<FieldData> mFields) {
        //add private static final long serialVersionUID
        builder.addSuperinterface(ClassName.get("java.io","Serializable"));
        if(!isInterface()){
            builder.addField(FieldSpec.builder(long.class, "serialVersionUID",
                    Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                    .initializer(" 1L")
                    .build());
        }
        MethodSpec.Builder constructorBuilder = onCreateConstructor();
        for (FieldData field : mFields) {
            String nameForMethod = getPropNameForMethod(field.getPropertyName());

            TypeInfo info = new TypeInfo();
            getTypeName(field, info);

            FieldSpec.Builder fieldBuilder = onBuildField(field, info);
            if (fieldBuilder != null) {
                builder.addField(fieldBuilder.build());
            }
            //get and set
            MethodSpec.Builder get = onBuildGet(field, nameForMethod, info);
            MethodSpec.Builder set = onBuildSet(field, nameForMethod, info);
            if(constructorBuilder != null){
                onBuildConstructor(constructorBuilder, field, info);
            }
            builder.addMethod(get.build())
                    .addMethod(set.build());

        }
    }
    protected boolean isInterface(){
        return true;
    }

    protected FieldSpec.Builder onBuildField(FieldData field, TypeInfo info) {
        return null;
    }

    protected MethodSpec.Builder onCreateConstructor() {
        return null;
    }

    protected void onBuildConstructor(MethodSpec.Builder construct, FieldData field, TypeInfo info) {
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

    static void getTypeName(FieldData field, TypeInfo info) {
        final FieldData.TypeCompat typeCompat = field.getTypeCompat();
        TypeName rawTypeName = typeCompat.getInterfaceTypeName();
        switch (field.getComplexType()) {
            case FieldData.COMPLEXT_ARRAY:
                info.setTypeName(ArrayTypeName.of(rawTypeName));
                info.setParamName("array1");
                break;

            case FieldData.COMPLEXT_LIST:
                info.setParamName("list1");
                TypeName typeName = ParameterizedTypeName.get(ClassName.get(List.class),
                        rawTypeName.box());
                info.setTypeName(typeName);
                break;

            default:
                info.setTypeName(rawTypeName);
                info.setParamName(getParamName(typeCompat.getTypeMirror()));
                break;
        }
    }
}
