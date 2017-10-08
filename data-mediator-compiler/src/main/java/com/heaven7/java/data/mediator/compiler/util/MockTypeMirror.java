package com.heaven7.java.data.mediator.compiler.util;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.lang.annotation.Annotation;
import java.util.List;

public class MockTypeMirror implements TypeMirror {

    private final String typeName;

    public MockTypeMirror(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.DECLARED;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return null;
    }

    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        return null;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return null;
    }

    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        return null;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
