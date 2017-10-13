package com.heaven7.java.data.mediator.compiler.insert;


import com.heaven7.java.data.mediator.compiler.Util;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/*public*/ class TypeAdapterInsertDelegate extends TypeInsertDelegate {

    @Override
    public void addStaticCode(TypeSpec.Builder typeBuilder, Object param) {
        ClassName cn_type_handler = ClassName.get(PKG_GSON_SUPPORT, SN_TYPE_HANDLER);
        ClassName cn_impl = ClassName.get(getClassInfo().getPackageName(), getClassInfo().getCurrentClassname());
        ClassName cn_type_adapter = ClassName.get(getClassInfo().getPackageName(),
                Util.getTypeAdapterName(getClassInfo().getDirectParentInterfaceName()));

        typeBuilder.addStaticBlock(CodeBlock.of("$T.registerTypeAdapter($T.class, new $T());\n",
                cn_type_handler, cn_impl, cn_type_adapter));
    }

    @Override
    public void addClassAnnotation(TypeSpec.Builder typeBuilder) {
        ClassName cn_type_adapter = ClassName.get(getClassInfo().getPackageName(),
                Util.getTypeAdapterName(getClassInfo().getDirectParentInterfaceName()));
        typeBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get(PKG_GSON_ANNO, SN_GSON_JSON_ADAPTER))
                .addMember("value", "$T.class", cn_type_adapter)
                .build());
    }
}
