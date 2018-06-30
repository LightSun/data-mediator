package com.heaven7.java.data.mediator.compiler.generator;


import com.heaven7.java.data.mediator.ImportDesc;
import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.module.ImportDescData;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import java.io.IOException;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.util.TypeUtils.getTypeName;

/**
 * the expression econtext generator
 * @author heaven7
 * @since 1.4.5
 */
public class ExpreContextGenerator extends BaseGenerator{

    public ExpreContextGenerator(ProcessorContext context) {
        super(context);
    }

    public boolean generate(TypeElement inter, String proxyName, ImportDescData idd, GroupPropertyGenerator.TypeElementDelegate delegate){
        final String packageName = getElements().getPackageOf(inter).getQualifiedName().toString();

        //ClassName cn_context = ClassName.get(packageName, proxyName, SN_EXPRESSION_CONTEXT);
        TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(inter) + EXPRE_CONTEXT_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(CodeBlock.of(DOC));
        //super class
        TypeElement superInter = getSuperInterface(inter, delegate);
        if(superInter != null){
            final String pkg = getElements().getPackageOf(superInter).getQualifiedName().toString();
            final ClassName cn = ClassName.get(pkg,
                    getContext().getTargetClassName(superInter) + EXPRE_CONTEXT_SUFFIX);
            builder.superclass(cn);
        }else{
            builder.superclass(ClassName.get(PKG_UTIL, SN_EXPRESSION_EVALUATOR, SN_SIMPLE_EXPRESSION_CONTEXT));
        }
        //constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()");
        //add static refer
        for (Map.Entry<String, String> en : idd.getImportDatas().entrySet()){
            constructor.addStatement("addStaticClass($S, $T.class)", en.getKey(), getTypeName(en.getValue()));
        }
        builder.addMethod(constructor.build());
        //generate java file
        try {
            JavaFile.builder(packageName, builder.build())
                    .build()
                    .writeTo(getFiler());
        } catch (IOException e) {
            getProcessorPrinter().error(TAG, "generate", Util.toString(e));
            return false;
        }
        return true;
    }

    protected boolean hasAnnotation(TypeElement te) {
        ImportDesc desc = te.getAnnotation(ImportDesc.class);
        return desc != null && desc.names().length > 0;
    }
}
