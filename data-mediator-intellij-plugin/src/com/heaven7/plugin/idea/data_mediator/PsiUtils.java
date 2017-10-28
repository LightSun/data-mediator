package com.heaven7.plugin.idea.data_mediator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 */
final public class PsiUtils {
    private static final String NAME_SELECTABLE = "com.heaven7.adapter.ISelectable";

    /**
     * find the all extend PsiClass of super's extends.
     * @param psiClass the current psi class
     * @return the list of extends PsiClass of super's extends.
     */
    static List<PsiClass> getExtendsClasses(PsiClass psiClass){
        return getExtendsClasses(psiClass, 2, 0);
    }

    static List<PsiClass> getExtendsClasses(PsiClass psiClass, int lowDepth, int currentDepth){
        List<PsiClass> list = new ArrayList<>();
        for(PsiClassType type :  psiClass.getExtendsListTypes()) {
            PsiClass superPsiClass = type.resolve();
            if(superPsiClass != null){
                if(currentDepth + 1 >= lowDepth) {
                    list.add(superPsiClass);
                }
                list.addAll(getExtendsClasses(superPsiClass, lowDepth, currentDepth + 1));
            }
        }
        return list;
    }

    static boolean hasSelectable(PsiClass psiClass){
        PsiClassType[] listTypes = psiClass.getExtendsListTypes();
        for(PsiClassType type : listTypes){
            PsiClass superPsiClass = type.resolve();
            if (superPsiClass != null && NAME_SELECTABLE.equals(superPsiClass.getQualifiedName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks that the given type is an implementer of the given canonicalName with the given typed parameters
     *
     * @param type                what we're checking against
     * @param canonicalName       the type must extend/implement this generic
     * @param canonicalParamNames the type that the generic(s) must be (in this order)
     * @return
     */
    public static boolean isTypedClass(PsiType type, String canonicalName, String... canonicalParamNames) {
        PsiClass parameterClass = PsiTypesUtil.getPsiClass(type);

        if (parameterClass == null) {
            return false;
        }

        // This is a safe cast, for if parameterClass != null, the type was checked in PsiTypesUtil#getPsiClass(...)
        PsiClassType pct = (PsiClassType) type;

        // Main class name doesn't match; exit early
        if (!canonicalName.equals(parameterClass.getQualifiedName())) {
            return false;
        }

        List<PsiType> psiTypes = new ArrayList<PsiType>(pct.resolveGenerics().getSubstitutor().getSubstitutionMap().values());

        for (int i = 0; i < canonicalParamNames.length; i++) {
            if (!isOfType(psiTypes.get(i), canonicalParamNames[i])) {
                return false;
            }
        }

        // Passed all screenings; must be a match!
        return true;
    }

    /**
     * Resolves generics on the given type and returns them (if any) or null if there are none
     */
    public static List<PsiType> getResolvedGenerics(PsiType type) {
        List<PsiType> psiTypes = null;

        if (type instanceof PsiClassType) {
            PsiClassType pct = (PsiClassType) type;
            psiTypes = new ArrayList<PsiType>(pct.resolveGenerics().getSubstitutor().getSubstitutionMap().values());
        }

        return psiTypes;
    }

    public static boolean isOfType(PsiType type, String canonicalName) {
        if (type.getCanonicalText().equals(canonicalName)) {
            return true;
        }

        final String nonGenericType = getNonGenericType(type);

        if (nonGenericType != null && nonGenericType.equals(canonicalName)) {
            return true;
        }

        for (PsiType iterType : type.getSuperTypes()) {
            if (isOfType(iterType, canonicalName)) {
                return true;
            }
        }

        return false;
    }

    public static String getNonGenericType(PsiType type) {
        if (type instanceof PsiClassType) {
            PsiClassType pct = (PsiClassType) type;
            final PsiClass psiClass = pct.resolve();

            return psiClass != null ? psiClass.getQualifiedName() : null;
        }

        return type.getCanonicalText();
    }

    //like int.class, Integer.class, Student.class
    public static boolean isPrimitive(String text_type) {
        String str = text_type.substring(0, text_type.lastIndexOf("."));
        if(str.contains(".")){
            return false;
        }
        switch (str){
            case Property.TYPE_boolean:
            case Property.TYPE_byte:
            case Property.TYPE_char:
            case Property.TYPE_double:
            case Property.TYPE_float:
            case Property.TYPE_int:
            case Property.TYPE_long:
            case Property.TYPE_short:
                return true;
        }
        return false;
    }
}
