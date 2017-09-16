package com.heaven7.java.data.mediator.compiler.fillers;

import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.TypeInterfaceFiller;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/9.
 */
public class TypeSerializableFiller extends TypeInterfaceFiller {
    @Override
    public String getInterfaceName() {
        return DataMediatorConstants.NAME_SERIALIZABLE;
    }

    @Override
    public int getInterfaceFlag() {
        return 0;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName,
                                     String curClassName, ExecutableElement ee,
                                     MethodSpec.Builder builder,
                                     List<FieldData> list, boolean hasSuperClass, int superFlagsForParent) {
        // don't need
    }

    @Override
    public FieldSpec.Builder[] createFieldBuilder(String pkgName, String interName,
                                                  String classname, List<FieldData> datas) {
        final FieldSpec.Builder builder = FieldSpec.builder(long.class, "serialVersionUID",
                Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer(" 1L");
        return new FieldSpec.Builder []{ builder };
    }

    @Override
    public void buildProxyMethod(MethodSpec.Builder builder, ExecutableElement ee, ClassName cn_interface) {
        // no need
    }
}
