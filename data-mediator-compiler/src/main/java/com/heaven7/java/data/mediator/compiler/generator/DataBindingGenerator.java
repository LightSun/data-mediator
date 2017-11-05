package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.DataBindingInfo;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;

import java.io.IOException;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/11/5.
 */
public class DataBindingGenerator extends BaseGenerator {

    public DataBindingGenerator(ProcessorContext context) {
        super(context);
    }

    public boolean generate(TypeElement element, DataBindingInfo info){
        final String className = element.getQualifiedName().toString();
        final ClassName cn_target = ClassName.get(element);
        final TypeSpec.Builder builder = TypeSpec.classBuilder(className + DATA_BINDING_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", TypeName.OBJECT, cn_target));
        //super class
        final TypeMirror superclass = element.getSuperclass();
        final ParameterizedTypeName realSuper_tn;
        if(!(superclass instanceof NoType)){
           // ParameterizedTypeName.get()get("T");
            FieldData.TypeCompat tc = new FieldData.TypeCompat(getTypes(), superclass);
            realSuper_tn = ParameterizedTypeName.get(
                    ClassName.get(tc.getElementAsType()),
                    ClassName.get("","T")
            );
        }else{
            realSuper_tn = ParameterizedTypeName.get(
                    ClassName.get(PKG_PROP, SN_DATA_BINDING),
                    ClassName.get("","T")
            );
        }
        builder.superclass(realSuper_tn);
         //constructor
        final MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(cn_target, "target")
                .addStatement("super(target)");
        final ClassName cn_internal_util = ClassName.get(PKG_DM_INTERNAL, SN_INTERNAL_UTILS);
        // void addBindInfo(Object view, String propName, int index, String methodName, Class<?>[] methodTypes)
        for (DataBindingInfo.BindInfo bi : info.getBindInfos()){
            constructor.addStatement("addBindInfo(target,$N, $N, $L, $N, $T.convert2Classes($N))",
                    bi.fieldViewName, bi.propName, bi.index,
                    bi.methodName, cn_internal_util, bi.methodTypes);
        }
        final TypeName binderClass = info.getBinderClass();
        if(binderClass != null){
            constructor.addStatement("setBinderClass($T.class)", binderClass);
        }
        final TypeName binderFactoryClass = info.getBinderFactoryClass();
        if(binderFactoryClass != null){
            constructor.addStatement("setBinderFactory(new $T())", binderFactoryClass);
        }
        builder.addMethod(constructor.build());

        final String packageName = getElements().getPackageOf(element).getQualifiedName().toString();
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
}
