package com.heaven7.plugin.idea.data_mediator;

import com.heaven7.plugin.idea.data_mediator.typeInterface.TypeInterfaceExtend__Poolable;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateConstantFieldFromUsageFix;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.List;

public class PropertyMethodGenerator {

    private final List<Property> mProps = new ArrayList<>();
    private final PsiClass mPsiClass;
    private final String mSetReturn;
    private final String mCurrentModule;

    public PropertyMethodGenerator(PsiClass mPsiClass, boolean chain) {
        this.mPsiClass = mPsiClass;
        this.mCurrentModule = mPsiClass.getQualifiedName();
        if( !chain){
            mSetReturn = "void";
        }else {
            String name = mPsiClass.getQualifiedName();
            int index = name.lastIndexOf(".");
            if (index >= 0) {
                mSetReturn = name.substring(index + 1);
            } else {
                mSetReturn = name;
            }
        }
    }

    public void addProperty(Property property) {
        this.mProps.add(property);
    }

    public void generate(){
      /*  PsiJavaFile javaFile = (PsiJavaFile) mPsiClass.getContainingFile();
        PsiPackage pkg = JavaPsiFacade.getInstance(mPsiClass.getProject())
                .findPackage(javaFile.getPackageName());*/
        final Project project = mPsiClass.getProject();
        //remove exist method
        removeExistingImpl(mPsiClass);

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        List<PsiMethod> methods = new ArrayList<>();
        List<PsiField> fields = new ArrayList<>();
        for(Property prop: mProps) {
            //property cons , like : PROP_student
            fields.add(createConstantField(mPsiClass, elementFactory, prop));

            //get and set.
            String name = Util.getPropNameForMethod(prop.getName());
            String getMethod = generateGet(name, prop);
            String setMethod = generateSet(name, prop);
            methods.add(elementFactory.createMethodFromText(getMethod, mPsiClass));
            methods.add(elementFactory.createMethodFromText(setMethod, mPsiClass));

            //editor
            switch (prop.getComplexType()){
                case FieldFlags.COMPLEX_LIST:
                    methods.add(elementFactory.createMethodFromText(generateListEditor(name, prop), mPsiClass));
                    break;
                case FieldFlags.COMPLEX_SPARSE_ARRAY:
                    methods.add(elementFactory.createMethodFromText(generateSparseArrayEditor(name, prop), mPsiClass));
                    break;
            }
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        for(PsiField pf : fields){
            styleManager.shortenClassReferences(pf);
            mPsiClass.add(pf);
        }
        for(PsiMethod psi : methods) {
            styleManager.shortenClassReferences(mPsiClass.addBefore(psi, mPsiClass.getLastChild()));
        }
        //extend Poolable.
        TypeInterfaceExtend__Poolable poolable = new TypeInterfaceExtend__Poolable();
        if(!poolable.isSuperClassHas(mPsiClass)){
            poolable.makeInterfaceExtend(mPsiClass, elementFactory, styleManager);
        }
    }

    private PsiField createConstantField(PsiClass mPsiClass, PsiElementFactory elementFactory, Property prop) {
        // Property PROP_student = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student", 0);
        PsiType psiType = PsiType.getTypeByName("com.heaven7.java.data.mediator.Property",
                mPsiClass.getProject(), GlobalSearchScope.allScope(mPsiClass.getProject()));
        PsiField psiField = elementFactory.createField("PROP_" + prop.getName(), psiType);
        PsiExpression psiInitializer = elementFactory.createExpressionFromText(
                String.format("%s.get(\"%s\", \"%s\" ,%d)",
                        "com.heaven7.java.data.mediator.internal.SharedProperties" ,
                        prop.getTypeString(), prop.getName(), prop.getComplexType()),
                psiField);
        psiField.setInitializer(psiInitializer);
        PsiModifierList modifierList = psiField.getModifierList();
        if (modifierList != null) {
            modifierList.setModifierProperty( PsiModifier.PUBLIC + " " + PsiModifier.STATIC, true);
        }
        return psiField;
    }
    private String generateSparseArrayEditor(String name, Property prop) {
        //SparseArrayPropertyEditor<? extends TestParcelableDataModule, ResultData> beginTest_SparseArrayEditor()
        return String.format("%s<? extends %s, %s> begin%sEditor();",
                "com.heaven7.java.data.mediator.SparseArrayPropertyEditor" ,
                mCurrentModule,
                prop.getBoxTypeString(),
                name);
    }

    private String generateListEditor(String name, Property prop) {
        //ListPropertyEditor<? extends TestParcelableDataModule, Integer> beginTest_int_listEditor()
        return String.format("%s<? extends %s, %s> begin%sEditor();",
                "com.heaven7.java.data.mediator.ListPropertyEditor" ,
                mCurrentModule,
                prop.getBoxTypeString(),
                name);
    }

    private String generateSet(String name, Property prop) {
        return String.format("%s set%s(%s %s);", mSetReturn ,
                name,
                prop.getRealTypeString(),
                prop.getName() + "1" );
    }
    private String generateGet(String name, Property prop) {
        String prefix = prop.getTypeString().equals("boolean") ? "is" : "get";
        return String.format("%s %s%s();",  prop.getRealTypeString(), prefix,  name);
    }

    private void removeExistingImpl(PsiClass psiClass) {
        for(Property prop : mProps){
            //remove field
            findAndRemoveField(psiClass, prop.getName());
            //remove get and set.
            String name = Util.getPropNameForMethod(prop.getName());
            String prefix = prop.getTypeString().equals("boolean") ? "is" : "get";
            String getMethodName = prefix + name;
            String setMethodName = "set" + name;
            findAndRemoveMethod(psiClass, getMethodName);
            findAndRemoveMethod(psiClass, setMethodName, prop.getRealTypeString());
            //remove editor.
            switch (prop.getComplexType()){
                case FieldFlags.COMPLEX_LIST:
                case FieldFlags.COMPLEX_SPARSE_ARRAY:
                    findAndRemoveMethod(psiClass, "begin" + name + "Editor");
                    break;
            }
        }
       /* findAndRemoveMethod(psiClass, psiClass.getName(), TYPE_PARCEL);
        findAndRemoveMethod(psiClass, "describeContents");
        findAndRemoveMethod(psiClass, "writeToParcel", TYPE_PARCEL, "int");*/
    }

    private void findAndRemoveField(PsiClass clazz, String propName) {
        PsiField field = clazz.findFieldByName("PROP_" + propName, false);
        if (field != null){
            field.delete();
        }
    }

    //arguments is full name.
    private static void findAndRemoveMethod(PsiClass clazz, String methodName, String... arguments) {
        // Maybe there's an easier way to do this with mClass.findMethodBySignature(), but I'm not an expert on Psi*
        PsiMethod[] methods = clazz.findMethodsByName(methodName, false);

        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();

            if (parameterList.getParametersCount() == arguments.length) {
                boolean shouldDelete = true;

                PsiParameter[] parameters = parameterList.getParameters();

                for (int i = 0; i < arguments.length; i++) {
                   /* Util.log("read param type: " + parameters[i].getType().getCanonicalText());
                    Util.log("expect param type: " + arguments[i]);*/
                    if (!parameters[i].getType().getCanonicalText().equals(arguments[i])) {
                        shouldDelete = false;
                    }
                }
                if (shouldDelete) {
                    method.delete();
                }
            }
        }
    }

}
