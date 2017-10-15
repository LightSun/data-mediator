package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.GlobalConfig;
import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;
import com.heaven7.java.data.mediator.compiler.Util;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.HashMap;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/9/14 0014.
 */
public class SharedPropertiesGenerator {

    private static final String TAG  =  "SharedPropertiesGenerator";

    public static boolean generateSharedProperties(Filer filer, ProcessorPrinter pp){
        ClassName propertyCN = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        TypeName stringTN = TypeName.get(String.class);
        ParameterizedTypeName pt_cache = ParameterizedTypeName.get(ClassName.get(HashMap.class),
                stringTN, propertyCN);

        FieldSpec fieldSpec = FieldSpec.builder(pt_cache,"sCache", Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
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
                .addModifiers(Modifier.PROTECTED, Modifier.STATIC)
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
                .addStatement("return sCache.get(generateKey(typeName, propName, complexFlag))")
                .build();

        CodeBlock.Builder staticBuilder = CodeBlock.builder()
                //GlobalSetting.getgetDefault().setGsonVersion(xxx)
                .add("$T.getDefault().setGsonVersion($L);\n", ClassName.get(PKG_PROP, SN_GLOBAL_SETTING),
                        GlobalConfig.getInstance().getVersion())
                .add("sCache = new HashMap<>();\n");

        //add selected ( for ISelectable)
        staticBuilder.add("putToCache($S, $S, $L);\n", FD_SELECTABLE.getTypeCompat().toString(),
                       FD_SELECTABLE.getPropertyName(), FD_SELECTABLE.getComplexType())
                .beginControlFlow("try")
                    .beginControlFlow(" for(int i = 1; i < 100 ; i ++)")
                    .addStatement("$T.forName($S + $S +i)", Class.class,
                            "com.heaven7.java.data.mediator.factory.SharedProperties", "_")
                .endControlFlow()
                .nextControlFlow("catch (Exception e)")
                .add("//ignore \n")
                .endControlFlow();
       /* for(FieldData fd : fields){
            staticBuilder.add("putToCache($S, $S, $L);\n", fd.getTypeCompat().toString(),
                    fd.getPropertyName(), fd.getComplexType());
        }*/

        TypeSpec typeSpec = TypeSpec.classBuilder(SIMPLE_NAME_SHARED_PROP)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStaticBlock(staticBuilder.build())
                .addField(fieldSpec)
                .addMethod(get)
                .addMethod(putToCache)
                .addMethod(generateKey)
                .build();


        try {
            JavaFile javaFile = JavaFile.builder(PKG_SHARED_PROP, typeSpec)
                    .build();
           // System.out.println(javaFile.toString());
            javaFile.writeTo(filer);
        } catch (IOException e) {
            pp.error(TAG, "generateSharedProperties", Util.toString(e));
            return false;
        }
        return true;
    }

}
