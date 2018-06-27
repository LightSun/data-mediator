package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.GroupDesc;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.GroupProperty;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
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

    public GroupPropertyGenerator(ProcessorContext context) {
        super(context);
    }

    public boolean generate(TypeElement te, List<GroupProperty> gps, TypeElementDelegate delegate) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(getContext().getTargetClassName(te) + GROUP_PROPERTY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(CodeBlock.of(DOC));
        //super class
        TypeElement superClass = getSuperClass(te, delegate);
        if(superClass != null){
            builder.superclass(ClassName.get(superClass));
        }else{
            builder.superclass(ClassName.get(PKG_DM_INTERNAL, SIMPLE_NAME_GPS));
        }
        //constructor
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super()");
        for(GroupProperty gp : gps){
            constructor.addStatement("this.addGroupProperty(createGroupProperty($L, $T.$N, $L, $L, $L))",
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

    private TypeElement getSuperClass(TypeElement te, TypeElementDelegate delegate) {
        TypeMirror superclass = te.getSuperclass();
        String superName = superclass.toString();
        if(superclass instanceof NoType){
            //no super.
        }else if(superName.startsWith("java.") || superName.startsWith("android.")){
            // no super too.
        }else {
            TypeElement newTe = new FieldData.TypeCompat(getTypes(), superclass).getElementAsType();
            TypeElement ele = delegate.get(superName);
            if(ele == null){
                if(hasGroupPropertyAnnotation(newTe)){
                    return newTe;
                }
                return getSuperClass(newTe, delegate);
            }else {
                return newTe;
            }
        }
        return null;
    }

    private boolean hasGroupPropertyAnnotation(TypeElement te) {
        Fields fields = te.getAnnotation(Fields.class);
        if(fields == null){
            return false;
        }
        return fields.groups().length > 0;
    }

    public interface TypeElementDelegate{
        TypeElement get(String qualifyName);
        ProcessorContext getContext();
    }

}
