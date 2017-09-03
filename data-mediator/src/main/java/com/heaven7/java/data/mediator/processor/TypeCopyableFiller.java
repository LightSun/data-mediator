package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.FieldData;
import com.heaven7.java.data.mediator.TypeInterfaceFiller;
import com.squareup.javapoet.*;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

import static com.heaven7.java.data.mediator.FieldData.*;
import static com.heaven7.java.data.mediator.processor.Util.NAME_COPYA;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */
public class TypeCopyableFiller extends TypeInterfaceFiller {

    @Override
    public String getInterfaceName() {
        return NAME_COPYA;
    }

    @Override
    public int getInterfaceFlag() {
        return FLAG_COPY;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName, String curClassName,
                                     ExecutableElement ee, MethodSpec.Builder builder, List<FieldData> list) {
        note("start buildMethodStatement --------------");
        TypeName current = TypeVariableName.get(curClassName);
        builder.addStatement("$T result = new $T()", current, current);
        if(list != null && !list.isEmpty()) {
            for (FieldData fd : list) {
                note(" ======= fd = " + fd.getPropertyName());
                builder.addStatement("result.$L = this.$L", fd.getPropertyName(), fd.getPropertyName());
            }
        }
        builder.addStatement("return result");
        note("end buildMethodStatement --------------");
    }
}
