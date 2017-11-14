package com.heaven7.plugin.data.mediator.convertor;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;

import java.util.*;

public class PropertyProcessor {

    private static final String NAME_SERIALIZE = "com.google.gson.annotations.SerializedName";
    private static final String NAME_EXPOSE    = "com.google.gson.annotations.Expose";
    private static final String NAME_SINCE     = "com.google.gson.annotations.Since";
    private static final String NAME_UNTIL     = "com.google.gson.annotations.Until";
    private final PsiClass mPsiClass;
    private final LinkedHashMap<PsiClass, ClassInfo> mMap;

    public PropertyProcessor(PsiClass psiClass) {
        this.mPsiClass = psiClass;
        this.mMap = new LinkedHashMap<>();
    }

    public void parse(){
        /*
         * 1, get the all property names
         * 2, serialize name and expose
         * 3, super classes and interfaces.
         * 4. generate annotations
         */
        LinkedList<PsiClass> list = new LinkedList<>();
        list.addLast(mPsiClass);
        PsiClass superClass = mPsiClass.getSuperClass();
        while (superClass != null){
            String qualifiedName = superClass.getQualifiedName();
            if(qualifiedName == null || qualifiedName.startsWith("java.") || qualifiedName.startsWith("android.")){
                break;
            }
            list.addLast(superClass);
            superClass = superClass.getSuperClass();
        }
        //from tail to head. means from the root super -> to current
        for(PsiClass pc = list.pollLast() ; pc != null; pc = list.pollLast()){
            parseInternal(pc);
        }
    }

    private void parseInternal(PsiClass mPsiClass) {
        final ClassInfo info = new ClassInfo();
        if(mPsiClass.getSuperClass() != null){
            info.addInterface(getSuperClassAsInterfaceName(mPsiClass.getSuperClass()));
        }
        for(PsiClass pcs : mPsiClass.getInterfaces()){
            info.addInterface(pcs.getQualifiedName());
        }

        //get fields of current class
        final Project project = mPsiClass.getProject();
        final PsiConstantEvaluationHelper evaluationHelper = JavaPsiFacade.getInstance(project)
                .getConstantEvaluationHelper();
        PsiType psi_list = PsiType.getTypeByName("java.util.List",
                project, GlobalSearchScope.allScope(project));
        PsiType psi_spa = PsiType.getTypeByName("com.heaven7.java.base.util.SparseArray",
                project, GlobalSearchScope.allScope(project));

        final List<PropertyInfo> infos = info.getPropertyInfos();

        PsiField[] fields = mPsiClass.getFields();
        for(PsiField field : fields){
            PsiModifierList list = field.getModifierList();
            //ignore static and final
            if(list != null){
                if(list.hasModifierProperty(PsiModifier.STATIC) || list.hasModifierProperty(PsiModifier.FINAL)){
                    continue;
                }
            }
            PsiType type = field.getType();
            PropertyInfo pi = new PropertyInfo();
            infos.add(pi);

            String fdTyoe = type.getCanonicalText();
            int complexType = 0;
            Util.log("getCanonicalText() = " +  type.getCanonicalText());
            if(type instanceof PsiArrayType){
                Util.log(" is array");
                complexType = FieldFlags.COMPLEX_ARRAY;
                fdTyoe = fdTyoe.substring(0, fdTyoe.indexOf("["));
            }else if(type.isAssignableFrom(psi_list)){
                Util.log(" is list");
                complexType = FieldFlags.COMPLEX_LIST;

                fdTyoe = fdTyoe.substring(fdTyoe.indexOf("<") + 1, fdTyoe.lastIndexOf(">"));
            }else if(type.isAssignableFrom(psi_spa)){
                Util.log(" is SparseArray");

                complexType = FieldFlags.COMPLEX_SPARSE_ARRAY;
                fdTyoe = fdTyoe.substring(fdTyoe.indexOf("<") + 1, fdTyoe.lastIndexOf(">"));
            }
            //TODO inner class?
            pi.setProperty(new Property(fdTyoe, field.getName(), complexType));
            if(list != null){
                PsiAnnotation seria = list.findAnnotation(NAME_SERIALIZE);
                if(seria != null){
                   String seriaName = (String) evaluationHelper.computeConstantExpression(seria.findAttributeValue("value"));
                    pi.setSerializeName(seriaName);
                }
                PsiAnnotation expose = list.findAnnotation(NAME_EXPOSE);
                if(expose != null){
                    pi.setUseExpose(true);
                    boolean serialize = (boolean) evaluationHelper.computeConstantExpression(expose.findAttributeValue("serialize"));
                    boolean deserialize = (boolean) evaluationHelper.computeConstantExpression(expose.findAttributeValue("deserialize"));
                    pi.setSerialize(serialize);
                    pi.setDeserialize(deserialize);
                }
                PsiAnnotation since = list.findAnnotation(NAME_SINCE);
                if(since != null){
                    double sinceVal = (double) evaluationHelper.computeConstantExpression(since.findAttributeValue("value"));
                    pi.setSince(sinceVal);
                }
                PsiAnnotation until = list.findAnnotation(NAME_UNTIL);
                if(until != null){
                    double untilVal = (double) evaluationHelper.computeConstantExpression(until.findAttributeValue("value"));
                    pi.setUntil(untilVal);
                }
            }
        }
        mMap.put(mPsiClass, info);
    }

