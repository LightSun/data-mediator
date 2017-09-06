package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.FieldData;

import javax.lang.model.element.*;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.heaven7.java.data.mediator.processor.Util.*;

/**
 * an element info contains the weight of the element.
 * Created by heaven7 on 2017/9/5 0005.
 */
public class ElementHelper {

    private static final String TAG = "ElementHelper";
    private static final String TARGET_PACKAGE = "com.heaven7.java.data.mediator";
    private static final String KEY_FIELDS_ANNO = "value";

    /**
     * the depend map: key-value is annotated class name - field class name
     */
    static final Map<String, List<String>> sDependFieldMap = new ConcurrentHashMap<>();
    /**
     * the depend map: key-value is annotated class name - super class/interface name
     */
    static final Map<String, List<String>> sDependSuperMap = new ConcurrentHashMap<>();

    private final TypeElement mElement;
    private final Elements mElements;
    private final List<FieldData> mFieldDatas;
    private int mWeight;

    public ElementHelper(Elements mElements, TypeElement mElement) {
        this.mElements = mElements;
        this.mElement = mElement;
        this.mFieldDatas = new ArrayList<>();
    }

    public static void calculateWeight(){

    }
    public static void sort(){

    }

    public TypeElement getElement() {
        return mElement;
    }
    public int getWeight() {
        return mWeight;
    }
    public boolean preprocess(ProcessorPrinter pp) {
        List<? extends AnnotationMirror> annoMirrors = mElement.getAnnotationMirrors();
        if (!processAnnotation(pp, annoMirrors))
            return false;
        List<? extends TypeMirror> interfaces = mElement.getInterfaces();
        for(TypeMirror tm : interfaces){
            addToFieldDependIfNeed(tm, sDependSuperMap);
        }
        return true;
    }

    private boolean processAnnotation(ProcessorPrinter pp, List<? extends AnnotationMirror> annoMirrors) {
        final String methodName = "processAnnotation";
        //@Fields
        for (AnnotationMirror am : annoMirrors) {
            if (!isValidAnnotation(am, pp)) {
                return false;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
            pp.note(TAG, methodName, "am.getElementValues() = map . is " + map);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
                ExecutableElement key = en.getKey();
                pp.note(TAG, methodName, "key: " + key);//the method of annotation
                if (key.getSimpleName().toString().equals(KEY_FIELDS_ANNO)) {
                    //get all @Field(...)
                    AnnotationValue value = en.getValue();
                    Object target = value.getValue();
                    if (target == null || !(target instanceof List)) {
                        pp.error(TAG, methodName, "@Fields's value() must be a list.");
                        return false;
                    }
                    List list = (List) target;
                    if (list.isEmpty()) {
                        pp.error(TAG, methodName, "@Fields's value() must have value list.");
                        return false;
                    }
                    Object obj = list.get(0);
                    if (!(obj instanceof AnnotationMirror)) {
                        pp.error(TAG, methodName, "@Fields's value() must have list of @Field.");
                        return false;
                    }
                    if (!iterateField((List<? extends AnnotationMirror>) list, pp)) {
                        return false;
                    }
                } else {
                    //do other. current not use
                }
            }
        }
        return true;
    }

    private boolean iterateField(List<? extends AnnotationMirror> list, ProcessorPrinter pp) {
        final String methodName = "iterateField";
        pp.note("=================== start iterateField() ====================");
        for (AnnotationMirror am1 : list) {
            if (!isValidAnnotation(am1, pp)) {
                return false;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am1.getElementValues();

            FieldData data = new FieldData();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {

                ExecutableElement key = en.getKey();
                AnnotationValue av = en.getValue();
                pp.note(TAG, methodName, "test --->  " + av.getValue());

                switch (key.getSimpleName().toString()) {
                    case STR_PROP_NAME:
                        data.setPropertyName(av.getValue().toString());
                        break;

                    case STR_SERIA_NAME:
                        data.setSerializeName(av.getValue().toString());
                        break;

                    case STR_FLAGS:
                        data.setFlags(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_COMPLEXT_TYPE:
                        data.setComplexType(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_TYPE:
                        try {
                            pp.note(TAG, methodName, "STR_TYPE >>> " + av.getValue().toString());
                            final TypeMirror tm = (TypeMirror) av.getValue();
                            applyType(data, tm, pp);
                            addToFieldDependIfNeed(tm, sDependFieldMap);
                        } catch (ClassNotFoundException e) {
                            pp.note(Util.toString(e));
                            return false;
                        }
                        break;

                    default:
                        pp.note(TAG, methodName, "unsupport name = " + key.getSimpleName().toString());
                }
            }
            mFieldDatas.add(data);
        }
        return true;
    }

    private void addToFieldDependIfNeed(TypeMirror value, Map<String, List<String>> container) {
        final String key_className = mElement.getQualifiedName().toString();
        final String classname = value.toString().trim();
        //ignore primitive
        if (value instanceof PrimitiveType) {
            return;
        }
        //ignore official package.
        if (classname.startsWith("java.")
                || classname.startsWith("javax.")
                || classname.startsWith("android.")
                || classname.startsWith("dalvik.")
                ) {
            return;
        }
        //ignore, if the class is exist.
        try {
            Class.forName(classname);
            return;
        } catch (ClassNotFoundException e) {
            //not exist , we need.
        }
        List<String> list = container.get(key_className);
        if (list == null) {
            list = new ArrayList<>();
            container.put(key_className, list);
        }
        list.add(classname);
    }

    private static boolean isValidAnnotation(AnnotationMirror am, ProcessorPrinter pp) {
        Element e1 = am.getAnnotationType().asElement();
        Element e1_enclosing = e1.getEnclosingElement();
        if (!(e1_enclosing instanceof QualifiedNameable)) {
            pp.error(TAG, "isValidAnnotation", " annotation element not instanceof QualifiedNameable)");
            return false;
        }
        if (!TARGET_PACKAGE.equals(((QualifiedNameable) e1_enclosing).getQualifiedName().toString())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ElementHelper{" +
                "mElement=" + mElement +
                ", mWeight=" + mWeight +
                '}';
    }
}
