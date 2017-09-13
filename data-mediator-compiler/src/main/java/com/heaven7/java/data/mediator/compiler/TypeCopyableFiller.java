package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.FieldData.FLAG_COPY;
import static com.heaven7.java.data.mediator.compiler.Util.NAME_COPYA;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */
public class TypeCopyableFiller extends TypeInterfaceFiller {

    public static final String  NAME_COPY     = "copy";
    public static final String  NAME_COPY_TO  = "copyTo";

    @Override
    public String getInterfaceName() {
        return NAME_COPYA;
    }

    @Override
    public int getInterfaceFlag() {
        return FLAG_COPY;
    }

    //void copyTo(T out);
    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName, String curClassName,
                                     ExecutableElement ee, MethodSpec.Builder builder, List<FieldData> list,
                                     boolean hasSuperClass) {
        final String method = "buildMethodStatement";
        note(method, "start  --------------");

        ClassName parentInterface = ClassName.get(curPkg, parentInterfaceName);
        final TypeName current = TypeVariableName.get(curClassName);
        switch (ee.getSimpleName().toString()){
            /**
            public GoodStudent copy() {
            GoodStudent gs = new GoodStudent();
            copyTo(gs);
            return gs;
            }
             */
            case NAME_COPY:
                builder.addStatement("$T result = new $T()", current, current)
                        .addStatement("this.copyTo(result)")
                        .addStatement("return result");
                break;

            /** //IStudent come from super.
             * public void copyTo(IStudent out) {
                 super.copyTo(out);
                 if(out instanceof GoodStudent){
                     GoodStudent gs = (GoodStudent) out;
                     gs.setThinking(getThinking());
                 }
             }
             */
            case NAME_COPY_TO:
                final String paramName = "out";
                if(hasSuperClass){
                    builder.addStatement("super.copyTo($N)", paramName);
                }

                builder.beginControlFlow("if($N instanceof $T)", paramName, parentInterface)
                        .addStatement("$T result = ($T)$N", parentInterface, parentInterface, paramName);
                if(list != null && !list.isEmpty()) {
                    for (FieldData fd : list) {
                        note(method," ======= fd = " + fd.getPropertyName());
                        final String nameForMethod = Util.getPropNameForMethod(fd);
                        final String setMethodName = BaseMemberBuilder.SET_PREFIX + nameForMethod;
                        final String getMethodName = BaseMemberBuilder.GET_PREFIX + nameForMethod;
                        builder.addStatement("result.$N(this.$N())", setMethodName, getMethodName);
                    }
                }
                builder.endControlFlow();
                break;

            default:
                note("");
        }
      /*  TypeName current = TypeVariableName.get(curClassName);
        builder.addStatement("$T result = new $T()", current, current);
        if(list != null && !list.isEmpty()) {
            for (FieldData fd : list) {
                note(" ======= fd = " + fd.getPropertyName());
                builder.addStatement("result.$L = this.$L", fd.getPropertyName(), fd.getPropertyName());
            }
        }
        builder.addStatement("return result");*/
        note(method, "end  --------------");
    }
}
/**
    T copy();
    void copyTo(T out);
 */
