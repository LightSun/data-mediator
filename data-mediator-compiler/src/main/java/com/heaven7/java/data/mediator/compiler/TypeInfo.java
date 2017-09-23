package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.TypeName;

/**
 * Created by heaven7 on 2017/8/30.
 */
/*public*/ class TypeInfo {
    TypeName typeName; //type name. may be list .array
    String paramName;  //parameter name
    TypeName simpleTypeName;

    public TypeInfo() {
    }
    public TypeName getTypeName() {
        return typeName;
    }
    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    public TypeName getSimpleTypeNameBoxed() {
        return simpleTypeName.box();
    }
    public TypeName getSimpleTypeName() {
        return simpleTypeName;
    }
    public void setSimpleTypeName(TypeName rawTypeName) {
        this.simpleTypeName = rawTypeName;
    }

}
