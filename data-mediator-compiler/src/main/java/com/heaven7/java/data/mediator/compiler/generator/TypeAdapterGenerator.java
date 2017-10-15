package com.heaven7.java.data.mediator.compiler.generator;


import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.GlobalConfig;
import com.heaven7.java.data.mediator.compiler.Util;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Collection;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.FieldData.*;
/**
 * the type adapter generator for gson.
 */
public class TypeAdapterGenerator {

    public static boolean generate(TargetClassInfo info, Collection<FieldData> fields, Filer filer){
        if(!GlobalConfig.getInstance().isJsonAdapterEnabled()){
            //normal ok
            return true;
        }
        ClassName cn_impl = ClassName.get(info.getPackageName(), info.getCurrentClassname());
        ClassName cn_inter = ClassName.get(info.getPackageName(), info.getDirectParentInterfaceName());
        ParameterizedTypeName superTypeName = ParameterizedTypeName.get(
                ClassName.get(PKG_GSON_SUPPORT, SN_BASE_TYPE_ADAPTER), cn_inter);

        TypeSpec.Builder builder = TypeSpec.classBuilder(Util.getTypeAdapterName(info.getDirectParentInterfaceName()))
                .addModifiers(Modifier.PUBLIC)
               .superclass(superTypeName);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of(" super();\n"));

        ClassName cn_gson_prop = ClassName.get(PKG_GSON_SUPPORT, SN_GSON_PROPERTY);
        for(FieldData fd : fields){
            //gson persistence json.
            if(Util.hasFlag(fd.getFlags(), FLAG_GSON_PERSISTENCE)) {
                constructor.addStatement(" this.addGsonProperty($T.of($T.$N, $S, $L, $L))", cn_gson_prop, cn_inter, fd.getFieldConstantName(),
                        getSerializeName(fd), getSinceValue(fd), getUntilValue(fd));
            }
        }
        builder.addMethod(constructor.build());
        builder.addMethod(MethodSpec.methodBuilder("create")
                .returns(cn_inter)
                .addModifiers(Modifier.PROTECTED)
                .addStatement("return new $T()", cn_impl)
                .build());

        TypeSpec typeSpec = builder.build();
        try {
            JavaFile.builder(info.getPackageName(), typeSpec)
                    .build().writeTo(filer);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    private static String getSerializeName(FieldData fd){
        return fd.getSerializeName() == null ? "" : fd.getSerializeName();
    }
    private static double getSinceValue(FieldData fd){
        if(fd.isSinceEnabled()){
            return fd.getSince();
        }else{
            return 1.0;
        }
    }
    private static double getUntilValue(FieldData fd){
        if(fd.isUntilEnabled()){
            return fd.getUntil();
        }else{
            return Integer.MAX_VALUE;
        }
    }
}
