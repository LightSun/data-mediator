package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.Fields;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by heaven7 on 2017/10/25.
 */
public class MultiModuleSuperFieldDelegate implements ISuperFieldDelegate{

    final Elements elements;
    final Types types;
    final ProcessorPrinter pp;

    public MultiModuleSuperFieldDelegate(Elements elements, Types types, ProcessorPrinter pp) {
        this.elements = elements;
        this.types = types;
        this.pp = pp;
    }

    @Override
    public Set<FieldData> getDependFields(TypeElement te) {
        Set<FieldData> list = new HashSet<>();

        List<? extends AnnotationMirror> mirrors = te.getAnnotationMirrors();
        AnnotationMirror expect = null;
        for(AnnotationMirror am : mirrors){
            DeclaredType type = am.getAnnotationType();
            if(type.toString().equals(Fields.class.getName())){
                expect = am;
                break;
            }
        }
        if(expect != null){
            ElementHelper.parseFields(elements, types, expect, list, pp);
        }
        //a depend b, b depend c ,,, etc.
        List<? extends TypeMirror> superInterfaces = te.getInterfaces();
        for(TypeMirror tm : superInterfaces){
            final TypeElement newTe = (TypeElement) ((DeclaredType) tm).asElement();
            list.addAll(getDependFields(newTe)); //recursion
        }
        return list;
    }
}
