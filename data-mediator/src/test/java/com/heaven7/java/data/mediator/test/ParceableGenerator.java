package com.heaven7.java.data.mediator.test;

import com.squareup.javapoet.*;
import junit.framework.TestCase;

import javax.lang.model.element.Modifier;

/**
 * Created by heaven7 on 2017/9/7.
 */
public class ParceableGenerator extends TestCase{

    public static void main(String[] args){
        mockGenerateCreatorField();
    }

    private static void mockGenerateCreatorField() {
        ClassName obj = ClassName.get("com.heaven7.java.data.mediator.test", "TestBean");

        // ClassName creator = ClassName.get("android.os", "Parcelable", "Creator");
        //  ClassName parcel = ClassName.get("android.os", "Parcel");
        ClassName creator = ClassName.get("com.heaven7.java.data.mediator.test", "Parcelable", "Creator");
        ClassName parcel = ClassName.get("com.heaven7.java.data.mediator.test", "Parcel");

        TypeName creatorOfobj = ParameterizedTypeName.get(creator, obj);//泛型

        //方法参数。
        ParameterSpec sizeParam = ParameterSpec.builder(TypeName.INT, "size")
                .build();
        //return 匿名对象
        TypeSpec aSimpleThung = TypeSpec.anonymousClassBuilder(
                       ""
                   )
                .superclass(creatorOfobj)
                /**
                 *   @Override
                public CaseRecordEntity createFromParcel(Parcel source) {
                return new CaseRecordEntity(source);
                }
                 */
                .addMethod(MethodSpec.methodBuilder("createFromParcel")
                        .addAnnotation(Override.class)
                        .returns(obj)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(parcel, "in")
                        .addCode("/* code snippets */\n")
                        .addStatement("return new $T(in)", obj)
                        .build())
                /**
                 *  @Override
                    public CaseRecordEntity[] newArray(int size) {
                        return new CaseRecordEntity[size];
                    }
                 */
                .addMethod(MethodSpec.methodBuilder("newArray")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(ArrayTypeName.of(obj))
                        .addParameter(sizeParam)
                        .addStatement("return new $T[size]", obj)
                        .build())
                .build();

        TypeSpec taco = TypeSpec.classBuilder("Taco")
                .addField(FieldSpec.builder(creatorOfobj, "CREATOR")
                        .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                        .initializer("$L", aSimpleThung)
                        .build())
                .build();
        System.out.println(JavaFile.builder("com.heaven7", taco)
                .build()
                .toString());
    }
}
