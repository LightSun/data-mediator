package com.heaven7.java.data.mediator.compiler;


import com.heaven7.java.data.mediator.ImplClass;
import com.heaven7.java.data.mediator.ImplMethod;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_void;
import static com.heaven7.java.data.mediator.compiler.ElementHelper.parseImplMethodName;
import static com.heaven7.java.data.mediator.compiler.ElementHelper.verifyClassName;

/**
 * get all impl infos for supers'
 *
 * @see com.heaven7.java.data.mediator.ImplClass
 * @see com.heaven7.java.data.mediator.ImplMethod
 */
public class ImplInfoDelegate {

    private static final String TAG = "ImplInfoDelegate";
    final Types types;
    final ProcessorPrinter pp;

    public ImplInfoDelegate(Types types, ProcessorPrinter pp) {
        this.types = types;
        this.pp = pp;
    }

    boolean parseImplInfo(TypeElement te, CodeGenerator cg) {
        ImplInfo[] cur_infos = new ImplInfo[1];
        if(!parseImplInfo(te, cur_infos, cg)){
            return false;
        }
        if(cur_infos[0] != null && cur_infos[0].isValid()){
            cg.setCurrentImplInfo(cur_infos[0]);
        }

        //handle super interface with @ImplClass and @ImplMethods
        List<ImplInfo> superImplInfos = new ArrayList<>();
        for (TypeMirror tm : te.getInterfaces()) {
            TypeElement te_temp = new FieldData.TypeCompat(types, tm).getElementAsType();
            if(!parseImplInfosRecursively(te_temp, superImplInfos, cg)){
                return false;
            }
        }
        cg.setSuperImplInfos(superImplInfos);
        return true;
    }

    /**
     * parse impl info for current element
     * @param te the current type element
     * @param out the out infos
     * @return true if parse without error.
     */
    private boolean parseImplInfo(TypeElement te, ImplInfo[] out, CodeGenerator cg) {
        //handle @ImplClass
        AnnotationMirror am_impl = null;
        for (AnnotationMirror am : te.getAnnotationMirrors()) {
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            if ( e1.getQualifiedName().toString().equals(ImplClass.class.getName())) {
                am_impl = am;
                break;
            }
        }
        ImplInfo info = new ImplInfo();
        if (am_impl != null) {
            if(!parseImplClass(types, pp, am_impl, info)){
                return false;
            }
        }
        //handle @ImplMethod. note: may only have @ImplMethod.
        List<? extends Element> elements = ElementFilter.methodsIn(te.getEnclosedElements());
        parseImplMethods(types, pp, elements, info, cg);
        if(info.isValid()) {
            out[0] = info;
        }
        return true;
    }
    /**
     * parse impl infos Recursively.
     * @param te the root element.
     * @param list the list to add to.
     * @return true if parse success.
     */
    private boolean parseImplInfosRecursively(TypeElement te, List<ImplInfo> list, CodeGenerator cg) {

        //current
        ImplInfo[] cur_infos = new ImplInfo[1];
        if(!parseImplInfo(te, cur_infos, cg)){
            return true;
        }
        if(cur_infos[0] != null && cur_infos[0].isValid()){
            list.add(cur_infos[0]);
        }
        //handle super interface with @ImplClass and @ImplMethods
        for (TypeMirror tm : te.getInterfaces()) {
            TypeElement te_temp = new FieldData.TypeCompat(types, tm).getElementAsType();
            if(!parseImplInfosRecursively(te_temp, list, cg)){
                return false;
            }
        }
        return true;
    }

    private static boolean parseImplClass(Types types, ProcessorPrinter pp, AnnotationMirror am, ImplInfo info) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        pp.note(TAG, "parseImplClass", "am.getElementValues() = map . is " + map);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();
            if (key.getSimpleName().toString().equals("value")) {
                TypeMirror tm = (TypeMirror) en.getValue().getValue();
                if (!verifyClassName(tm, pp)) {
                    return false;
                }
                info.setImplClass(new FieldData.TypeCompat(types, tm));
            }
        }
        return true;
    }

    private static boolean parseImplMethods(Types mTypeUtils,
                                            ProcessorPrinter pp,
                                            Collection<? extends Element> list,
                                            ImplInfo info, CodeGenerator cg) {
        //parse all @ImplMethod
        for (Element e : list) {
            //only parse method.
            if (e instanceof ExecutableElement) {
                final ExecutableElement method = (ExecutableElement) e;

                //ImplMethod -- name and class name
                List<? extends AnnotationMirror> mirrors = method.getAnnotationMirrors();
                /* indicate @ImplMethod which have annotation @ImplMethod */
                AnnotationMirror anno_implMethod = null;
                for (AnnotationMirror am : mirrors) {
                    TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
                    if (e1.getQualifiedName().toString().equals(ImplMethod.class.getName())) {
                        anno_implMethod = am;
                        break;
                    }
                }
                //no @ImplMethod
                if (anno_implMethod == null) {
                    continue;
                }
                final ImplInfo.MethodInfo methodInfo = new ImplInfo.MethodInfo();
                methodInfo.setMethodName(method.getSimpleName().toString());
                if(!parseImplMethodName(mTypeUtils, pp, anno_implMethod, methodInfo, cg)){
                    return false;
                }
                if(methodInfo.getImplClass() == null){
                    if(info.getImplClass() == null){
                        pp.note(TAG, "parseImplMethods", "you must " +
                                "assign impl class by @ImplClass or @ImplMethod(from=xxx.class)");
                        return false;
                    }
                    methodInfo.setImplClass(info.getImplClass());
                }

                //return
                TypeMirror type = method.getReturnType();
                TypeName returnType = TypeName.get(type);
                boolean returnVoid = false;
                if (type.toString().equals(NAME_void)) {
                    returnVoid = true;
                }
                methodInfo.setReturnType(returnType);
                methodInfo.setReturnVoid(returnVoid);

                //if not define method name, use the same name of implClass's
                if (methodInfo.getImplMethodName() == null || methodInfo.getImplMethodName().equals("")) {
                    methodInfo.setImplMethodName(methodInfo.getMethodName());
                }

                //param type
                List<ImplInfo.ParamInfo> params = new ArrayList<>();
                List<? extends VariableElement> parameters = method.getParameters();
                for (VariableElement ve : parameters) {
                    String name = ve.getSimpleName().toString();
                    params.add(new ImplInfo.ParamInfo(name, TypeName.get(ve.asType())));
                }
                methodInfo.setParamInfos(params);
                //method.getEnclosingElement() may have super's @ImplClass and @ImplMethod.
                info.addMethodInfo(methodInfo);
            }
        }
        return true;
    }
}
