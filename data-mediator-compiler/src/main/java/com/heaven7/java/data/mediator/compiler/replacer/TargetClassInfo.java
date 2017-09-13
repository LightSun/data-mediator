package com.heaven7.java.data.mediator.compiler.replacer;

import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * the target class info to generate class.
 * Created by heaven7 on 2017/9/13 0013.
 */
public class TargetClassInfo {

    private String packageName;
    private String directParentInterfaceName;
    private String currentClassname;
    private  List<? extends TypeMirror> superInterfaces;
    private TypeName superClass;

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDirectParentInterfaceName() {
        return directParentInterfaceName;
    }
    public void setDirectParentInterfaceName(String directParentInterfaceName) {
        this.directParentInterfaceName = directParentInterfaceName;
    }

    public String getCurrentClassname() {
        return currentClassname;
    }
    public void setCurrentClassname(String currentClassname) {
        this.currentClassname = currentClassname;
    }

    public List<? extends TypeMirror> getSuperInterfaces() {
        return superInterfaces;
    }
    public void setSuperInterfaces(List<? extends TypeMirror> superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public TypeName getSuperClass() {
        return superClass;
    }
    public void setSuperClass(TypeName superClass) {
        this.superClass = superClass;
    }
}
