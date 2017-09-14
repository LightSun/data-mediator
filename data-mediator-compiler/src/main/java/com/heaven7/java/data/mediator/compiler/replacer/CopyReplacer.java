package com.heaven7.java.data.mediator.compiler.replacer;

import com.heaven7.java.data.mediator.compiler.BaseTypeReplacer;
import com.heaven7.java.data.mediator.compiler.fillers.TypeCopyableFiller;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * indicate the interface (com.heaven7.java.data.mediator.ICopyable.) replacer.
 * Created by heaven7 on 2017/9/13 0013.
 */
public class CopyReplacer extends BaseTypeReplacer {


    @Override
    public TypeName replaceReturnType(String currentPkg, String directParentInterface, String curClassname,
                                      List<? extends TypeMirror> superInterfaces, TypeName superClass, ExecutableElement method) {
        final TypeMirror returnType = method.getReturnType();
        switch (method.getSimpleName().toString()){
            case TypeCopyableFiller.NAME_COPY: {
                switch (returnType.getKind()) {
                    case TYPEVAR: //泛型
                        return  TypeVariableName.get(directParentInterface);

                    default:
                        return TypeName.get(returnType);
                }
            }

            default:
                return TypeName.get(returnType);

        }

    }

  /*  @Override
    public TypeName replaceParameterType(String currentPkg, String directParentInterface, String curClassname,
                                         List<? extends TypeMirror> superInterfaces, TypeName superClass,
                                         ExecutableElement method, VariableElement ve) {
        final TypeMirror asType = ve.asType();
        final TypeName type = TypeName.get(asType);

        switch (method.getSimpleName().toString()){
            case TypeCopyableFiller.NAME_COPY_TO: {
                 if(superClass == null || superInterfaces == null || superInterfaces.isEmpty()){
                     return ClassName.get(currentPkg, directParentInterface);
                 }else{
                     //use parent interface's copyTo. or else may cause problem.
                     for(TypeMirror tm : superInterfaces){
                         FieldData.TypeCompat tc = new FieldData.TypeCompat(getTypes(), tm);
                         tc.replaceIfNeed(getPrinter());
                         TypeName replaceInterfaceTypeName = tc.getReplaceInterfaceTypeName();
                         if(replaceInterfaceTypeName != null){
                             //found
                             return replaceInterfaceTypeName;
                         }
                     }
                 }
            }

        }

        return type;
    }*/
}
