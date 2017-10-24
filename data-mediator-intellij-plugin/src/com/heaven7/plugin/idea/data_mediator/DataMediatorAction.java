/*
 * Copyright (C) 2013 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.plugin.idea.data_mediator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.PsiUtilCore;

public class DataMediatorAction extends AnAction {

    private static final String NAME_FIELDS = "com.heaven7.java.data.mediator.Fields";


    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        if(project == null){
            return;
        }
        //PsiJavaFile javaFile = (PsiJavaFile) psiClass.getContaningFile();
        //PsiPackage pkg = JavaPsiFacade.getInstance(project).findPackage(javaFile.getPackageName());
        PsiClass psiClass = getPsiClassFromContext(e);
        if(psiClass == null){
            Util.logError("psiClass == null");
            return;
        }
        PsiModifierList list = psiClass.getModifierList();
        if(list == null){
            return;
        }

        PsiAnnotation annotation = list.findAnnotation(NAME_FIELDS);
        if(annotation == null){
            return;
        }
        PsiAnnotationMemberValue pam_chain = annotation.findAttributeValue("enableChain");
        final  PropertyMethodGenerator pmGenerator = new PropertyMethodGenerator(
                psiClass, Util.getBooleanValue(pam_chain));

        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if(value == null){
            return;
        }
        PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(project)
                .getConstantEvaluationHelper();

        PsiElement[] children = value.getChildren();
        for (PsiElement child : children){
            if(child instanceof PsiAnnotation){
                PsiAnnotation expect = (PsiAnnotation) child;
                PsiAnnotationMemberValue propName = expect.findAttributeValue("propName");
              //  String pName = propName.getText();
                String pName = (String) evaluationHelper.computeConstantExpression(propName);
                Util.log("propName = " + pName);
                //logAnnoValue(propName);

                String expectType;
                PsiAnnotationMemberValue typeVal = expect.findAttributeValue("type");
                String text_type = typeVal.getText();
                if(PsiUtils.isPrimitive(text_type)){
                    expectType = text_type.substring(0, text_type.lastIndexOf("."));
                }else {
                    if (typeVal instanceof PsiExpression) {
                        PsiType psiType = ((PsiExpression) typeVal).getType();
                        if (psiType instanceof PsiClassType) {
                            //class
                            String className = psiType.getCanonicalText();
                            expectType = className.substring(className.indexOf("<") + 1, className.lastIndexOf(">"));
                            // expectType = ((PsiClassType) psiType).resolve().getQualifiedName(); //java.lang.Class , not want.
                        } else {
                            //uncatch here
                            throw new UnsupportedOperationException("type is not  PsiClassType.");
                        }
                    }else{
                        //uncatch here
                        throw new UnsupportedOperationException("type is not PsiExpression.");
                    }
                }
              /*  Util.log("expectType = " + expectType);
                Util.log("type = " + text_type);
                logAnnoValue(typeVal);*/

                PsiAnnotationMemberValue complexType = expect.findAttributeValue("complexType");
                int val = (Integer) evaluationHelper.computeConstantExpression(complexType);

               /* Util.log("val = " + val);
                if(complexType != null){
                    Util.log("complexType = " + complexType.getText());
                    logAnnoValue(complexType);
                }
                Util.logNewLine();
                Util.log(new Property(expectType, pName, val).toString());
                Util.logNewLine();*/
                pmGenerator.addProperty(new Property(expectType, pName, val));
            }
        }
        generateDataMediator(psiClass, pmGenerator);
    }

    private void logAnnoValue(PsiAnnotationMemberValue complexType) {
        Util.log("PsiAnnotationMemberValue class =  " + complexType.getClass().getName());
        if(complexType instanceof PsiExpression){
            PsiType type = ((PsiExpression) complexType).getType();
            Util.log("PsiExpression = " + type.getClass().getName());
            //if is class , something like : getCanonicalText = java.lang.Class<com.heaven7.plugin.idea.data_mediator.test.Student>
            Util.log("getCanonicalText = " + type.getCanonicalText());
            if(type instanceof PsiPrimitiveType){
                Util.log("PsiPrimitiveType: box name = " + ((PsiPrimitiveType) type).getBoxedTypeName());
            }else if(type instanceof PsiClassType){
                //PsiClassReferenceType and PsiImmediateClassType
                Util.log("PsiClassType:  name = " + ((PsiClassType) type).getClassName() );
                PsiClassType rawType = ((PsiClassType) type).rawType();
                Util.log( "" + rawType.getCanonicalText());
            }
            if(PsiUtils.isOfType(type, "int")){
                Util.log("psi type is:   int" );
            }else if(PsiUtils.isOfType(type, "java.lang.String")){
                Util.log("psi type is:   java.lang.String" );
            }
        }
    }

    private void generateDataMediator(final PsiClass psiClass,
                                      final PropertyMethodGenerator generator) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                generator.generate();
            }
        }.execute();
    }


    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        e.getPresentation().setEnabled(psiClass != null && psiClass.isInterface());
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }
}
