package com.heaven7.plugin.data.mediator.convertor;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

public class ConvertAction extends AnAction{

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        if(project == null){
            return;
        }
        PsiClass psiClass = getPsiClassFromContext(e);
        if(psiClass == null){
            Util.logError("psiClass == null");
            return;
        }
        PropertyProcessor processor = new PropertyProcessor(psiClass);
        processor.parse();
        processor.generate();
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        e.getPresentation().setEnabled(psiClass != null && !psiClass.isInterface() && psiClass.isWritable());
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
       // psiFile.getViewProvider().getVirtualFile()

        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);

        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }
}
