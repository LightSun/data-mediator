package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.module.FamilyDescData;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * the family group generator
 * @author heaven7
 * @since 1.4.5
 */
public class FamilyGroupGenerator extends BaseGenerator {

    public FamilyGroupGenerator(ProcessorContext context) {
        super(context);
    }

    public boolean generate(TypeElement inter, String proxyName, List<FamilyDescData> fdds,
                            GroupPropertyGenerator.TypeElementDelegate delegate){
        final String packageName = getElements().getPackageOf(inter).getQualifiedName().toString();

       // ClassName cn_context = ClassName.get(packageName, proxyName, SN_FGS);
        TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(inter) + FAMILY_PROPERTY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(CodeBlock.of(DOC));

        //super class
        TypeElement superInter = getSuperInterface(inter, delegate);
        if(superInter != null){
            final String pkg = getElements().getPackageOf(superInter).getQualifiedName().toString();
            final ClassName cn = ClassName.get(pkg,
                    getContext().getTargetClassName(superInter) + FAMILY_PROPERTY_SUFFIX);
            builder.superclass(cn);
        }else{
            builder.superclass(ClassName.get(PKG_PROP, SN_FAMILY_MANAGER, DataMediatorConstants.SN_FGS));
        }
        //constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()");
        ClassName cn_familyM = ClassName.get(PKG_PROP, SN_FAMILY_MANAGER);
        for(FamilyDescData fdd : fdds){
            List<Object> args = new ArrayList<>();
            args.add(cn_familyM);
            args.add(fdd.getType());

            String template_master = buildMasterArrayTemplate(inter, fdd, args);
            String template = "$T.createFamilyGroup((byte)$L, " + template_master + ", ";
            if(fdd.hasSlave()){
                String template_slave = buildSlaveArrayTemplate(inter, fdd, args);
                template += template_slave + ", ";
            }
            args.add(fdd.getConnectClass().getTypeName());
            Object[] params = args.toArray(new Object[args.size()]);

            constructor.addStatement(template + "new $T())", params);
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

    @Override
    protected boolean hasAnnotation(TypeElement te) {
        Fields fields = te.getAnnotation(Fields.class);
        if(fields == null){
            return false;
        }
        return fields.families().length > 0;
    }

    private static String buildMasterArrayTemplate(TypeElement inter, FamilyDescData fdd, List<Object> args) {
        ClassName cn_prop = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        StringBuilder sb = new StringBuilder();
        sb.append("new $T[]{");
        args.add(cn_prop);

        ClassName cn_inter = ClassName.get(inter);
        for (int i =0 ; i < fdd.getMaster().length ; i ++){
            sb.append("$T.$N");
            args.add(cn_inter);
            args.add(PREFIX_PROP + fdd.getMaster()[i]);
            if( i != fdd.getMaster().length - 1){
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    private static String buildSlaveArrayTemplate(TypeElement inter, FamilyDescData fdd, List<Object> args) {
        ClassName cn_prop = ClassName.get(PKG_PROP, SIMPLE_NAME_PROPERTY);
        StringBuilder sb = new StringBuilder();
        sb.append("new $T[]{");
        args.add(cn_prop);

        ClassName cn_inter = ClassName.get(inter);
        for (int i =0 ; i < fdd.getSlave().length ; i ++){
            sb.append("$T.$N");
            args.add(cn_inter);
            args.add(PREFIX_PROP + fdd.getSlave()[i]);
            if( i != fdd.getSlave().length - 1){
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
