package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.Util.getPropNameForMethod;
import static com.heaven7.java.data.mediator.compiler.Util.getTypeName;

/**
 * the base member builder. just build method for interface.
 * often used to build common member(field and method.)
 * Created by heaven7 on 2017/8/30.
 */
/*public*/ class BaseMemberBuilder {


    public final void build(TypeSpec.Builder builder, List<FieldData> mFields) {
        MethodSpec.Builder constructorBuilder = onCreateConstructor();
        for (FieldData field : mFields) {
            String nameForMethod = getPropNameForMethod(field);

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
        if(constructorBuilder != null){
            builder.addMethod(constructorBuilder.build());
        }
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
        MethodSpec.Builder get = MethodSpec.methodBuilder(DataMediatorConstants.GET_PREFIX + nameForMethod)
                .returns(info.typeName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return get;
    }

    protected MethodSpec.Builder onBuildSet(FieldData field,
                                            String nameForMethod, TypeInfo info) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(DataMediatorConstants.SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return set;
    }
}
