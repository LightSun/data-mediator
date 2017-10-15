package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.GlobalConfig;
import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;
import com.heaven7.java.data.mediator.compiler.Util;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/9/14 0014.
 */
public class StaticLoaderGenerator {

    public static final String CNAME = PKG_DM_INTERNAL + "." + SN_STATIC_LOADER;
    private static final String TAG  =  "StaticLoaderGenerator";

    public static boolean generateStaticCodeLoader(Filer filer, ProcessorPrinter pp){
        CodeBlock.Builder staticBuilder = CodeBlock.builder()
                //GlobalSetting.getgetDefault().setGsonVersion(xxx)
                .add("$T.getDefault().setGsonVersion($L);\n", ClassName.get(PKG_PROP, SN_GLOBAL_SETTING),
                        GlobalConfig.getInstance().getVersion());

        TypeSpec typeSpec = TypeSpec.classBuilder(SN_STATIC_LOADER)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStaticBlock(staticBuilder.build())
                .addJavadoc(CodeBlock.of(DOC))
                .build();
        try {
            JavaFile javaFile = JavaFile.builder(PKG_DM_INTERNAL, typeSpec)
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
