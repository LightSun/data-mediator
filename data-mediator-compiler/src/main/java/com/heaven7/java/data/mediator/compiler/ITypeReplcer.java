package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/12 0012.
 */
public interface ITypeReplcer {

    /**
     * replace type if need.
     * @param currentPkg current package
     * @param directParentInterface the main direct parent interface.
     * @param curClassname  the current class name. (simple name.)
     * @param superInterfaces super interfaces of current class, which to generate .java file.
     * @param method the method of
     * @return
     */
    TypeName replaceReturn(String currentPkg, String directParentInterface, String curClassname ,
                           List<? extends TypeMirror> superInterfaces , ExecutableElement method);


    VariableElement replaceParam(String currentPkg, String directParentInterface, String curClassname ,
                                 List<? extends TypeMirror> superInterfaces , ExecutableElement method);
}
