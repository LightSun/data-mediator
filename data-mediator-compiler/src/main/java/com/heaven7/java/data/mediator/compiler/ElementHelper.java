package com.heaven7.java.data.mediator.compiler;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/9/5 0005.
 */
/*public*/ class ElementHelper {

    private static final String TAG = "ElementHelper";
    private static final String TARGET_PACKAGE = "com.heaven7.java.data.mediator";
    private static final String KEY_FIELDS_ANNO = "value";
    private static final String KEY_ENABLE_CHAIN = "enableChain";

    //process @Fields
    public static boolean processAnnotation(Types mTypes, ProcessorPrinter pp,
                                             List<? extends AnnotationMirror> annoMirrors, CodeGenerator cg) {
        final List<FieldData> mFieldDatas = cg.getFieldDatas();

        final String methodName = "processAnnotation";
        //@Fields
        for (AnnotationMirror am : annoMirrors) {
            //if not my want. ignore
            if (!isValidAnnotation(am, pp)) {
               continue;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
            pp.note(TAG, methodName, "am.getElementValues() = map . is " + map);
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
                ExecutableElement key = en.getKey();
                pp.note(TAG, methodName, "key: " + key);//the method of annotation

                switch (key.getSimpleName().toString()){
                    case KEY_FIELDS_ANNO:{
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
                        if (!iterateField(mTypes, (List<? extends AnnotationMirror>) list, pp, mFieldDatas)) {
                            return false;
                        }
                    }
                        break;

                    case KEY_ENABLE_CHAIN:
                        cg.setEnableChain((Boolean) en.getValue().getValue());
                        break;
                }
            }
        }
        return true;
    }

    public static boolean iterateField(Types types ,List<? extends AnnotationMirror> list, ProcessorPrinter pp, List<FieldData> datas) {
        final String methodName = "iterateField";
        pp.note(TAG, "iterateField", "=================== start iterate @Field() ====================");
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

                    case STR_FLAGS: {
                        Integer flags = Integer.valueOf(av.getValue().toString());
                        if(flags == 0){
                            //default to main flags
                            flags = FieldData.FLAGS_MAIN;
                        }
                        data.setFlags(flags);
                    }
                        break;

                    case STR_COMPLEXT_TYPE:
                        data.setComplexType(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_TYPE:
                        pp.note(TAG, methodName, "STR_TYPE >>> " + av.getValue().toString());
                        final TypeMirror tm = (TypeMirror) av.getValue();
                        FieldData.TypeCompat typeCompat = new FieldData.TypeCompat(types, tm);
                        data.setTypeCompat(typeCompat);
                        typeCompat.replaceIfNeed(pp);
                        break;

                    default:
                        pp.note(TAG, methodName, "unsupport name = " + key.getSimpleName().toString());
                }
            }
            datas.add(data);
        }
        return true;
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

}
