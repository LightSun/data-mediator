package com.heaven7.plugin.idea.data_mediator;

import com.heaven7.plugin.idea.data_mediator.typeInterface.TypeInterfaceExtend__Poolable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/*public*/ class PropertyGenerator {

    private static final String NAME_KEEP = "com.heaven7.java.data.mediator.Keep";
    private static final String NAME_IMPL_METHOD = "com.heaven7.java.data.mediator.ImplMethod";

    private List<Property> mSuperFields;
    private final List<Property> mProps = new ArrayList<>();

    private final PsiClass mPsiClass;
    private final String mCurrentModule;
    private String mSetReturn;

    private boolean mEnableChain;
    private boolean mHasSelectable;

    PropertyGenerator(PsiClass mPsiClass) {
        this.mPsiClass = mPsiClass;
        this.mCurrentModule = mPsiClass.getQualifiedName();
    }

    void setHasSelectable(boolean has) {
        mHasSelectable = has;
    }

    void setEnableChainCall(boolean enable) {
        mEnableChain = enable;
        if (!enable) {
            mSetReturn = "void";
        } else {
            String name = mPsiClass.getQualifiedName();
            int index = name.lastIndexOf(".");
            if (index >= 0) {
                mSetReturn = name.substring(index + 1);
            } else {
                mSetReturn = name;
            }
        }
    }

    boolean isEnableChain() {
        return mEnableChain;
    }

    List<Property> getSuperProperties() {
        if (mSuperFields == null) {
            mSuperFields = new ArrayList<>();
        }
        return mSuperFields;
    }

    List<Property> getProperties() {
        return mProps;
    }

    /**
     * 1， add Override for super properties.
     * 2, generate super.
     * 3, remove fields/methods which have no annotation of @Keep and @ImplMethod.
     */
    void generate() {
        final Project project = mPsiClass.getProject();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        //remove exist method
        removeExistingImpl(mPsiClass);

        List<PsiMethod> methods = new ArrayList<>();
        List<PsiField> fields = new ArrayList<>();

        //generate PROP_selected if possible.
        if(mHasSelectable) {
            fields.add(createConstantField(mPsiClass, elementFactory, Property.PROP_selected));
        }
        //generate for current properties.
        generateProperties(elementFactory, mProps, methods, fields, false);

        final PsiMethod anchor = methods.get(methods.size() - 1);
        PsiComment doc = elementFactory.createCommentFromText(
                "/* \n================== start super methods =============== */", null);
        //generate for super properties
        if (mSuperFields != null && !mSuperFields.isEmpty()) {
            generateProperties(elementFactory, mSuperFields, methods, fields, true);
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        for (PsiField pf : fields) {
            styleManager.shortenClassReferences(pf);
            mPsiClass.add(pf);
        }
        for (PsiMethod psi : methods) {
            styleManager.shortenClassReferences(mPsiClass.addBefore(psi, mPsiClass.getLastChild()));
            if (psi == anchor) {
                mPsiClass.addBefore(doc, mPsiClass.getLastChild());
            }
        }
        //extend Poolable.
        TypeInterfaceExtend__Poolable poolable = new TypeInterfaceExtend__Poolable();
        if (!poolable.isSuperClassHas(mPsiClass)) {
            poolable.makeInterfaceExtend(mPsiClass, elementFactory, styleManager);
        }
    }

    //override super setMethod if need..
    private void generateProperties(PsiElementFactory elementFactory, List<Property> props,
                                    List<PsiMethod> methods, List<PsiField> fields, boolean fromSuper) {
        for (Property prop : props) {
            //get and set.
            String name = Util.getPropNameForMethod(prop.getName());
            //TODO wait intellij response for 'interface override interface method'
            String setMethod = generateSet(name, prop, false);
            methods.add(elementFactory.createMethodFromText(setMethod, mPsiClass));
            if (!fromSuper) {
                //property cons , like : PROP_student
                fields.add(createConstantField(mPsiClass, elementFactory, prop));
                //set method
                String getMethod = generateGet(name, prop);
                methods.add(elementFactory.createMethodFromText(getMethod, mPsiClass));
            }
            //editor
            switch (prop.getComplexType()) {
                case FieldFlags.COMPLEX_LIST:
                    methods.add(elementFactory.createMethodFromText(generateListEditor(name, prop, false), mPsiClass));
                    break;
                case FieldFlags.COMPLEX_SPARSE_ARRAY:
                    methods.add(elementFactory.createMethodFromText(generateSparseArrayEditor(name, prop, false), mPsiClass));
                    break;
            }
        }
    }

    private PsiField createConstantField(PsiClass mPsiClass, PsiElementFactory elementFactory, Property prop) {
        // Property PROP_student = SharedProperties.get("com.heaven7.data.mediator.demo.testpackage.TestBind", "student", 0);
        PsiType psiType = PsiType.getTypeByName("com.heaven7.java.data.mediator.Property",
                mPsiClass.getProject(), GlobalSearchScope.allScope(mPsiClass.getProject()));
        PsiField psiField = elementFactory.createField("PROP_" + prop.getName(), psiType);
        PsiExpression psiInitializer = elementFactory.createExpressionFromText(
                String.format("%s.get(\"%s\", \"%s\" ,%d)",
                        "com.heaven7.java.data.mediator.internal.SharedProperties",
                        prop.getTypeString(), prop.getName(), prop.getComplexType()),
                psiField);
        psiField.setInitializer(psiInitializer);
        PsiModifierList modifierList = psiField.getModifierList();
        if (modifierList != null) {
            modifierList.setModifierProperty(PsiModifier.PUBLIC + " " + PsiModifier.STATIC, true);
        }
        return psiField;
    }

    private String generateSparseArrayEditor(String name, Property prop, boolean override) {
        //SparseArrayPropertyEditor<? extends TestParcelableDataModule, ResultData> beginTest_SparseArrayEditor()
        final String base = "%s<? extends %s, %s> begin%sEditor();";
        String format = override ? "@java.lang.Override " + base : base;
        return String.format(format,
                "com.heaven7.java.data.mediator.SparseArrayPropertyEditor",
                mCurrentModule,
                prop.getBoxTypeString(),
                name);
    }

    private String generateListEditor(String name, Property prop, boolean override) {
        //ListPropertyEditor<? extends TestParcelableDataModule, Integer> beginTest_int_listEditor()
        final String base = "%s<? extends %s, %s> begin%sEditor();";
        String format = override ? "@java.lang.Override " + base : base;
        return String.format(format,
                "com.heaven7.java.data.mediator.ListPropertyEditor",
                mCurrentModule,
                prop.getBoxTypeString(),
                name);
    }

    private String generateSet(String name, Property prop, boolean addOverride) {
        String format = addOverride ? " @java.lang.Override %s set%s(%s %s);" : "%s set%s(%s %s);";
        return String.format(format, mSetReturn,
                name,
                prop.getRealTypeString(),
                prop.getName() + "1");
    }

    private String generateGet(String name, Property prop) {
        String prefix = getPrefix(prop);
        return String.format("%s %s%s();", prop.getRealTypeString(), prefix, name);
    }

    private void removeExistingImpl(PsiClass psiClass) {
        //here can't use getAllFields/getAllMethods. they contains super methods and fields.
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            PsiModifierList list = field.getModifierList();

            boolean delete = false;
            if (list == null) {
                delete = true;
            } else {
                PsiAnnotation psiAnnotation = list.findAnnotation(NAME_KEEP);
                if (psiAnnotation == null) {
                    delete = true;
                }
            }
            if (delete) {
                field.delete();
            }
        }
        for (PsiMethod method : psiClass.getMethods()) {
            PsiModifierList list = method.getModifierList();

            PsiAnnotation pa_keep = list.findAnnotation(NAME_KEEP);
            PsiAnnotation pa_impl = list.findAnnotation(NAME_IMPL_METHOD);
            if (pa_keep == null && pa_impl == null) {
                method.delete();
            }
        }
       /* findAndRemoveMethod(psiClass, psiClass.getName(), TYPE_PARCEL);
        findAndRemoveMethod(psiClass, "describeContents");
        findAndRemoveMethod(psiClass, "writeToParcel", TYPE_PARCEL, "int");*/
    }

    @NotNull
    private String getPrefix(Property prop) {
        return prop.getComplexType() == 0 && prop.getTypeString().equals("boolean") ? "is" : "get";
    }

    private void findAndRemoveField(PsiClass clazz, String propName) {
        PsiField field = clazz.findFieldByName("PROP_" + propName, false);
        if (field != null) {
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
