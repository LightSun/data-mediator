package com.heaven7.java.data.mediator.lint;

import com.intellij.psi.*;
import com.intellij.util.containers.ArrayListSet;
import org.jetbrains.uast.UClass;

import java.util.Set;

/**
 * Created by heaven7 on 2017/11/29 0029.
 */

/*public*/ class PropertyUtils {

    private static final String FOCUS_FIELDS = "com.heaven7.java.data.mediator.Fields";
    //private static final String FOCUS_FIELD  = "com.heaven7.java.data.mediator.Field";

    static Set<PropertyDetector.PropInfo> getPropInfoWithSupers(UClass uClass){
        Set<PropertyDetector.PropInfo> mSet = new ArrayListSet<>();
        getPropInfoWithSupers(uClass.getPsi(), mSet);
        return mSet;
    }
    private static void getPropInfoWithSupers(PsiClass uClass, Set<PropertyDetector.PropInfo> out){
        if(uClass == null || uClass.getQualifiedName().startsWith("java.")
                || uClass.getQualifiedName().startsWith("android.")){
            return;
        }
        getPropInfosForTarget(uClass, out);
        for(PsiClass clazz : uClass.getInterfaces()){
           getPropInfoWithSupers(clazz, out);
        }
    }

    private static void getPropInfosForTarget(PsiClass psiClass, Set<PropertyDetector.PropInfo> mSet) {
        final PsiModifierList list = psiClass.getModifierList();
        if(list == null){
            return;
        }
        for(PsiAnnotation ua : list.getAnnotations()){
            if(!getPropInfosOfFields(ua, mSet)){
                return;
            }
        }
    }

    //true means success
    private static boolean getPropInfosOfFields(PsiAnnotation psa_fields, Set<PropertyDetector.PropInfo> mSet) {
        String qualifiedName = psa_fields.getQualifiedName();
        if(qualifiedName == null || !qualifiedName.equals(FOCUS_FIELDS)){
            return false;
        }
       // Set<PropertyDetector.PropInfo> mSet = new ArrayListSet<>();
        PsiConstantEvaluationHelper evaluation = JavaPsiFacade.getInstance(psa_fields.getProject())
                .getConstantEvaluationHelper();

        PsiAnnotationMemberValue tempValues = psa_fields.findAttributeValue("value");
        if(tempValues == null){
            return false;
        }
         //all fields
        for(PsiElement child : tempValues.getChildren()){
            if(child instanceof PsiAnnotation){
                PsiAnnotation expect = (PsiAnnotation) child;
                String prop = (String) evaluation.computeConstantExpression(expect.findAttributeValue("propName"));

                PsiAnnotationMemberValue typeVal = expect.findAttributeValue("type");
                boolean is_bool = false;
                if(typeVal instanceof PsiExpression){
                    PsiType type = ((PsiExpression) typeVal).getType();
                    if(PsiType.BOOLEAN.equals(type)){
                        is_bool = true;
                    }
                }else{
                    continue;
                }
                mSet.add(new PropertyDetector.PropInfo(prop, is_bool));
            }
        }
        return true;
    }
    static String getProp(String methodName){
        String prop = methodName;
        if(methodName.startsWith("is")){
            String temp = methodName.substring(2);
            prop = temp.substring(0,1).toLowerCase() + temp.substring(1);
        }else if(methodName.startsWith("get")){
            String temp = methodName.substring(3);
            prop = temp.substring(0,1).toLowerCase() + temp.substring(1);
        }else if(methodName.startsWith("set")){
            String temp = methodName.substring(3);
            prop = temp.substring(0,1).toLowerCase() + temp.substring(1);
        }
        return prop;
    }
}
