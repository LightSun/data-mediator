package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.*;


import javax.lang.model.element.Modifier;
import java.util.Collection;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * 内部接口插入器: Poolable.
 * Created by heaven7 on 2017/9/27 0027.
 */
public class PoolableInsert {

    public void addStaticCode(TypeSpec.Builder typeBuilder, String packageName, String className, Object maxCount){
        final int maxPoolCount = (int) maxCount;
        if(maxPoolCount > 0 ) {
            typeBuilder.addStaticBlock(CodeBlock.of("$T.preparePool($S, $L);\n",
                    ClassName.get(PKG_PROP, SIMPLE_NAME_DATA_POOL),
                    packageName + "." + className, maxPoolCount
            ));
        }
    }

    public void addSuperInterface(TypeSpec.Builder typeBuilder){
        typeBuilder.addSuperinterface(ClassName.get(PKG_PROP, SIMPLE_NAME_DATA_POOL, SIMPLE_NAME_POOLABLE));
    }

    public void overrideMethodsForImpl(TypeSpec.Builder typeBuilder, Collection<FieldData> fields, boolean hasSuperClass){
        // void clearProperties();
        MethodSpec.Builder builder_cp = MethodSpec.methodBuilder("clearProperties")
                .returns(TypeName.VOID)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);
        if(hasSuperClass){
            builder_cp.addStatement("super.clearProperties()");
        }else{
            // recycle
            typeBuilder.addMethod(MethodSpec.methodBuilder("recycle")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(TypeName.VOID)
                    .addStatement("$T.recycle(this)", ClassName.get(PKG_PROP, SIMPLE_NAME_DATA_POOL))
                    .build());
        }
        //T,L,S, N
        for(FieldData fd : fields){
            Util.addInitStatement(fd, builder_cp);
        }
        typeBuilder.addMethod(builder_cp.build());
    }

    public void overrideMethodsForProxy(TypeSpec.Builder typeBuilder){
        /*@Override
        public void clearProperties() {
            getTarget().clearProperties();
        }*/
        MethodSpec.Builder builder_cp = MethodSpec.methodBuilder("clearProperties")
                .returns(TypeName.VOID)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("throw new $T($S)", UnsupportedOperationException.class, "proxy class can't call this method.");
        typeBuilder.addMethod(builder_cp.build());

        // recycle
        typeBuilder.addMethod(MethodSpec.methodBuilder("recycle")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addStatement("throw new $T($S)", UnsupportedOperationException.class, "proxy class can't call this method.")
                .build());
    }
}
