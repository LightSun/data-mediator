package com.heaven7.java.data.mediator.test;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by Administrator on 2017/9/8 0008.
 */
public class SimpleGenerator {

    private static String  mClassname = "TestBean";
    private static String  mTargetPackage = "com.heaven7";
    private static ClassName creator = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcelable", "Creator");
    private static ClassName parcel = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcel");
    private static ClassName parcelable = ClassName.get("com.heaven7.java.data.mediator.test",
            "Parcelable");

    public static void main(String[] args){
        final String in = "in";
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(parcel, in);
        MethodSpec methodSpec = builder.addStatement("int[] arr = $N.createIntArray()", in)
                .addStatement("final int size = arr.length")
                .addStatement("this.$N = new short[size]", "age")
                .beginControlFlow("for(int i= 0 ; i < size ; i++)")
                .addStatement("this.$N[i] = (short)arr[i]", "age")
                .endControlFlow()
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(mClassname)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(parcelable)
                .addMethod(methodSpec)
                .build();

        String test = JavaFile.builder(mTargetPackage, typeSpec).build().toString();
        System.out.println(test);
    }
}
