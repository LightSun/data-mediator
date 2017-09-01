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
public class TypeCopyableFiller extends TypeInterfaceFiller{

    @Override
    protected String getInterfaceName() {
        return NAME_COPYA;
    }
    @Override
    protected int getInterfaceFlag() {
        return FLAG_COPY;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName,  String curClassName,
                                     ExecutableElement ee, MethodSpec.Builder builder, List<FieldData> list) {
        TypeName interfaceTN = ClassName.get(curPkg, parentInterfaceName);
        //TODO
        TypeName current = TypeVariableName.get(curClassName);
        for(FieldData fd : list){
            builder.addStatement("$T result = new $T()", interfaceTN, current);
        }
    }
}
