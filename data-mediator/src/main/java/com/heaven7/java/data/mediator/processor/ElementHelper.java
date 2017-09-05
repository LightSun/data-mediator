package com.heaven7.java.data.mediator.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * an element info contains the weight of the element.
 * Created by heaven7 on 2017/9/5 0005.
 */
public class ElementHelper {

    private final TypeElement mElement;
    private int mWeight;

    public ElementHelper(TypeElement mElement) {
        this.mElement = mElement;
    }

    public TypeElement getElement() {
        return mElement;
    }
    public int getWeight() {
        return mWeight;
    }
    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    public void  processWeight(){
        List<? extends AnnotationMirror> annoMirrors = mElement.getAnnotationMirrors();
        for(AnnotationMirror am : annoMirrors){
          //  am.get
        }
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();

    }

   /*private static boolean isValidAnnotation(AnnotationMirror am){
        am.getAnnotationType().getTypeArguments()
        if (!(element instanceof QualifiedNameable)) {
            error("annotation element not instanceof QualifiedNameable)");
            return false;
        }
        if (!TARGET_PACKAGE.equals(((QualifiedNameable) element).getQualifiedName().toString())) {
            return false;
        }
    }*/

    @Override
    public String toString() {
        return "ElementHelper{" +
                "mElement=" + mElement +
                ", mWeight=" + mWeight +
                '}';
    }
}
