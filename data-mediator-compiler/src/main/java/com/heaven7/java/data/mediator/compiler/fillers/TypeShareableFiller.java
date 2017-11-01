package com.heaven7.java.data.mediator.compiler.fillers;

import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.TypeInterfaceFiller;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */
public class TypeShareableFiller extends TypeInterfaceFiller {

    @Override
    public String getInterfaceName() {
        return DataMediatorConstants.NAME_SHARE;
    }

    @Override
    public int getInterfaceFlag() {
        return FieldData.FLAG_SHARE;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName,
                                     String curClassName, ExecutableElement ee,
                                     MethodSpec.Builder builder, List<FieldData> list, boolean hasSuperClass, int superFlagsForParent) {
        note("start buildMethodStatement --------------");
        if(hasSuperClass && (superFlagsForParent & getInterfaceFlag()) != 0){
            builder.addStatement("super.clearShare()");
        }
        if(list != null && !list.isEmpty()) {
            for (FieldData fd : list) {
                Util.addInitStatement(fd, builder);
            }
        }
        note("end buildMethodStatement --------------");
    }

    @Override
    public void buildProxyMethod(MethodSpec.Builder builder, ExecutableElement ee, ClassName cn_interface) {
          builder.addStatement("_getTarget().clearShare()");
    }

}
