package com.heaven7.plugin.idea.data_mediator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public interface ITypeInterfaceExtend {

    boolean isSuperClassHas(PsiClass type);

    void makeInterfaceExtend(PsiClass mClass, PsiElementFactory elementFactory, JavaCodeStyleManager styleManager);
}
