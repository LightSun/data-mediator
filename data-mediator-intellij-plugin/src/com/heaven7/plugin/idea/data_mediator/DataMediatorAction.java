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

import java.util.List;

public class DataMediatorAction extends AnAction {

    private static final String NAME_FIELDS = "com.heaven7.java.data.mediator.Fields";
    public static final String NAME_SELECTABLE = "com.heaven7.adapter.ISelectable";

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
       // final PsiElement parent = psiClass.getParent();
       // Util.logError("parent == " + parent.getClass().getName());

        final PropertyGenerator pmGenerator = new PropertyGenerator(psiClass);

        //parse current
        if (!parseProperties(project, psiClass, pmGenerator, pmGenerator.getProperties())) {
            return;
        }
        if(pmGenerator.isEnableChain()){
            //parse super and sub fields.
            parseSuperProperties(project, psiClass, pmGenerator, true);
        }
        //indicate if super has ISelectable or nor.
        boolean superHasSelectable = false;
        for(PsiClass pc : PsiUtils.getExtendsClasses(psiClass)){
            if(NAME_SELECTABLE.equals(pc.getQualifiedName())){
                superHasSelectable = true;
                break;
            }
        }
        pmGenerator.setHasSelectable(PsiUtils.hasSelectable(psiClass) && !superHasSelectable);
        //do generate
        generateDataMediator(psiClass, pmGenerator);
        //change subclass's generate methods  when chain call enabled and super property changed.
        //but we don't manual change all sub PsiClass. Note this.
    }
    private void parseSuperProperties(Project project, PsiClass psiClass,
                                      PropertyGenerator pmGenerator, boolean isCurrentPsiClass) {
        PsiClassType[] listTypes = psiClass.getExtendsListTypes();
        for(PsiClassType type : listTypes){
            PsiClass superPsiClass = type.resolve();
            if(superPsiClass != null){
                parseProperties(project, superPsiClass, null, pmGenerator.getSuperProperties());
                //parse super recursively
                parseSuperProperties(project, superPsiClass, pmGenerator, false);
            }
        }
    }

    /**
     * parse properties of @Fields.
     * @param project the project
     * @param psiClass the psi class. must be interface in 'data-mediator'
     * @param pg the property generator , can be null when parse super.
     * @param props the out properties
     * @return true if parse success. false otherwise
     */
    private boolean parseProperties(Project project, PsiClass psiClass, PropertyGenerator pg,List<Property> props) {
        PsiModifierList list = psiClass.getModifierList();
        if(list == null){
            return false;
        }

        PsiAnnotation annotation = list.findAnnotation(NAME_FIELDS);
        if(annotation == null){
            return false;
        }
        PsiAnnotationMemberValue pam_chain = annotation.findAttributeValue("enableChain");
        if(pg != null) {
            pg.setEnableChainCall(Util.getBooleanValue(pam_chain));
        }

        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if(value == null){
            return false;
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
               // Util.log("propName = " + pName);

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
                PsiAnnotationMemberValue complexType = expect.findAttributeValue("complexType");
                int val = (Integer) evaluationHelper.computeConstantExpression(complexType);
                props.add(new Property(expectType, pName, val));
            }
        }
        return true;
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
                                      final PropertyGenerator generator) {
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