    public void generate() {
        new WriteCommandAction.Simple(mPsiClass.getProject(), mPsiClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                for(Map.Entry<PsiClass, ClassInfo> en : mMap.entrySet()){
                    generateInternal(en.getKey(), en.getValue());
                }
            }
        }.execute();
    }

    private void generateInternal(PsiClass mPsiClass, ClassInfo info) {
        if(mPsiClass.getName() == null){
            Util.log("mPsiClass.getName() == null");
            return;
        }
        final String name = "I" + mPsiClass.getName();
        final Project project = mPsiClass.getProject();
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        PsiElement parent = mPsiClass.getParent();
        if(parent instanceof PsiClass){
            Util.log("start nested class.");
            PsiClass psic = elementFactory.createInterface(name);
            psic.getModifierList().addAnnotation("com.heaven7.java.data.mediator.Fields(value ={"
                    + buildFieldAnnotations(info.getPropertyInfos())+ "\n} , generateJsonAdapter = false)");
            if (!addExtendInterfaces(info, styleManager, elementFactory, psic)) {
                return;
            }
            styleManager.shortenClassReferences(parent.addAfter(psic, mPsiClass));
        }else{
            PsiDirectory dir = mPsiClass.getContainingFile().getContainingDirectory();
            PsiClass psiClass = createJavaFile(name, "@com.heaven7.java.data.mediator.Fields(value ={"
                                        + buildFieldAnnotations(info.getPropertyInfos())
                    + "\n}, generateJsonAdapter = false) "
                    + "public interface " + name + "{}");
            if (!addExtendInterfaces(info, styleManager, elementFactory, psiClass)) {
                return;
            }
            styleManager.shortenClassReferences(psiClass);
            dir.add(psiClass);
        }
    }

    private boolean addExtendInterfaces(ClassInfo info, JavaCodeStyleManager styleManager,
                                        PsiElementFactory elementFactory, PsiClass psic) {
        List<String> interfaces = info.getInterfaces();
        if(interfaces != null){
            PsiReferenceList list = psic.getExtendsList();
            if(list == null){
                Util.log(" psic.getExtendsList() == null");
                return false;
            }
            for(String inter : interfaces){
                PsiJavaCodeReferenceElement reference = elementFactory.createReferenceFromText(inter, psic);
                styleManager.shortenClassReferences(reference);
                list.add(reference);
            }
        }
        return true;
    }

    private String buildFieldAnnotations(List<PropertyInfo> infos) {
        final String fieldFlags = "com.heaven7.java.data.mediator.FieldFlags";
        StringBuilder sb = new StringBuilder();
        for(int i = 0 , size = infos.size() ; i < size ; i ++){
            sb.append("@com.heaven7.java.data.mediator.Field(");
            // propName, seriaName, type, complexType, flags, since, until
            PropertyInfo info = infos.get(i);
            Property property = info.getProperty();
            sb.append("propName = \"").append(property.getName()).append("\"");
            if(info.getSerializeName() != null && info.getSerializeName().length() > 0){
                sb.append(",seriaName = \"").append(info.getSerializeName()).append("\"");
            }
            if(property.getTypeString() != null && !property.isString()){
                sb.append(",type = ").append(property.getTypeString()).append(".class");
            }
            if(property.getComplexType() != 0){
                sb.append(",complexType = ").append(property.getComplexType());
            }
            if(info.isUseExpose()){
                boolean serialize = info.isSerialize();
                boolean deserialize = info.isDeserialize();
                sb.append(",flags = ")
                        .append(fieldFlags)
                        .append(".FLAGS_MAIN_SCOPES_2 | ")
                        .append(fieldFlags).append(".FLAG_EXPOSE_DEFAULT");
                if(!serialize){
                    sb.append("|").append(fieldFlags).append(".FLAG_EXPOSE_SERIALIZE_FALSE");
                }
                if(!deserialize){
                    sb.append("|").append(fieldFlags).append(".FLAG_EXPOSE_DESERIALIZE_FALSE");
                }
            }
            if(info.getSince() != 0){
                sb.append(",since = ").append(info.getSince());
            }
            if(info.getUntil() != 0){
                sb.append(",until = ").append(info.getUntil());
            }
            sb.append(")");
            if(i != size - 1){
                sb.append(",\n");
            }
        }
        return sb.toString();
    }

    private PsiClass createJavaFile(String javaFileName, @NonNls String text) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) PsiFileFactory.getInstance(mPsiClass.getProject()).createFileFromText(
                javaFileName + "." + JavaFileType.INSTANCE.getDefaultExtension(),
                JavaFileType.INSTANCE, text);
        PsiClass[] classes = psiJavaFile.getClasses();
        if (classes.length != 1) {
            throw new IncorrectOperationException("Incorrect class '" + text + "'");
        } else {
            PsiClass pc = classes[0];
            if (pc == null) {
                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null",
                        "com/intellij/psi/impl/PsiJavaParserFacadeImpl", "createJavaFile"));
            } else {
                return pc;
            }
        }
    }

    /** the target interface will generate
     * @return  null , if is in java/android */
    private static String getSuperClassAsInterfaceName(PsiClass mPsiClass){
        String name = mPsiClass.getQualifiedName();
        if(name == null || name.startsWith("java.") || name.startsWith("android.")){
            return null;
        }
        int lastDot = name.lastIndexOf(".");
        return name.substring(0, lastDot) + ".I" + name.substring(lastDot + 1);
    }

  /*  private void generateInternal() {
        final Project project = mPsiClass.getProject();
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        PsiElement parent = mPsiClass.getParent();
        if(parent instanceof PsiClass){
            Util.log("start nested class.");
            PsiClass psic = elementFactory.createInterface("IStudent");
            psic.getModifierList().addAnnotation("com.heaven7.java.data.mediator.Fields({})");
            styleManager.shortenClassReferences(parent.addAfter(psic, mPsiClass));
        }else{
            PsiDirectory dir = mPsiClass.getContainingFile().getContainingDirectory();
            PsiClass psiClass = createJavaFile("IStudent", "@com.heaven7.java.data.mediator.Fields({}) public interface IStudent{}");
            styleManager.shortenClassReferences(psiClass);
            dir.add(psiClass);
        }
    }*/
}
