package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.DataBindingInfo;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.util.TypeUtils;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import java.io.IOException;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/11/5.
 * @since 1.4.0
 */
public class DataBindingGenerator extends BaseGenerator {

    public DataBindingGenerator(ProcessorContext context) {
        super(context);
    }

    public boolean generate(TypeElement element, DataBindingInfo info){
        final ClassName cn_target = ClassName.get(element);
        final TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(element) + DATA_BINDING_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(CodeBlock.of(DOC))
                .addTypeVariable(TypeVariableName.get("T", TypeName.OBJECT, cn_target));
        //super class
        final TypeElement superClass = info.getSuperClass();
        final ParameterizedTypeName realSuper_tn;
        final ClassName cn_data_binding = ClassName.get(PKG_PROP, SN_DATA_BINDING);
        if(superClass != null){
            final String packageName = getElements().getPackageOf(superClass).getQualifiedName().toString();
            realSuper_tn = ParameterizedTypeName.get(
                    ClassName.get(packageName, getContext().getTargetClassName(superClass) + DATA_BINDING_SUFFIX),
                    TypeVariableName.get("T")
            );
        }else{
            realSuper_tn = ParameterizedTypeName.get(
                    cn_data_binding,
                    TypeVariableName.get("T")
            );
        }
        builder.superclass(realSuper_tn);

         //constructor
        final MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter( ClassName.get("","T"), "target")
                .addStatement("super(target)");

        // add BindInfos
        boolean defineBindInfo = false;
        for (DataBindingInfo.BindInfo bi : info.getBindInfos()){
            if(defineBindInfo){
                constructor.addStatement("bi = $T.createBindInfo(target.$N, $S, $L, $S)", cn_data_binding,
                        bi.fieldViewName, bi.propName, bi.index, bi.methodName);
            }else{
                constructor.addStatement("$T.BindInfo bi = $T.createBindInfo(target.$N, $S, $L, $S)", cn_data_binding, cn_data_binding,
                        bi.fieldViewName, bi.propName, bi.index, bi.methodName);
                defineBindInfo = true;
            }
            //types
            constructor.addStatement("bi.typeCount($L)", bi.methodTypes.size());
            for(String type : bi.methodTypes){
                constructor.addStatement("bi.addType($T.class)", TypeUtils.getTypeName(type));
            }
            //extras
            if(bi.extras != null && bi.extras.length > 0){
                constructor.addStatement("bi.extraValueCount($L)", bi.extras.length);
                for(Object val : bi.extras){
                    constructor.addStatement("bi.addExtraValue($L)", val);
                }
            }
            constructor.addStatement("addBindInfo(bi)");
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

        //generate java file
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
