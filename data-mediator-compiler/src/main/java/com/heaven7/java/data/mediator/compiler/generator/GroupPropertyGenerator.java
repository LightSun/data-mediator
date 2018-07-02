package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.GroupProperty;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 *
 * group property generator for @GroupDesc
 * @author heaven7
 * @since 1.4.5
 */
public class GroupPropertyGenerator extends BaseGenerator {

    public static final String GROUP = "groups";

    public GroupPropertyGenerator(ProcessorContext context) {
        super(context);
    }

    //te is interface
    public boolean generate(TypeElement te, List<GroupProperty> gps, TypeElementDelegate delegate) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(te) + GROUP_PROPERTY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(CodeBlock.of(DOC));
        //super class
        TypeElement superClass = getSuperInterface(te, delegate);
        if(superClass != null){
            final String pkg = getElements().getPackageOf(superClass).getQualifiedName().toString();
            final ClassName cn = ClassName.get(pkg,
                    getContext().getTargetClassName(superClass) + GROUP_PROPERTY_SUFFIX);
            builder.superclass(cn);
        }else{
            builder.superclass(ClassName.get(PKG_PROP, SIMPLE_NAME_GPS));
        }
        //constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()");
        for(GroupProperty gp : gps){
            constructor.addStatement("this.addGroupProperty(createGroupProperty((byte)$L, $T.$N, $L, $L, $L))",
                    gp.getType(), ClassName.get(te), PREFIX_PROP + gp.getProp(),
                    gp.getFocusVal(), gp.getOppositeVal(), gp.isAsFlags());
        }
        builder.addMethod(constructor.build());
        //generate java file
        final String packageName = getElements().getPackageOf(te).getQualifiedName().toString();
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
