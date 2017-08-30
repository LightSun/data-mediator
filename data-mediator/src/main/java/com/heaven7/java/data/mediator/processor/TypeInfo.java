package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.TypeName;

/**
 * Created by heaven7 on 2017/8/30.
 */
public class TypeInfo {
    TypeName typeName; //type name
    String paramName;  //parameter name

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
}
