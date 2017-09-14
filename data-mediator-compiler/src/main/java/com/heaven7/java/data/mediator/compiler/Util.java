package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.fillers.*;
import com.heaven7.java.data.mediator.compiler.replacer.CopyReplacer;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.FieldData.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public final class Util {

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
                info.setParamName(getParamName(typeCompat.getTypeMirror()));
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
        pp.note("getInterfaceMethodBuilders() >>> interface name = " + interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }
        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            pp.note("getInterfaceMethodBuilders >>> interface method: kind = " + e.getKind()
                    + " ," + e.getSimpleName() + ", e = " + e);
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
     * @return the field builders.
     */
    public static MethodSpec.Builder[] getImplClassConstructBuilders(String pkgName, String classname,
                                                                     TypeCompat tc, Map<String, List<FieldData>> map,
                                                                     boolean hasSuperClass) {
        final TypeElement te = tc.getElementAsType();
        String interfaceIname = te.getQualifiedName().toString();
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceIname);
        if (filler != null) {
            final List<FieldData> datas = map.get(filler.getInterfaceName());
            final String interName = te.getSimpleName().toString();
            return filler.createConstructBuilder(pkgName, interName, classname, datas, hasSuperClass);
        }
        return null;
    }

    //te is the target element to generate  Proxy.
    public static List<MethodSpec.Builder> getProxyClassMethodBuilders(TargetClassInfo info, TypeElement te,
                                                                   Types types, ProcessorPrinter pp){

        //the method builders for super interface.
        final List<MethodSpec.Builder> methodBuilders =  new ArrayList<>();
        final ClassName cn_inter = ClassName.get(info.getPackageName(), info.getDirectParentInterfaceName());

        List<? extends TypeMirror> interfaces = getProxyWantInterfaces(te, types);
        for(TypeMirror tm : interfaces){
            TypeCompat tc = new TypeCompat(types, tm);
            TypeElement type = tc.getElementAsType();
            String interfaceName = type.getQualifiedName().toString();
            final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);

            //get all method element
            final List<? extends Element> list = type.getEnclosedElements();
            if (list == null || list.size() == 0) {
                return methodBuilders;
            }
            for(Element e : list){
                pp.note("getProxyClassMethodBuilders >>> interface method: kind = " + e.getKind()
                        + " ," + e.getSimpleName() + ", e = " + e);
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

    private static List<? extends TypeMirror> getProxyWantInterfaces(TypeElement te, Types types){
        return getProxyWantInterfaces(te, types , new ArrayList<String>());
    }
    /**
     * get the interface that proxy want.
     * @param te the type element of current visit which is annotated by {@literal @}{@linkplain com.heaven7.java.data.mediator.Fields}.
     * @param types the type util
     * @return the all interfaces proxy need.
     */
    private static List<? extends TypeMirror> getProxyWantInterfaces(TypeElement te, Types types, List<String> existInterfaces) {

        final List<TypeMirror> list = new ArrayList<>();

        List<? extends TypeMirror> interfaces = te.getInterfaces();
        for(TypeMirror tm: interfaces){
            TypeCompat tc = new TypeCompat(types, tm);
            TypeElement newTe = tc.getElementAsType();
            String interfaceName = newTe.getQualifiedName().toString();
            if(sFillerMap.get(interfaceName) != null){
                if(!existInterfaces.contains(interfaceName)) {
                    list.add(tm);
                    existInterfaces.add(interfaceName);
                }
            }else{
                list.addAll(getProxyWantInterfaces(newTe, types, existInterfaces));
            }
        }
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
    public static MethodSpec.Builder[] getImplClassMethodBuilders(TargetClassInfo info,
                                                                  TypeName returnReplace, TypeCompat tc,
                                                                  ProcessorPrinter pp, Map<String, List<FieldData>> map,
                                                                  boolean usedSuperClass) {
        pp.note("map = " + map);
        final TypeElement te = tc.getElementAsType();
        final String interfaceName = te.getQualifiedName().toString();
        pp.note("getImplClassMethodBuilders() >>> interface name = " + interfaceName);
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }

        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            pp.note("getImplClassMethodBuilders >>> interface method: kind = " + e.getKind()
                    + " ," + e.getSimpleName() + ", e = " + e);
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
                pp.note("interface name = " + interName);
                pp.note("field datas = " + datas);
                filler.buildMethodStatement(info.getPackageName(), info.getDirectParentInterfaceName(), info.getCurrentClassname(),
                        ee, builder, datas, usedSuperClass);
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
        pp.note("test() >>> paramType = " + paramType.toString());

        final List<? extends Element> list = te.getEnclosedElements();
        for (Element e : list) {
            ExecutableElement ee = (ExecutableElement) e;
            pp.note("ee_getSimpleName: " + ee.getSimpleName());
            pp.note("ee_return: " + ee.getReturnType());
            pp.note("ee_getTypeParameters: " + ee.getTypeParameters());
            pp.note("ee_getThrownTypes: " + ee.getThrownTypes());
            // pp.note("ee_getReceiverType: " + ee.getReceiverType());
        }
        pp.note("test() >>> getTypeParameters = " + te.getTypeParameters());
        pp.note("test() >>> getEnclosedElements = " + list);
        pp.note("test() >>> getEnclosedElements__2 = " + Arrays.toString(
                list.get(0).getClass().getInterfaces()));
        pp.note("test() >>> getEnclosingElement = " + te.getEnclosingElement());
        // pp.note("test() >>> getInterfaces = " + te.getInterfaces());
    }

    public static void applyType(FieldData data, TypeMirror type, ProcessorPrinter pp)
            throws ClassNotFoundException {
        pp.note("============== start applyType() ============ ");
        /*
         * 如果这个type不是java和android系统自带的。很可能异常。
         * com.heaven7.data.mediator.demo.TestBind（依赖的注解）  正在处理...这里肯定异常
         */
       /* default:
            try {
                data.setType(Class.forName(type.toString().trim()));
            } catch (ClassNotFoundException e) {
                pp.note("can't find class . " + type.toString());
            }
            break;*/
    }
}
