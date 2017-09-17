package com.heaven7.java.data.mediator.compiler.fillers;

import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.TypeInterfaceFiller;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.Util.getInitValue;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */

public class TypeResetableFiller extends TypeInterfaceFiller {

    @Override
    public String getInterfaceName() {
        return DataMediatorConstants.NAME_RESET;
    }
    @Override
    public int getInterfaceFlag() {
        return FieldData.FLAG_RESET;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName,
                                     String curClassName, ExecutableElement ee,
                                     MethodSpec.Builder builder, List<FieldData> list, boolean hasSuperClass, int superFlagsForParent) {
        note("start buildMethodStatement --------------");
        if(hasSuperClass && (superFlagsForParent & getInterfaceFlag()) != 0){
            builder.addStatement("super.reset()");
        }
        if(list != null && !list.isEmpty()) {
            for (FieldData fd : list) {
                builder.addStatement("this.$L = $L", fd.getPropertyName(),
                        getInitValue(fd));
            }
        }
        note("end buildMethodStatement --------------");
    }

    @Override
    public void buildProxyMethod(MethodSpec.Builder builder, ExecutableElement ee, ClassName cn_interface) {
        builder.addStatement("getTarget().reset()");
    }
}
