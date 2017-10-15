package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/9/14 0014.
 */
public class PoetTest {

    //test generate MediatorSharedProperties.
    public static void main(String[] args){
        generateSharedProperties();
        //generateSharedProperties2();
       // testGenerateProxy();
        testToStringBuilder();
    }

    private static void testToStringBuilder(){
        List<FieldData> set = new ArrayList<>();
        FieldData fd = new FieldData();
        fd.setPropertyName("name");
        fd.setFlags(FieldData.FLAG_TO_STRING);
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("java.lang.String")));

        set.add(fd);
        fd = new FieldData();
        fd.setPropertyName("id");
        fd.setFlags(FieldData.FLAG_TO_STRING);
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("java.lang.String")));
        set.add(fd);

        fd = new FieldData();
        fd.setPropertyName("age");
        fd.setFlags(FieldData.FLAG_TO_STRING);
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("int")));
        set.add(fd);

        MethodSpec.Builder toStringBuilder = Util.createToStringBuilderForImpl(set, false);

        TypeSpec typeSpec = TypeSpec.classBuilder("TestBean")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(toStringBuilder.build())
                .build();

        String test = JavaFile.builder("com.heaven7.test", typeSpec).build().toString();
        System.out.println(test);
    }

    private static void testGenerateProxy() {
        Set<FieldData> set = new HashSet<>();
        FieldData fd = new FieldData();
        fd.setPropertyName("name");
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("java.lang.String")));

        set.add(fd);
        fd = new FieldData();
        fd.setPropertyName("id");
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("java.lang.String")));
        set.add(fd);

        fd = new FieldData();
        fd.setPropertyName("age");
        fd.setTypeCompat(new TypeCompatImpl(null, new TypeMirrorImpl("int")));
        set.add(fd);

        String interfaceName  = "IStudent";
        String pkg  = "com.heaven7.java.data.mediator.compiler.test";
        ClassName cn_inter = ClassName.get(pkg, interfaceName);
        ParameterizedTypeName superTypeName = ParameterizedTypeName.get(
                ClassName.get(PKG_PROP, SIMPLE_NAME_BASE_MEDIATOR), cn_inter);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(interfaceName + PROXY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superTypeName)
                .addSuperinterface(cn_inter);

        buildFieldsAndMethods(set, cn_inter, typeBuilder);
        //constructor
        typeBuilder.addMethod(MethodSpec.constructorBuilder().addParameter(cn_inter, "base")
                 .addStatement("super(base)")
                 .build());

        TypeSpec typeSpec = typeBuilder.build();
        String test = JavaFile.builder("com.heaven7.test", typeSpec)
                .build()
                .toString();
        System.out.println(test);
    }

    private static void buildFieldsAndMethods(Set<FieldData> set, ClassName cn_inter, TypeSpec.Builder typeBuilder) {
        ClassName cn_prop = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        ClassName cn_shared_properties = ClassName.get(PKG_DM_INTERNAL, SIMPLE_NAME_SHARED_PROP);
        //fields and methods.
        for(FieldData field : set){
            final String fieldName = "PROP_" + field.getPropertyName().toUpperCase();
            typeBuilder.addField(FieldSpec.builder(cn_prop,
                    fieldName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.get($S, $S, $L)",
                            cn_shared_properties, field.getTypeCompat().toString(), field.getPropertyName(), field.getComplexType())
                    .build());
            //get and set
            String nameForMethod = Util.getPropNameForMethod(field);
            final String getMethodName = GET_PREFIX + nameForMethod;
            typeBuilder.addMethod(MethodSpec.methodBuilder(getMethodName)
                    .returns(field.getTypeCompat().getInterfaceTypeName())
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return getTarget().$N()", getMethodName)
                    .build());

            final String setMethodName = SET_PREFIX + nameForMethod;
            final String paramName = field.getPropertyName() + "1";
            typeBuilder.addMethod(MethodSpec.methodBuilder(setMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(field.getTypeCompat().getInterfaceTypeName(), paramName)
                    .addStatement("$T target = getTarget()", cn_inter)
                    .addStatement("$T oldValue = getTarget().$N()", field.getTypeCompat().getInterfaceTypeName(), getMethodName)
                    .beginControlFlow("if(getEqualsComparator().isEquals(oldValue, $N))", paramName)
                          .addStatement("return")
                    .endControlFlow()
                    .addStatement("target.$N($N)", setMethodName, paramName)
                    .addStatement("dispatchCallbacks($N, oldValue, $N)",  fieldName, paramName)
                    .build());
        }
    }

    private static void generateSharedProperties2() {
        Set<FieldData> set = new HashSet<>();
        FieldData fd = new FieldData();
        fd.setPropertyName("name");
        fd.setTypeCompat(new FieldData.TypeCompat(null, new TypeMirrorImpl("java.lang.String")));

        set.add(fd);
        fd = new FieldData();
        fd.setPropertyName("name1");
        fd.setTypeCompat(new FieldData.TypeCompat(null, new TypeMirrorImpl("java.lang.String")));
        set.add(fd);

       // SharedPropertiesGenerator.generateSharedProperties(set, null, null);
    }

    private static void generateSharedProperties() {
        ClassName propertyCN = ClassName.get("com.heaven7.java.data.mediator", "Property");
        TypeName stringTN = TypeName.get(String.class);
        ParameterizedTypeName pt_cache = ParameterizedTypeName.get(ClassName.get(HashMap.class), stringTN, propertyCN);

        FieldSpec fieldSpec = FieldSpec.builder(pt_cache,"sCache", Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                .build();

        MethodSpec generateKey = MethodSpec.methodBuilder("generateKey")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(stringTN, "typeName")
                .addParameter(stringTN, "propName")
                .addParameter(int.class, "complexFlag")
                .returns(String.class)
                .addStatement("return typeName.hashCode()+ $S + propName + $S + complexFlag", "_", "_")
                .build();

        MethodSpec putToCache = MethodSpec.methodBuilder("putToCache")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(stringTN, "typeName")
                .addParameter(stringTN, "propName")
                .addParameter(int.class, "complexFlag")
                .returns(TypeName.VOID)
                .addStatement("sCache.put(generateKey(typeName, propName, complexFlag)," +
                        " new $T(typeName, propName, complexFlag))", propertyCN)
                .build();

        MethodSpec get = MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(stringTN, "typeName")
                .addParameter(stringTN, "propName")
                .addParameter(int.class, "complexFlag")
                .returns(propertyCN)
                .addStatement("return sCache.get(generateKey(typeName, propName, complexFlag)")
                .build();


        TypeSpec typeSpec = TypeSpec.classBuilder("MediatorSharedProperties")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStaticBlock(CodeBlock.builder()
                        .add("sCache = new HashMap<>();\n")
                        .add("putToCache($S, $S, $L);\n", "java.lang.String", "name", 0)
                        .beginControlFlow("try")
                           .beginControlFlow(" for(int i = 1; i < 100 ; i ++)")
                           .addStatement("$T.forName($S + i);", Class.class, "com.heaven7.java.data.mediator.factory.SharedProperties")
                           .endControlFlow()
                        .nextControlFlow("catch (Exception e)")
                        .add("//ignore \n")
                        .endControlFlow()
                        .build())
                .addField(fieldSpec)
                .addMethod(get)
                .addMethod(putToCache)
                .addMethod(generateKey)
                .build();

        String test = JavaFile.builder("com.heaven7.test", typeSpec)
                .build()
                .toString();
        System.out.println(test);
    }

    private static class TypeMirrorImpl implements TypeMirror{

        private final String typeName;

        public TypeMirrorImpl(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public TypeKind getKind() {
            return null;
        }
        @Override
        public <R, P> R accept(TypeVisitor<R, P> v, P p) {
            return null;
        }

        public List<? extends AnnotationMirror> getAnnotationMirrors() {
            return null;
        }

        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            return null;
        }

        @Override
        public String toString() {
            return typeName;
        }
    }
    //just mock test
    private static class TypeCompatImpl extends FieldData.TypeCompat{

        public TypeCompatImpl(Types types, TypeMirror tm) {
            super(types, tm);
        }
        @Override
        public TypeName getInterfaceTypeName() {
            return ClassName.get("java.lang", "String");
        }
    }
}
