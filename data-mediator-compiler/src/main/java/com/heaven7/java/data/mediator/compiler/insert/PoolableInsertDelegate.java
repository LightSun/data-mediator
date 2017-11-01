package com.heaven7.java.data.mediator.compiler.insert;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import java.util.Collection;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.PKG_PROP;
import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.SIMPLE_NAME_DATA_POOL;
import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.SIMPLE_NAME_POOLABLE;

/**
 * DataPools.Poolable
 * Created by heaven7 on 2017/9/28 0028.
 */
/*public*/ class PoolableInsertDelegate extends TypeInsertDelegate {

    @Override
    public boolean addStaticCode(CodeBlock.Builder staticBuilder, Object maxCount){
        final int maxPoolCount = (int) maxCount;
        String packageName = getClassInfo().getPackageName();
        String className = getClassInfo().getCurrentClassname();
        if(maxPoolCount > 0 ) {
            staticBuilder.add("$T.preparePool($S, $L);\n",
                    ClassName.get(PKG_PROP, SIMPLE_NAME_DATA_POOL),
                    packageName + "." + className, maxPoolCount);
            return true;
        }
        return false;
    }
    @Override
    public void addSuperInterface(TypeSpec.Builder typeBuilder){
        typeBuilder.addSuperinterface(ClassName.get(PKG_PROP, SIMPLE_NAME_DATA_POOL, SIMPLE_NAME_POOLABLE));
    }

    @Override
    public void overrideMethodsForImpl(TypeSpec.Builder typeBuilder, Collection<FieldData> fields) {
        final boolean hasSuperClass = getClassInfo().getSuperClass() != null;
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

    @Override
    public void overrideMethodsForProxy(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){
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
