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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.DefaultJavaProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.terminal.TerminalExecutionConsole;
import groovyjarjarantlr.CodeGenerator;

import java.nio.charset.Charset;
import java.util.List;

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
        Util.log("enableChain = " + pam_chain.getText());
        logAnnoValue(pam_chain);
        final  PropertyMethodGenerator pmGenerator = new PropertyMethodGenerator(psiClass, Util.getBooleanValue(pam_chain));

        Util.log("anno of fields = " + annotation);
        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if(value == null){
            return;
        }
        PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper();

        PsiElement[] children = value.getChildren();
        Util.log("anno of all field.size = " + children.length);
        for (PsiElement child : children){
            if(child instanceof PsiAnnotation){
                PsiAnnotation expect = (PsiAnnotation) child;
                PsiAnnotationMemberValue propName = expect.findAttributeValue("propName");
                String pName = propName.getText();
                Util.log("propName = " + pName);
                logAnnoValue(propName);

                String expectType = null;
                PsiAnnotationMemberValue typeVal = expect.findAttributeValue("type");
                String text_type = typeVal.getText();
                if(typeVal instanceof PsiExpression){
                    PsiType psiType = ((PsiExpression) typeVal).getType();
                    if(psiType instanceof PsiPrimitiveType){
                        expectType = text_type.substring(0, text_type.lastIndexOf("."));
                    }else if(psiType instanceof PsiClassType){
                        expectType = ((PsiClassType) psiType).resolve().getQualifiedName();
                    }else {
                        //uncatch here
                        throw new UnsupportedOperationException("type is not PsiPrimitiveType and PsiClassType.");
                    }
                }

                Util.log("type = " + text_type);
                logAnnoValue(typeVal);

                PsiAnnotationMemberValue complexType = expect.findAttributeValue("complexType");
                int val = (int) evaluationHelper.computeConstantExpression(complexType);

                Util.log("val = " + val);
                if(complexType != null){
                    Util.log("complexType = " + complexType.getText());
                    logAnnoValue(complexType);
                }
                pmGenerator.addProperty(new Property(expectType, pName, val));
                //TODO how to get value from expression
            }
        }
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
            }
            if(PsiUtils.isOfType(type, "int")){
                Util.log("psi type is:   int" );
            }else if(PsiUtils.isOfType(type, "java.lang.String")){
                Util.log("psi type is:   java.lang.String" );
            }
        }
    }

    private void generateDataMediator(final PsiClass psiClass, final List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
               //TODO new CodeGenerator(psiClass, fields).generate();
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
