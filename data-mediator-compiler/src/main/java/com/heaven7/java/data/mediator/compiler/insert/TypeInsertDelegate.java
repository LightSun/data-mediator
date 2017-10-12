package com.heaven7.java.data.mediator.compiler.insert;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.TypeSpec;

import java.util.Collection;

/**
 * Created by heaven7 on 2017/9/28 0028.
 */
public abstract class TypeInsertDelegate {

    private TargetClassInfo mClassInfo;

    public TargetClassInfo getClassInfo() {
        return mClassInfo;
    }
    public void setClassInfo(TargetClassInfo mClassInfo) {
        this.mClassInfo = mClassInfo;
    }

    /**
     * add annotation for current class.
     * @param typeBuilder the builder
     */
    public void addClassAnnotation(TypeSpec.Builder typeBuilder){

    }
    /**
     * add static code
     * @param typeBuilder the class builder
     * @param param the extra param
     */
    public void addStaticCode(TypeSpec.Builder typeBuilder, Object param){

    }

    /**
     * add constructor code
     * @param typeBuilder the class builder
     * @param fields the all fields use in constructor
     */
    public void addConstructor(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){

    }

    /**
     * add super interface
     * @param typeBuilder the type builder
     */
    public void addSuperInterface(TypeSpec.Builder typeBuilder){

    }
    /**
     * override method for proxy class
     * @param typeBuilder the type builder
     * @param fields the fields
     */
    public void overrideMethodsForProxy(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){

    }
    /**
     * override method for impl class
     * @param typeBuilder the type builder
     * @param fields the fields
     */
    public void overrideMethodsForImpl(TypeSpec.Builder typeBuilder, Collection<FieldData> fields){

    }
}
