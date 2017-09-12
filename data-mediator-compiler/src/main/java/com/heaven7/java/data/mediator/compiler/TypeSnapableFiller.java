package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.Util.getInitValue;

/**
 * Created by heaven7 on 2017/9/1 0001.
 */
/*public*/ class TypeSnapableFiller extends TypeResetableFiller {

    @Override
    public String getInterfaceName() {
        return Util.NAME_SNAP;
    }

    @Override
    public int getInterfaceFlag() {
        return FieldData.FLAG_SNAP;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName,
                                     String curClassName, ExecutableElement ee,
                                     MethodSpec.Builder builder, List<FieldData> list, boolean hasSuperClass) {
        note("start buildMethodStatement --------------", list);
        if(hasSuperClass){
            builder.addStatement("super.clearSnap()");
        }
        if(list != null && !list.isEmpty()) {
            for (FieldData fd : list) {
                builder.addStatement("this.$L = $L", fd.getPropertyName(),
                        getInitValue(fd));
            }
        }
        note("end buildMethodStatement --------------");
    }

}
