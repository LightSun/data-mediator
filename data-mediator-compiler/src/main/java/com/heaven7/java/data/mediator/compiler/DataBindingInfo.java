package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Created by heaven7 on 2017/11/5.
 */
public class DataBindingInfo {
    private final HashSet<BindInfo> binds = new HashSet<>();
    private TypeMirror binderClass;
    private TypeMirror binderFactoryClass;
    private TypeMirror bindMethodSupplier;
    private TypeElement mSuperClass;

    public TypeName getBinderClass() {
        return binderClass == null ? null :TypeName.get(binderClass);
    }
    public void setBinderClass(TypeMirror binderClass) {
        this.binderClass = binderClass;
    }
    public TypeName getBinderFactoryClass() {
        return binderFactoryClass == null ? null :TypeName.get(binderFactoryClass);
    }
    public void setBinderFactoryClass(TypeMirror binderFactoryClass) {
        this.binderFactoryClass = binderFactoryClass;
    }
    public void addBindInfo( BindInfo info){
        binds.add(info);
    }
    public HashSet<BindInfo> getBindInfos() {
        return binds;
    }
    public TypeElement getSuperClass(){
        return mSuperClass;
    }

    public void setSuperClass(TypeElement superClass) {
        this.mSuperClass = superClass;
    }

    public void setBindMethodSupplier(TypeMirror tm) {
        this.bindMethodSupplier = tm;
    }
    public TypeName getBindMethodSupplier() {
        return bindMethodSupplier == null ? null : TypeName.get(bindMethodSupplier);
    }

    public static class BindMethodInfo{
        public String name;
        //default types is String.class, Object.class
        public final List<String> types = new ArrayList<>();
        public BindMethodInfo() {
            types.add(String.class.getName());
            types.add(Object.class.getName());
        }
    }

    public static class BindInfo {
        public final String fieldViewName;
        public String propName;
        public int index;

        public final String methodName;
        public final List<String> methodTypes;
        public Object[] extras;

        BindInfo(String fieldViewName, String methodName, List<String> types) {
            this.fieldViewName = fieldViewName;
            this.methodName = methodName;
            this.methodTypes = types;
        }
        BindInfo(String fieldViewName, String propName, int index, String methodName, List<String> types) {
            this(fieldViewName, methodName, types);
            this.propName = propName;
            this.index = index;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }
        public void setIndex(int index) {
            this.index = index;
        }

        public void setExtras(Object[] extras) {
            this.extras = extras;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BindInfo bindInfo = (BindInfo) o;
            return Objects.equals(fieldViewName, bindInfo.fieldViewName) &&
                    Objects.equals(propName, bindInfo.propName);
        }

        @Override
        public int hashCode() {
            return  Arrays.hashCode(new Object[]{fieldViewName, propName});
        }

        public boolean isValid() {
            return propName != null && !propName.isEmpty();
        }
    }
}
/**
 void addBindInfo(Object view, String propName, int index,String methodName, Class<?>[] methodTypes) {
 */
