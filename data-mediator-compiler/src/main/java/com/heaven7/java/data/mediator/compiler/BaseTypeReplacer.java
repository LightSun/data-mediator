package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;

/**
 * the type replacer
 * Created by heaven7 on 2017/9/12 0012.
 */
public abstract class BaseTypeReplacer {

    private Types mTypes;
    private ProcessorPrinter mPrinter;

    public Types getTypes() {
        return mTypes;
    }
    public void setTypes(Types mTypes) {
        this.mTypes = mTypes;
    }
    public ProcessorPrinter getPrinter() {
        return mPrinter;
    }
    public void setPrinter(ProcessorPrinter mPrinter) {
        this.mPrinter = mPrinter;
    }

    public void reset(){
        mTypes = null;
        mPrinter = null;
    }

    /**
     * replace return type if need.
     * @param currentPkg current package
     * @param directParentInterface the main direct parent interface.
     * @param curClassname  the current class name. (simple name.)
     * @param superInterfaces super interfaces of current class, which to generate .java file.
     * @param superClass super class,  null means no super class.
     * @param method the method of
     * @return the new return type . should not be null.
     */
    public TypeName replaceReturnType(String currentPkg, String directParentInterface, String curClassname ,
                           List<? extends TypeMirror> superInterfaces , TypeName superClass,
                                               ExecutableElement method){
        return  TypeName.get( method.getReturnType());
    }


    /**
     * replace parameter type
     * @param currentPkg the current package name
     * @param directParentInterface the main direct parent interface
     * @param curClassname  the current class name. (simple name.)
     * @param superInterfaces  super interfaces of current class, which to generate .java file.
     * @param superClass super class. null means no super class.
     * @param method the method.
     * @param ve the variable element
     * @return the new parameter type . should not be null.
     */
    public  TypeName replaceParameterType(String currentPkg, String directParentInterface, String curClassname ,
                                 List<? extends TypeMirror> superInterfaces , TypeName superClass,
                          ExecutableElement method, VariableElement ve){
        return  TypeName.get(ve.asType());
    }
}
