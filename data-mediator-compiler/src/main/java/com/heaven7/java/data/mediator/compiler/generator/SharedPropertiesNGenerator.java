package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.Collection;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * SharedProperties1, SharedProperties2...SharedProperties2N
 * as generate SharedProperties_N have a bug in main/test module on java platform.
 * it's deprecated.
 * Created by heaven7 on 2017/9/14 0014.
 */
@Deprecated
public class SharedPropertiesNGenerator {

    private static final String TAG  =  "SharedPropertiesNGenerator";

    public static boolean generateSharedProperties(Collection<FieldData> fields,
                                                   Elements elements, Filer filer, ProcessorPrinter pp){
        final ClassName cn_sp = ClassName.get(PKG_DM_INTERNAL, SIMPLE_NAME_SHARED_PROP);
        CodeBlock.Builder staticBuilder = CodeBlock.builder();
        for(FieldData fd : fields){
            staticBuilder.add("$T.putToCache($S, $S, $L);\n", cn_sp, fd.getTypeCompat().toString(),
                    fd.getPropertyName(), fd.getComplexType());
        }
        String classSimpleName = SIMPLE_NAME_SHARED_PROP + "_" + findBestIndex(elements);
        TypeSpec typeSpec = TypeSpec.classBuilder(classSimpleName)
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
    private static int findBestIndex(Elements elements){
        int index = 0;
        do{
            index ++;
        }while(elements.getTypeElement(getClassName(index)) != null);
        return index;
    }
    private static String getClassName(int index){
        return PKG_DM_INTERNAL + "." + SIMPLE_NAME_SHARED_PROP + "_" + index;
    }

}
