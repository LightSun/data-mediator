package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.Util.getTypeName;

/**
 * the proxy generator
 * Created by heaven7 on 2017/9/14 0014.
 */
public class ProxyGenerator {

    private static final String TAG = ProxyGenerator.class.getSimpleName();

    public static boolean generateProxy(TargetClassInfo info, List<FieldData> list,
                                        List<MethodSpec.Builder> superMethods, ISuperFieldDelegate delegate,
                                        Filer filer, ProcessorPrinter pp) {
        //String interfaceName  = "IStudent";
       // String pkg  = "com.heaven7.java.data.mediator.compiler.test";
        final String interfaceName = info.getDirectParentInterfaceName();
        final String pkg = info.getPackageName();

        ClassName cn_inter = ClassName.get(pkg, interfaceName);
        ParameterizedTypeName superTypeName = ParameterizedTypeName.get(
                ClassName.get(PKG_PROP, SIMPLE_NAME_BASE_MEDIATOR), cn_inter);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(interfaceName + PROXY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superTypeName)
                .addSuperinterface(cn_inter);

        final Set<FieldData> temp_datas = new HashSet<>(list);
        //find super fields.
        List<? extends TypeMirror> superInterfaces = info.getSuperInterfaces();
        for(TypeMirror mirror : superInterfaces){
            final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
            Set<FieldData> dependFields = delegate.getDependFields(te);
            if(dependFields != null){
                temp_datas.addAll(dependFields);
            }
        }
        //build field and methods.
        buildFieldsAndMethods(temp_datas, cn_inter, typeBuilder);

        //constructor
        typeBuilder.addMethod(MethodSpec.constructorBuilder().addParameter(cn_inter, "base")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(base)")
                .build());
        //override super methods from super interface .like IResetable.
        if(superMethods != null) {
            for (MethodSpec.Builder builder : superMethods) {
                typeBuilder.addMethod(builder.build());
            }
        }

        TypeSpec typeSpec = typeBuilder.build();
        try {
            JavaFile.builder(pkg, typeSpec)
                    .build().writeTo(filer);
        } catch (IOException e) {
            pp.note(TAG, "generateProxy", Util.toString(e));
            return false;
        }
        return true;
    }

    private static void buildFieldsAndMethods(Set<FieldData> set, ClassName cn_inter, TypeSpec.Builder typeBuilder) {
        ClassName cn_prop = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        ClassName cn_shared_properties = ClassName.get(PKG_SHARED_PROP, SIMPLE_NAME_SHARED_PROP);

        int index = 0;
        //fields and methods.
        for(FieldData field : set){
            final TypeInfo info = new TypeInfo();
            getTypeName(field, info);

            final String fieldName = "PROP_" + field.getPropertyName().toUpperCase() + "_" + index;
            index ++;
            typeBuilder.addField(FieldSpec.builder(cn_prop,
                    fieldName, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.get($S, $S, $L)",
                            cn_shared_properties, field.getTypeCompat().toString(), field.getPropertyName(), field.getComplexType())
                    .build());
            //get
            String nameForMethod = Util.getPropNameForMethod(field);
            final String getMethodName = GET_PREFIX + nameForMethod;
            typeBuilder.addMethod(MethodSpec.methodBuilder(getMethodName)
                    .returns(info.getTypeName())
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return getTarget().$N()", getMethodName)
                    .build());

            //set
            final String setMethodName = SET_PREFIX + nameForMethod;
            final String paramName = info.getParamName();
            typeBuilder.addMethod(MethodSpec.methodBuilder(setMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(info.getTypeName(), paramName)
                    .addStatement("$T target = getTarget()", cn_inter)
                    .addStatement("$T oldValue = getTarget().$N()", info.getTypeName(), getMethodName)
                        .beginControlFlow("if(getEqualsComparator().isEquals(oldValue, $N))", paramName)
                        .addStatement("return")
                        .endControlFlow()
                    .addStatement("target.$N($N)", setMethodName, paramName)
                    .addStatement("dispatchCallbacks($N, oldValue, $N)",  fieldName, paramName)
                    .build());
        }
    }
}
