package com.heaven7.plugin.idea.data_mediator.typeInterface;

import com.heaven7.plugin.idea.data_mediator.ITypeInterfaceExtend;
import com.heaven7.plugin.idea.data_mediator.PsiUtils;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public class TypeInterfaceExtend__Poolable implements ITypeInterfaceExtend {

    @Override
    public boolean isSuperClassHas(PsiClass mClass) {
        PsiClassType[] superTypes = mClass.getSuperTypes();
        for (PsiClassType superType : superTypes) {
            if (PsiUtils.isOfType(superType, "com.heaven7.java.data.mediator.DataPools.Poolable")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void makeInterfaceExtend(PsiClass mClass, PsiElementFactory elementFactory, JavaCodeStyleManager styleManager) {
        // get extend interfaces.
        final PsiClassType[] implementsListTypes = mClass.getExtendsListTypes();
        final String implementsType = "com.heaven7.java.data.mediator.DataPools.Poolable";

        for (PsiClassType implementsListType : implementsListTypes) {
            PsiClass resolved = implementsListType.resolve();

            // Already implements Parcelable, no need to add it
            if (resolved != null && implementsType.equals(resolved.getQualifiedName())) {
                return;
            }
        }
        PsiJavaCodeReferenceElement implementsReference = elementFactory.createReferenceFromText(implementsType, mClass);
        PsiReferenceList implementsList = mClass.getExtendsList();
        if (implementsList != null) {
            styleManager.shortenClassReferences(implementsReference);
            implementsList.add(implementsReference);
        }
    }
}
