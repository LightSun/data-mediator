package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.fillers.*;
import com.heaven7.java.data.mediator.compiler.replacer.CopyReplacer;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_COPYA;
import static com.heaven7.java.data.mediator.compiler.FieldData.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public final class Util {

    private static final String TAG = "Util";
    private static final HashMap<String, TypeInterfaceFiller> sFillerMap;
    private static final HashMap<String, BaseTypeReplacer> sReplacerMap;

    static {
        sFillerMap = new HashMap<String, TypeInterfaceFiller>();

        final TypeInterfaceFiller sCopyFiller = new TypeCopyableFiller();
        final TypeInterfaceFiller sResetFiller = new TypeResetableFiller();
        final TypeInterfaceFiller sShareFiller = new TypeShareableFiller();
        final TypeInterfaceFiller sSnapFiller = new TypeSnapableFiller();

        final TypeInterfaceFiller sParcelable = new TypeParcelableFiller();
        final TypeInterfaceFiller sSerializable = new TypeSerializableFiller();

        sFillerMap.put(sCopyFiller.getInterfaceName(), sCopyFiller);
        sFillerMap.put(sResetFiller.getInterfaceName(), sResetFiller);
        sFillerMap.put(sShareFiller.getInterfaceName(), sShareFiller);
        sFillerMap.put(sSnapFiller.getInterfaceName(), sSnapFiller);
        sFillerMap.put(sParcelable.getInterfaceName(), sParcelable);
        sFillerMap.put(sSerializable.getInterfaceName(), sSerializable);

        sReplacerMap = new HashMap<>();
        sReplacerMap.put(NAME_COPYA, new CopyReplacer());
    }

    public static MethodSpec.Builder createToStringBuilderForImpl(List<FieldData> dataList, boolean hasSuper){
        ClassName cn_objects = ClassName.get("com.heaven7.java.base.util", "Objects");
        MethodSpec.Builder toStringBuilder = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class);

        final List<FieldData> tempList = new ArrayList<>();
        for ( FieldData fd : dataList) {
            if (hasFlag(fd.getFlags(), FLAG_TO_STRING)) {
                tempList.add(fd);
            }
        }
        if(tempList.isEmpty()){
            toStringBuilder.addCode(CodeBlock.of("$T.ToStringHelper helper = $T.toStringHelper(this);\n",
                    cn_objects, cn_objects));
        }else {
            toStringBuilder.addCode(CodeBlock.of("$T.ToStringHelper helper = $T.toStringHelper(this)\n",
                    cn_objects, cn_objects));

            for (int size = tempList.size(), i = 0; i < size; i++) {
                FieldData fd = tempList.get(i);
                if (hasFlag(fd.getFlags(), FLAG_TO_STRING)) {
                    if (i == size - 1) {
                        if(fd.getComplexType() == FieldData.COMPLEXT_ARRAY){
                            toStringBuilder.addCode("    .add($S, $T.toString(this.$N));\n",
                                    fd.getPropertyName(), Arrays.class, fd.getPropertyName());
                        }else{
                            toStringBuilder.addCode("    .add($S, $T.valueOf(this.$N));\n",
                                    fd.getPropertyName(), String.class, fd.getPropertyName());
                        }
                    } else {
                        if(fd.getComplexType() == FieldData.COMPLEXT_ARRAY){
                            toStringBuilder.addCode("    .add($S, $T.toString(this.$N))\n",
                                    fd.getPropertyName(), Arrays.class, fd.getPropertyName());
                        }else{
                            toStringBuilder.addCode("    .add($S, $T.valueOf(this.$N))\n",
                                    fd.getPropertyName(), String.class, fd.getPropertyName());
                        }
                    }
                }
            }
        }
        if(hasSuper){
            toStringBuilder.addStatement("return helper.toString() + $S + super.toString()", "#_@super_#");
        }else {
            toStringBuilder.addStatement("return helper.toString()");
        }
        return toStringBuilder;
    }

    public static void getTypeName(FieldData field, TypeInfo info) {
        final FieldData.TypeCompat typeCompat = field.getTypeCompat();
        TypeName rawTypeName = typeCompat.getInterfaceTypeName();
        switch (field.getComplexType()) {
            case FieldData.COMPLEXT_ARRAY:
                info.setTypeName(ArrayTypeName.of(rawTypeName));
                info.setParamName(field.getPropertyName() + "1");
                break;

            case FieldData.COMPLEXT_LIST:
                info.setParamName(field.getPropertyName() + "1");
                TypeName typeName = ParameterizedTypeName.get(ClassName.get(List.class),
                        rawTypeName.box());
                info.setTypeName(typeName);
                break;

            default:
                info.setTypeName(rawTypeName);
                info.setParamName(field.getPropertyName() + "1");
                break;
        }
    }

    public static void setLogPrinter(ProcessorPrinter pp) {
        for (TypeInterfaceFiller filler : sFillerMap.values()) {
            filler.setLogPrinter(pp);
        }
        for (BaseTypeReplacer replacer : sReplacerMap.values()) {
            replacer.setPrinter(pp);
        }
    }

    public static void reset() {
        for (BaseTypeReplacer replacer : sReplacerMap.values()) {
            replacer.reset();
        }
    }

    //according to the interface that field apply to.
    public static Map<String, List<FieldData>> groupFieldByInterface(List<FieldData> mFields) {
        HashMap<String, List<FieldData>> map = new HashMap<>();
        final Collection<TypeInterfaceFiller> values = sFillerMap.values();
        for (FieldData fd : mFields) {
            for (TypeInterfaceFiller filler : values) {
                filler.fill(fd, map);
            }
        }
        return map;
    }

    //here mirror is interface
    public static MethodSpec.Builder[] getInterfaceMethodBuilders(TargetClassInfo info, TypeName returnReplace,
                                                                  TypeMirror mirror, ProcessorPrinter pp) {
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        final String interfaceName = te.getQualifiedName().toString();
        pp.note(TAG, "getInterfaceMethodBuilders", "interface name = " + interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }
        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            //may have inner class/interface/field
            if (e instanceof ExecutableElement) {
                ExecutableElement ee = (ExecutableElement) e;
                MethodSpec.Builder builder = overriding(info, interfaceName, ee, pp, returnReplace)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
                builders[i++] = builder;
            }
        }
        return builders;
    }

    /**
     * get field builder.
     *
     * @param pkgName   the package name
     * @param classname the class name
     * @param tc        the type element wrapper.
     * @param map       the map.
     * @param superFlagsForParent
     * @return the field builders.
     */
    public static MethodSpec.Builder[] getImplClassConstructBuilders(String pkgName, String classname,
                                                                     TypeCompat tc, Map<String, List<FieldData>> map,
                                                                     boolean hasSuperClass, int superFlagsForParent) {
        final TypeElement te = tc.getElementAsType();
        String interfaceIname = te.getQualifiedName().toString();
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceIname);
        if (filler != null) {
            final List<FieldData> datas = map.get(filler.getInterfaceName());
            final String interName = te.getSimpleName().toString();
            return filler.createConstructBuilder(pkgName, interName, classname,
                    datas, hasSuperClass, superFlagsForParent);
        }
        return null;
    }

    //te is the target element to generate  Proxy.
    public static List<MethodSpec.Builder> getProxyClassMethodBuilders(TargetClassInfo info, TypeElement te,
                                                                   Types types, ProcessorPrinter pp){

        //the method builders for super interface.
        final List<MethodSpec.Builder> methodBuilders =  new ArrayList<>();
        final ClassName cn_inter = ClassName.get(info.getPackageName(), info.getDirectParentInterfaceName());

        List<? extends TypeMirror> interfaces = getAttentionInterfaces(te, types, pp);
        pp.note(TAG, "getProxyClassMethodBuilders", "te : " + te + " ,superinterfaces = " + interfaces);
        for(TypeMirror tm : interfaces){
            TypeCompat tc = new TypeCompat(types, tm);
            TypeElement type = tc.getElementAsType();
            String interfaceName = type.getQualifiedName().toString();
            final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);

            //get all method element
            final List<? extends Element> list = type.getEnclosedElements();
            for(Element e : list){
                if (!(e instanceof ExecutableElement)) {
                    continue;
                }
                ExecutableElement ee = (ExecutableElement) e;
                MethodSpec.Builder builder = overriding(info, interfaceName, ee, pp, cn_inter)
                        .addModifiers(Modifier.PUBLIC);
                if(filler != null){
                    filler.buildProxyMethod(builder, ee, cn_inter);
                    methodBuilders.add(builder);
                }
            }
        }
        return methodBuilders;
    }

    /**
     * get the super interface flags for parent
     * @param te the current type element
     * @param pp the log printer
     * @return the flags.
     */
    public static int getSuperInterfaceFlagForParent(TypeElement te,
                             Types types, ProcessorPrinter pp){
        final String tag = te.getSimpleName().toString();
        List<? extends TypeMirror> interfaces = te.getInterfaces();
        for(TypeMirror tm: interfaces) {
           // pp.note(TAG, "getSuperInteraceFlagForParent_" + tag, "TypeMirror : " + tm);
            TypeCompat tc = new TypeCompat(types, tm);
            tc.replaceIfNeed(pp);
            pp.note(TAG, "getSuperInteraceFlagForParent_" + tag, "TypeMirror : " + tm
                    + " , replace_inter = " + tc.getReplaceInterfaceTypeName());
            if(tc.getReplaceInterfaceTypeName() != null){
                //we want.
                int sum = 0;
                final Set<Integer> flags = getSuperInterfaceFlags(tag,
                        tc.getElementAsType(), types, pp);
                for(Integer val : flags){
                    sum += val;
                }
                return sum;
            }
        }
        return 0;
    }
    private static Set<Integer> getSuperInterfaceFlags(String tag, TypeElement te,
                                                       Types types, ProcessorPrinter pp){
        final Set<Integer> set = new HashSet<>();
        final List<? extends TypeMirror> interfaces = te.getInterfaces();
        for(TypeMirror tm: interfaces) {
            pp.note(TAG + "_" + tag, "getSuperInterfaceFlags", "TypeMirror : " + tm);
            TypeCompat tc = new TypeCompat(types, tm);
            TypeElement newTe = tc.getElementAsType();
            String interfaceName = newTe.getQualifiedName().toString();
            final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);
            if (filler != null) {
                pp.note(tag, "getSuperInterfaceFlags", "has interface flag = " + interfaceName);
                set.add(filler.getInterfaceFlag());
            }else{
                set.addAll(getSuperInterfaceFlags(tag, newTe, types, pp));
            }
        }
        return set;
    }

    public static List<? extends TypeMirror> getAttentionInterfaces(TypeElement te,
                           Types types, ProcessorPrinter pp){
        return getAttentionInterfaces(te, types , new ArrayList<String>(), pp);
    }
    /**
     * get the focus/attention interface ,like Parcelable, IReset and etc..
     * @param te the type element of current visit
     *           which is annotated by {@literal @}{@linkplain com.heaven7.java.data.mediator.Fields}.
     * @param types the type util
     * @return the all interfaces proxy need.
     */
    private static List<? extends TypeMirror> getAttentionInterfaces(TypeElement te, Types types,
                                                                     List<String> existInterfaces, ProcessorPrinter pp) {

        pp.note(TAG, "getAttentionInterfaces", "start >>> te : " + te);
        final List<TypeMirror> list = new ArrayList<>();

        List<? extends TypeMirror> interfaces = te.getInterfaces();
        for(TypeMirror tm: interfaces){
            pp.note(TAG, "getAttentionInterfaces", "TypeMirror : " + tm);
            TypeCompat tc = new TypeCompat(types, tm);
            TypeElement newTe = tc.getElementAsType();
            String interfaceName = newTe.getQualifiedName().toString();
            if(sFillerMap.get(interfaceName) != null){
                if(!existInterfaces.contains(interfaceName)) {
                    list.add(tm);
                    existInterfaces.add(interfaceName);
                }
            }else{
                list.addAll(getAttentionInterfaces(newTe, types, existInterfaces, pp));
            }
        }
        pp.note(TAG, "getAttentionInterfaces", "end >>> te : " + te);
        return list;
    }

    /**
     * get field builder.
     *
     * @param pkgName   the package name
     * @param classname the class name
     * @param tc        the type element wrapper.
     * @param map       the map.
     * @return the field builders.
     */
    public static FieldSpec.Builder[] getImplClassFieldBuilders(String pkgName, String classname,
                                                                TypeCompat tc, Map<String, List<FieldData>> map) {
        final TypeElement te = tc.getElementAsType();
        String interfaceIname = te.getQualifiedName().toString();
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceIname);
        if (filler != null) {
            final List<FieldData> datas = map.get(filler.getInterfaceName());
            final String interName = te.getSimpleName().toString();
            return filler.createFieldBuilder(pkgName, interName, classname, datas);
        }
        return null;
    }

    //classname  : current class , here mirror is interface .can't be primitive.
    //tc indicate interface
    //superFlagsForParent  super interface flags for parent
    public static MethodSpec.Builder[] getImplClassMethodBuilders(TargetClassInfo info,
                                                                  TypeName returnReplace, TypeCompat tc,
                                           ProcessorPrinter pp, Map<String, List<FieldData>> map,
                                        boolean usedSuperClass, int superFlagsForParent) {
        final TypeElement te = tc.getElementAsType();
        final String interfaceName = te.getQualifiedName().toString();
        pp.note(TAG, "getImplClassMethodBuilders() >>> interface name = " + interfaceName);
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }

        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            if (!(e instanceof ExecutableElement)) {
                continue;
            }
            // may have inner class/interface/field.so 'e' may not be method
            ExecutableElement ee = (ExecutableElement) e;
            MethodSpec.Builder builder = overriding(info,interfaceName,  ee, pp, returnReplace)
                    .addModifiers(Modifier.PUBLIC);
            if (filler != null) {
                final List<FieldData> datas = map.get(filler.getInterfaceName());
                final String interName = te.getSimpleName().toString();
                pp.note(TAG , "getImplClassMethodBuilders", "interface name = " + interName);
                pp.note(TAG , "getImplClassMethodBuilders","field datas = " + datas);
                filler.buildMethodStatement(info.getPackageName(),
                        info.getDirectParentInterfaceName(), info.getCurrentClassname(),
                        ee, builder, datas, usedSuperClass , superFlagsForParent);
            }
            builders[i++] = builder;
        }
        return builders;
    }

    //copy from javapoet and change some

    /**
     * Returns a new method spec builder that overrides {@code method}.
     *
     * @param target     the target class info
     * @param returnTypeReplace the replaced return type .if super interface use param type(eg: T).
     *                          <p>This will copy its visibility modifiers, type parameters, return type, name, parameters, and
     *                          throws declarations. An {@link Override} annotation will be added.
     */
    public static MethodSpec.Builder overriding(TargetClassInfo target, String interfaceName ,
                                                ExecutableElement method, ProcessorPrinter pp,
                                                TypeName returnTypeReplace) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }

        Set<Modifier> modifiers = method.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)
                || modifiers.contains(Modifier.FINAL)
                || modifiers.contains(Modifier.STATIC)) {
            throw new IllegalArgumentException("cannot override method with modifiers: " + modifiers);
        }

        final String methodName = method.getSimpleName().toString();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName);

        methodBuilder.addAnnotation(ClassName.get(Override.class));
        for (AnnotationMirror mirror : method.getAnnotationMirrors()) {
            AnnotationSpec annotationSpec = AnnotationSpec.get(mirror);
            if (annotationSpec.type.equals(ClassName.get(Override.class)))
                continue;
            methodBuilder.addAnnotation(annotationSpec);
        }

        modifiers = new LinkedHashSet<>(modifiers);
        modifiers.remove(Modifier.ABSTRACT);
        methodBuilder.addModifiers(modifiers);

        for (TypeParameterElement typeParameterElement : method.getTypeParameters()) {
            TypeVariable var = (TypeVariable) typeParameterElement.asType();
            methodBuilder.addTypeVariable(TypeVariableName.get(var));
        }

        final BaseTypeReplacer replacer = sReplacerMap.get(interfaceName);
        if(replacer != null) {
            TypeName typeName = replacer.replaceReturnType(target.getPackageName(), target.getDirectParentInterfaceName(),
                    target.getCurrentClassname(), target.getSuperInterfaces(), target.getSuperClass(), method);
            methodBuilder.returns(typeName);
        }else {
            TypeMirror returnType = method.getReturnType();
            switch (returnType.getKind()) {
                case TYPEVAR: //泛型
                    methodBuilder.returns(returnTypeReplace);
                    break;

                default:
                    methodBuilder.returns(TypeName.get(returnType));
            }
        }
        /*
         * class Taco extends Comparable<Taco>
         */
       /* final ParameterizedTypeName typeName = ParameterizedTypeName.get(
                ClassName.get(Comparable.class), ClassName.get("com.squareup.tacos", "Taco"));*/
        //pp.note("return type is " + returnType.getKind()); //TYPEVAR
        final boolean isParcelable = interfaceName.equals("android.os.Parcelable");
        final boolean isCopy = interfaceName.equals("com.heaven7.java.data.mediator.ICopyable");

        List<? extends VariableElement> parameters = method.getParameters();
        for (VariableElement parameter : parameters) {
            final TypeMirror asType = parameter.asType();
            //replace type if need.
            final TypeName type;
            if(replacer != null){
                type = replacer.replaceParameterType(target.getPackageName(), target.getDirectParentInterfaceName(),
                        target.getCurrentClassname(), target.getSuperInterfaces(),
                        target.getSuperClass(), method, parameter);
            }else{
                type = TypeName.get(asType);
            }
           // TypeName type = TypeName.get(asType);
            String name = parameter.getSimpleName().toString();
            //for parcelable.
            if (isParcelable) {
                if (methodName.equals("writeToParcel")) {
                    if (asType.toString().equals("android.os.Parcel")) {
                        name = "dest";
                    } else if (asType.toString().equals("int")) {
                        name = "flags";
                    }
                }
            } else if (isCopy) {
                if (methodName.equals("copyTo")) {
                    name = "out";
                }
            }
            Set<Modifier> parameterModifiers = parameter.getModifiers();
            ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(type, name)
                    .addModifiers(parameterModifiers.toArray(new Modifier[parameterModifiers.size()]));
            for (AnnotationMirror mirror : parameter.getAnnotationMirrors()) {
                parameterBuilder.addAnnotation(AnnotationSpec.get(mirror));
            }
            methodBuilder.addParameter(parameterBuilder.build());
        }
        methodBuilder.varargs(method.isVarArgs());

        for (TypeMirror thrownType : method.getThrownTypes()) {
            methodBuilder.addException(TypeName.get(thrownType));
        }

        return methodBuilder;
    }

    //serializeable.
    static Modifier[] getFieldModifier(FieldData fieldData) {
        final int flags = fieldData.getFlags();
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PRIVATE);
        if ((flags & FLAG_TRANSIENT) == FLAG_TRANSIENT) {
            modifiers.add(Modifier.TRANSIENT);
        }
        if ((flags & FLAG_VOLATILE) == FLAG_VOLATILE) {
            modifiers.add(Modifier.VOLATILE);
        }
        return modifiers.toArray(new Modifier[modifiers.size()]);
    }

    public static boolean hasFlag(int flags, int require) {
        if (flags == 0 || require == 0) {
            return false;
        }
        return (flags & require) == require;
    }

    /**
     * get property name. eg: name to Name(get, set)
     *
     * @param fd the field data
     * @return the changed name for get/set method
     */
    public static String getPropNameForMethod(FieldData fd) {
        String prop = fd.getPropertyName();
        if (prop == null || "".equals(prop.trim())) {
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0, 1)
                .toUpperCase()
                .concat(prop.substring(1));
    }

    /**
     * get parameter name . eg: int to int1 Object to Object1
     *
     * @param name the raw name of param
     * @return the changed parameter name.
     */
    public static String getParamName(String name) {
        return name.substring(0, 1).toLowerCase()
                .concat(name.substring(1))
                .concat("1");
    }

    public static String getParamName(TypeMirror tm) {
        final String s = tm.toString();
        String name = s.contains(".") ? s.substring(s.lastIndexOf(".") + 1) : s;
        return name.substring(0, 1).toLowerCase()
                .concat(name.substring(1))
                .concat("1");
    }

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        t.printStackTrace(pw);
        Throwable cause = t.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.flush();
        String data = sw.toString();
        pw.close();
        return data;
    }

    public static Object getInitValue(FieldData fd) {
        switch (fd.getComplexType()) {
            case COMPLEXT_ARRAY:
            case COMPLEXT_LIST:
                return null;
        }
        final TypeMirror tm = fd.getTypeCompat().getTypeMirror();
        TypeKind kind = tm.getKind();
        switch (kind) {
            case INT:
            case LONG:
            case SHORT:
            case BYTE:
                return 0;

            case BOOLEAN:
                return false;

            case FLOAT:
                return 0f;

            case DOUBLE:
                return 0d;

            case CHAR:
                return Character.MIN_VALUE;
            default:
                return null;
        }

    }

    public static void test(TypeMirror mirror, ProcessorPrinter pp) {
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        Name paramType = te.getQualifiedName();
        pp.note(TAG, "test", "paramType = " + paramType.toString());

        final List<? extends Element> list = te.getEnclosedElements();
        for (Element e : list) {
            ExecutableElement ee = (ExecutableElement) e;
            pp.note(TAG, "test", "ee_getSimpleName: " + ee.getSimpleName());
            pp.note(TAG, "test", "ee_return: " + ee.getReturnType());
            pp.note(TAG, "test", "ee_getTypeParameters: " + ee.getTypeParameters());
            pp.note(TAG, "test", "ee_getThrownTypes: " + ee.getThrownTypes());
            // pp.note("ee_getReceiverType: " + ee.getReceiverType());
        }
        pp.note(TAG, "test", "getTypeParameters = " + te.getTypeParameters());
        pp.note(TAG, "test", "getEnclosedElements = " + list);
        pp.note(TAG, "test", "getEnclosedElements__2 = " + Arrays.toString(
                list.get(0).getClass().getInterfaces()));
        pp.note(TAG, "test", "getEnclosingElement = " + te.getEnclosingElement());
        // pp.note("test() >>> getInterfaces = " + te.getInterfaces());
    }
}
