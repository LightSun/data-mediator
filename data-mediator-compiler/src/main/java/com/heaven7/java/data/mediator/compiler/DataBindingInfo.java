package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by heaven7 on 2017/11/5.
 */
public class DataBindingInfo {
    private TypeMirror binderClass;
    private TypeMirror binderFactoryClass;
    private final HashSet<BindInfo> binds = new HashSet<>();

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
    public static class BindInfo {
        public String fieldViewName;
        public final String propName;
        public final int index;

        public final String methodName;
        public final List<String> methodTypes;

        BindInfo(String view, String propName, int index, String methodName, List<String> tms) {
            this.fieldViewName = view;
            this.methodName = methodName;
            this.propName = propName;
            this.index = index;
            this.methodTypes = tms;
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
    }
}
/**
 void addBindInfo(Object view, String propName, int index,String methodName, Class<?>[] methodTypes) {
 */
