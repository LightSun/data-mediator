package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

import static com.heaven7.java.data.mediator.compiler.Util.getPropNameForMethod;
import static com.heaven7.java.data.mediator.compiler.Util.getTypeName;

/**
 * the base member builder. just build method for interface.
 * often used to build common member(field and method.)
 * Created by heaven7 on 2017/8/30.
 */
/*public*/ class BaseMemberBuilder {


    //typeOfReturn used for chain call
    public final void build(TypeSpec.Builder builder, List<FieldData> mFields, Set<FieldData> superFields,
                            TypeName typeOfReturn, TypeName curModule) {
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
            MethodSpec.Builder set = onBuildSet(field, nameForMethod, info, typeOfReturn);
            if(constructorBuilder != null){
                onBuildConstructor(constructorBuilder, field, info);
            }
            builder.addMethod(get.build())
                    .addMethod(set.build());
            // for add/remove Callbacks .
            if(field.isList()){
                MethodSpec.Builder listEditor = onBuildListEditor(field, nameForMethod, info, curModule);
                builder.addMethod(listEditor.build());
            }
        }
        //change method for super. if use chain mode. chain mode means set method not return void. just return bean interface.
        if(typeOfReturn != TypeName.VOID && superFields != null){
            for (FieldData field : superFields){
                String nameForMethod = getPropNameForMethod(field);

                TypeInfo info = new TypeInfo();
                getTypeName(field, info);
                MethodSpec.Builder set = onBuildSuperSet(field, nameForMethod, info, typeOfReturn);
                builder.addMethod(set.build());
            }
        }
        if(constructorBuilder != null){
            builder.addMethod(constructorBuilder.build());
        }
    }

    protected MethodSpec.Builder onBuildListEditor(FieldData field, String nameForMethod,
                                                 TypeInfo info, TypeName curModule) {
        return ListPropertyBuildUtils.buildListEditorWithoutModifier(
                         field, nameForMethod, info, curModule)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
    }

    //==========================================================================================
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
        MethodSpec.Builder get = MethodSpec.methodBuilder(field.getGetMethodPrefix() + nameForMethod)
                .returns(info.typeName)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return get;
    }

    protected MethodSpec.Builder onBuildSet(FieldData field,
                                            String nameForMethod, TypeInfo info, TypeName typeOfReturn) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(DataMediatorConstants.SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(typeOfReturn)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return set;
    }

    protected MethodSpec.Builder onBuildSuperSet(FieldData field, String nameForMethod, TypeInfo info, TypeName typeOfReturn) {
        MethodSpec.Builder set = MethodSpec.methodBuilder(DataMediatorConstants.SET_PREFIX + nameForMethod)
                .addParameter(info.typeName, info.paramName)
                .returns(typeOfReturn)
                .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);
        return set;
    }
}
