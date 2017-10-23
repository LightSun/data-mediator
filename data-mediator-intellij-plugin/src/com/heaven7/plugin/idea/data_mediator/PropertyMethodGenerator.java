package com.heaven7.plugin.idea.data_mediator;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.ArrayList;
import java.util.List;

public class PropertyMethodGenerator {

    private final List<Property> mProps = new ArrayList<>();
    private final PsiClass mPsiClass;
    private final String mSetReturn;

    public PropertyMethodGenerator(PsiClass mPsiClass, boolean chain) {
        this.mPsiClass = mPsiClass;
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

    public void generate(){
      /*  PsiJavaFile javaFile = (PsiJavaFile) mPsiClass.getContainingFile();
        PsiPackage pkg = JavaPsiFacade.getInstance(mPsiClass.getProject())
                .findPackage(javaFile.getPackageName());*/
        //TODO delete exist
        removeExistingParcelableImplementation(mPsiClass);

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(mPsiClass.getProject());

        List<PsiMethod> methods = new ArrayList<>();
        PsiImportStatement import_list  = null;
        PsiImportStatement import_sa  = null;
        for(Property prop: mProps) {
            switch (prop.getComplexType()){
                case FieldFlags.COMPLEX_LIST:
                    if(import_list == null) {
                        import_list = elementFactory.createImportStatement(elementFactory.createClass("java.util.List"));
                    }
                    break;

                case FieldFlags.COMPLEX_SPARSE_ARRAY:
                    if(import_sa == null) {
                        import_sa = elementFactory.createImportStatement(elementFactory.createClass("com.heaven7.java.base.util.SparseArray"));
                    }
                    break;
            }
            String name = Util.getPropNameForMethod(prop.getName());
            methods.add(elementFactory.createMethodFromText(generateGet(name, prop), mPsiClass));
            methods.add(elementFactory.createMethodFromText(generateSet(name, prop), mPsiClass));
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mPsiClass.getProject());
        if(import_list != null){
            styleManager.shortenClassReferences(mPsiClass.addBefore(import_list, mPsiClass.getLastChild()));
        }
        if(import_sa != null){
            styleManager.shortenClassReferences(mPsiClass.addBefore(import_sa, mPsiClass.getLastChild()));
        }
        for(PsiMethod psi : methods) {
            styleManager.shortenClassReferences(mPsiClass.addBefore(psi, mPsiClass.getLastChild()));
        }
        makeClassImplementParcelable(elementFactory);
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

    private void removeExistingParcelableImplementation(PsiClass psiClass) {
        for(Property prop : mProps){
            String name = Util.getPropNameForMethod(prop.getName());
            String prefix = prop.getTypeString().equals("boolean") ? "is" : "get";
            String getMethodName = prefix + name;
            String setMethod = "set" + name;
            findAndRemoveMethod(psiClass, getMethodName);
            findAndRemoveMethod(psiClass, setMethod, prop.getRealTypeString());
        }
       /* findAndRemoveMethod(psiClass, psiClass.getName(), TYPE_PARCEL);
        findAndRemoveMethod(psiClass, "describeContents");
        findAndRemoveMethod(psiClass, "writeToParcel", TYPE_PARCEL, "int");*/
    }

    private void makeClassImplementParcelable(PsiElementFactory elementFactory) {

        final PsiClassType[] implementsListTypes = mPsiClass.getImplementsListTypes();
        final String implementsType = "android.os.Parcelable";

        for (PsiClassType implementsListType : implementsListTypes) {
            PsiClass resolved = implementsListType.resolve();

            // Already implements Parcelable, no need to add it
            if (resolved != null && implementsType.equals(resolved.getQualifiedName())) {
                return;
            }
        }

        PsiJavaCodeReferenceElement implementsReference = elementFactory.createReferenceFromText(implementsType, mPsiClass);
        PsiReferenceList implementsList = mPsiClass.getImplementsList();

        if (implementsList != null) {
            implementsList.add(implementsReference);
        }
    }
    private static void findAndRemoveMethod(PsiClass clazz, String methodName, String... arguments) {
        // Maybe there's an easier way to do this with mClass.findMethodBySignature(), but I'm not an expert on Psi*
        PsiMethod[] methods = clazz.findMethodsByName(methodName, false);

        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();

            if (parameterList.getParametersCount() == arguments.length) {
                boolean shouldDelete = true;

                PsiParameter[] parameters = parameterList.getParameters();

                for (int i = 0; i < arguments.length; i++) {
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

    public void addProperty(Property property) {
        this.mProps.add(property);
    }
}
