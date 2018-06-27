package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.Fields;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Collection;
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
    private static final String KEY_MAX_POOL_COUNT = "maxPoolCount";
    private static final String KEY_GROUPS = "groups"; //@Fileds

    private static final String KEY_GSON_CONFIG = "gsonConfig";
    private static final String KEY_GSON_GENERATE_JSON_ADAPTER = "generateJsonAdapter";
    private static final String KEY_GSON_VERSION = "version";
    private static final String KEY_GSON_FORCE_DISABLE = "forceDisable";

    private static final String KEY_VALUE = "value";
    private static final String KEY_FROM = "from";                 //@ImplMethod
    private static final String KEY_DEPEND_PROPS = "dependProps";  //@ImplMethod
    //GroupDesc
    private static final String KEY_PROP            = "prop";
    private static final String KEY_TYPE            = "type";
    private static final String KEY_FOCUS_VALUE     = "focusVal";
    private static final String KEY_OPPOSITE_VALUE  = "oppositeVal";
    private static final String KEY_AS_FLAG         = "asFlag";

    /**
     * only parse fields for handle super fields
     */
    public static boolean parseFields(Elements mElements, Types mTypes,
                                      AnnotationMirror am, Collection<FieldData> mFieldDatas,
                                      ProcessorPrinter pp) {
        final String methodName = "methodName";
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        pp.note(TAG, methodName, "am.getElementValues() = map . is " + map);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();
            pp.note(TAG, methodName, "key: " + key);//the method of annotation
            switch (key.getSimpleName().toString()) {
                case KEY_FIELDS_ANNO: {
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
                        pp.error(TAG, methodName, "@Fields's value() must have list of @Field. can't be empty");
                        return false;
                    }
                    if (!iterateField(mElements, mTypes,
                            (List<? extends AnnotationMirror>) list, pp, mFieldDatas)) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    //process @GlobalSetting
    public static boolean processGlobalSetting(Types mTypes, List<? extends AnnotationMirror> annoMirrors, ProcessorPrinter pp) {
        for (AnnotationMirror am : annoMirrors) {
            //if not my want. ignore
            if (!isValidAnnotation(am, pp)) {
                continue;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> methodMap = am.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : methodMap.entrySet()) {
                ExecutableElement key = en.getKey();//the method of annotation
                switch (key.getSimpleName().toString()) {
                    case KEY_GSON_CONFIG: {
                        Object value = en.getValue().getValue(); //@GsonConfig
                        AnnotationMirror am2 = (AnnotationMirror) value;
                        handleGsonConfig(am2, pp);
                    }

                    default:
                        //next to do
                        break;
                }
            }
        }
        return true;
    }

    /***process @Fields and @ImplClass /@ImplMethods */
    public static boolean processAnnotation(Elements mElements, Types mTypes, ProcessorPrinter pp,
                                            TypeElement element, CodeGenerator cg) {
        final List<FieldData> mFieldDatas = cg.getFieldDatas();
        final String methodName = "processAnnotation";
        List<? extends AnnotationMirror> annoMirrors = element.getAnnotationMirrors();
        //@Fields
        for (AnnotationMirror am : annoMirrors) {
            //if not my want. ignore
            if (!isValidAnnotation(am, pp)) {
                continue;
            }
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            final String rootAnnoName = e1.getQualifiedName().toString();
            pp.note(TAG, methodName, "anno qName = " + rootAnnoName);
            //here only handle @Fields
            if (rootAnnoName.equals(Fields.class.getName())) {
                if (!parseFieldsNormal(mElements, mTypes, pp, cg, mFieldDatas, am)) {
                    return false;
                }
                break;
            } /*else if (rootAnnoName.equals(ImplClass.class.getName())) {
                if (!parseImplClass(mTypes, pp, cg, am)) {
                    return false;
                }
            }*/
        }
        return true;
    }

    private static void handleGsonConfig(AnnotationMirror am, ProcessorPrinter pp) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();//the method of annotation

            AnnotationValue av = en.getValue();
            switch (key.getSimpleName().toString()) {
                case KEY_GSON_VERSION:
                    double version = Double.parseDouble(av.getValue().toString());
                    if (version >= 1.0) {
                        GlobalConfig.getInstance().setVersion(version);
                    }
                    break;

                case KEY_GSON_GENERATE_JSON_ADAPTER:
                    GlobalConfig.getInstance().setGenerateJsonAdapter(Boolean.valueOf(av.getValue().toString()));
                    break;

                case KEY_GSON_FORCE_DISABLE:
                    GlobalConfig.getInstance().setDisableGson(Boolean.valueOf(av.getValue().toString()));
                    break;
            }
        }
    }

    /*private*/
    static boolean parseImplMethodName(Types types, ProcessorPrinter pp, AnnotationMirror am,
                                       ImplInfo.MethodInfo info, CodeGenerator cg) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        pp.note(TAG, "parseImplMethodName", "am.getElementValues() = map . is " + map);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();

            switch (key.getSimpleName().toString()) {
                case KEY_VALUE:
                    info.setImplMethodName(en.getValue().getValue().toString());
                    break;

                case KEY_FROM:
                    TypeMirror tm = (TypeMirror) en.getValue().getValue();
                    if (!verifyClassName(tm, pp)) {
                        return false;
                    }
                    //must be
                    if (tm.getKind() != TypeKind.DECLARED) {
                        return false;
                    }
                    info.setImplClass(new FieldData.TypeCompat(types, tm));
                    break;

                case KEY_DEPEND_PROPS:
                    Object value = en.getValue().getValue();
                    if (value instanceof List) {
                        List<AnnotationValue> arrayMembers = (List<AnnotationValue>) value;
                        if (!iterateStrs(arrayMembers, info, cg)) {
                            return false;
                        }
                    }
                    break;
            }
        }
        return true;
    }

    private static boolean iterateStrs(List<AnnotationValue> list, ImplInfo.MethodInfo info, CodeGenerator cg) {
        List<FieldData> results = new ArrayList<>();
        for (AnnotationValue av : list){
            for(FieldData fd : cg.getFieldDatas()){
                if(fd.getPropertyName().equals(av.getValue().toString())){
                    results.add(fd);
                    break;
                }
            }
        }
        info.setDependProps(results);
        return true;
    }

    private static boolean iterateGroupProperty(List<? extends AnnotationMirror> list, ProcessorPrinter pp, CodeGenerator cg) {
        final String methodName = "iterateGroupProperty";
        pp.note(TAG, methodName, "=================== start iterate @GroupDesc() ====================");
        for (AnnotationMirror am1 : list) {
            if (!isValidAnnotation(am1, pp)) {
                return false;
            }
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am1.getElementValues();

            GroupProperty gp = new GroupProperty();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {

                ExecutableElement key = en.getKey();
                AnnotationValue av = en.getValue();
                pp.note(TAG, methodName, "test --->  " + av.getValue());

                switch (key.getSimpleName().toString()) {
                    case KEY_PROP:
                        gp.setProp(av.getValue().toString());
                        break;

                    case KEY_TYPE:
                        gp.setType(Byte.valueOf(av.getValue().toString()));
                        break;

                    case KEY_FOCUS_VALUE:
                        gp.setFocusVal(Long.valueOf(av.getValue().toString()));
                        break;

                    case KEY_OPPOSITE_VALUE:
                        gp.setOppositeVal(Long.valueOf(av.getValue().toString()));
                        break;

                    case KEY_AS_FLAG:
                        gp.setAsFlags((Boolean) en.getValue().getValue());
                        break;

                    default:
                        pp.note(TAG, methodName, "unsupport name = " + key.getSimpleName().toString());
                }
            }
            cg.addGroupProperty(gp);
        }
        return true;
    }

    public static boolean iterateField(Elements mElements, Types types, List<? extends AnnotationMirror> list,
                                       ProcessorPrinter pp, Collection<FieldData> datas) {
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

                    case STR_FLAGS:
                        data.setFlags(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_COMPLEXT_TYPE:
                        data.setComplexType(Integer.valueOf(av.getValue().toString()));
                        break;

                    case STR_TYPE:
                        pp.note(TAG, methodName, "STR_TYPE >>> " + av.getValue().toString());
                        final TypeMirror tm = (TypeMirror) av.getValue();
                        FieldData.TypeCompat typeCompat = new FieldData.TypeCompat(types, tm);
                        typeCompat.replaceIfNeed(mElements, pp);
                        data.setTypeCompat(typeCompat);
                        break;

                    case STR_SINCE:
                        data.setSince(Double.valueOf(av.getValue().toString()));
                        break;

                    case STR_UNTIL:
                        data.setUntil(Double.valueOf(av.getValue().toString()));
                        break;

                    default:
                        pp.note(TAG, methodName, "unsupport name = " + key.getSimpleName().toString());
                }
            }
            datas.add(data);
        }
        return true;
    }

    private static boolean parseFieldsNormal(Elements mElements, Types mTypes, ProcessorPrinter pp, CodeGenerator cg,
                                             List<FieldData> mFieldDatas, AnnotationMirror am) {
        String methodName = "parseFieldsNormal";
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        pp.note(TAG, methodName, "am.getElementValues() = map . is " + map);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();
            pp.note(TAG, methodName, "key: " + key);//the method of annotation

            switch (key.getSimpleName().toString()) {
                case KEY_FIELDS_ANNO: {
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
                    if (!iterateField(mElements, mTypes,
                            (List<? extends AnnotationMirror>) list, pp, mFieldDatas)) {
                        return false;
                    }
                }
                break;

                case KEY_GROUPS: {
                    Object target = en.getValue().getValue();
                    if (target == null || !(target instanceof List)) {
                        pp.error(TAG, methodName, "@Fields's groups() must be a list.");
                        return false;
                    }
                    List list = (List) target;
                    if (!list.isEmpty()) {
                        if (!iterateGroupProperty((List<? extends AnnotationMirror>) list, pp, cg)) {
                            return false;
                        }
                    }
                }
                break;

                case KEY_ENABLE_CHAIN:
                    cg.setEnableChain((Boolean) en.getValue().getValue());
                    break;

                case KEY_MAX_POOL_COUNT:
                    cg.setMaxPoolCount(Integer.parseInt(en.getValue().getValue().toString()));
                    break;

                case KEY_GSON_GENERATE_JSON_ADAPTER:
                    cg.setGenerateJsonAdapter((Boolean) en.getValue().getValue());
                    break;
            }
        }
        return true;
    }

    static boolean verifyClassName(TypeMirror tm, ProcessorPrinter pp) {
        final String fullName = tm.toString();
        if (fullName.startsWith("java.")) {
            pp.error(TAG, "verifyClassName", "can't use any class of 'java.**'");
            return false;
        }
        if (fullName.startsWith("android.")) {
            pp.error(TAG, "verifyClassName", "can't use any class of 'android.**'");
            return false;
        }
        switch (fullName) {
            case NAME_int:
            case NAME_long:
            case NAME_short:
            case NAME_byte:
            case NAME_boolean:
            case NAME_float:
            case NAME_double:
            case NAME_char:
            case NAME_void:
                pp.error(TAG, "verifyClassName", "can't use any primitive class");
                return false;
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
