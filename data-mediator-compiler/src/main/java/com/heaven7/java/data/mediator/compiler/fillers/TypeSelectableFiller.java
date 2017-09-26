package com.heaven7.java.data.mediator.compiler.fillers;

import com.heaven7.java.data.mediator.compiler.DataMediatorConstants;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.TypeInterfaceFiller;
import com.heaven7.java.data.mediator.compiler.Util;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**   'com.heaven7.adapter.ISelectable' as property 'selected'.
 * Created by heaven7 on 2017/9/26 0026.
 */
public class TypeSelectableFiller extends TypeInterfaceFiller {

    public static final String NAME_setSelected  = "setSelected";
    public static final String NAME_isSelected   = "isSelected";

    @Override
    public String getInterfaceName() {
        return NAME_SELECTABLE;
    }

    @Override
    public int getInterfaceFlag() {
        return FieldData.FLAG_SELECTABLE;
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName, String curClassName,
                                     ExecutableElement ee, MethodSpec.Builder builder, List<FieldData> list,
                                     boolean hasSuperClass, int superInterfaceFlagsForParent) {
        //if parent handled.
         if(Util.hasFlag(superInterfaceFlagsForParent, FieldData.FLAG_SELECTABLE)){
             return;
         }
         switch (ee.getSimpleName().toString()){
             case NAME_setSelected:
                 builder.addStatement("this.$N = selected", FD_SELECTABLE.getPropertyName());
                 break;

             case NAME_isSelected:
                 builder.addStatement("return this.$N", FD_SELECTABLE.getPropertyName());
                 break;
         }

    }

    @Override
    public FieldSpec.Builder[] createFieldBuilder(String pkgName, String interName, String classname,
                                                  List<FieldData> datas, int superInterfaceFlagsForParent) {
        //if parent handled.
        if(Util.hasFlag(superInterfaceFlagsForParent, FieldData.FLAG_SELECTABLE)){
            return null;
        }
        return new FieldSpec.Builder[]{
                FieldSpec.builder(TypeName.BOOLEAN, FD_SELECTABLE.getPropertyName(), Modifier.PRIVATE),
        };
    }

    @Override
    public void buildProxyMethod(MethodSpec.Builder builder, ExecutableElement ee, ClassName cn_interface) {
        // do proxy method in ProxyGenerator
    }
}
