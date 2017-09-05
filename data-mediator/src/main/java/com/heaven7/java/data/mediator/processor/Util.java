package com.heaven7.java.data.mediator.processor;

import com.heaven7.java.data.mediator.FieldData;
import com.heaven7.java.data.mediator.TypeInterfaceFiller;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.heaven7.java.data.mediator.FieldData.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class Util {

    public static final String STR_PROP_NAME = "propName";
    public static final String STR_SERIA_NAME = "seriaName";
    public static final String STR_FLAGS = "flags";
    public static final String STR_TYPE = "type";
    public static final String STR_COMPLEXT_TYPE = "complexType";

    public static final String NAME_COPYA = "com.heaven7.java.data.mediator.ICopyable";
    public static final String NAME_RESET = "com.heaven7.java.data.mediator.IResetable";
    public static final String NAME_SHARE = "com.heaven7.java.data.mediator.IShareable";
    public static final String NAME_SNAP = "com.heaven7.java.data.mediator.ISnapable";

    private static final HashMap<String, TypeInterfaceFiller> sFillerMap;

    static {
        sFillerMap = new HashMap<>();
        final TypeInterfaceFiller sCopyFiller = new TypeCopyableFiller();
        final TypeInterfaceFiller sResetFiller = new TypeResetableFiller();
        final TypeInterfaceFiller sShareFiller = new TypeShareableFiller();
        final TypeInterfaceFiller sSnapFiller = new TypeSnapableFiller();
        sFillerMap.put(sCopyFiller.getInterfaceName(), sCopyFiller);
        sFillerMap.put(sResetFiller.getInterfaceName(), sResetFiller);
        sFillerMap.put(sShareFiller.getInterfaceName(), sShareFiller);
        sFillerMap.put(sSnapFiller.getInterfaceName(), sSnapFiller);
    }

    public static void applyType(FieldData data, TypeMirror type, ProcessorPrinter pp)
            throws ClassNotFoundException {
        pp.note("============== start applyType() ============ ");
        TypeKind kind = type.getKind();
        switch (kind) {
            case INT:
                data.setType(int.class);
                break;

            case LONG:
                data.setType(long.class);
                break;
            case SHORT:
                data.setType(short.class);
                break;
            case BYTE:
                data.setType(byte.class);
                break;
            case BOOLEAN:
                data.setType(boolean.class);
                break;

            case FLOAT:
                data.setType(float.class);
                break;
            case DOUBLE:
                data.setType(double.class);
                break;

            case CHAR:
                data.setType(char.class);
                break;

            default:
                data.setType(Class.forName(type.toString().trim()));
                break;
        }
    }


    public static void setLogPrinter(ProcessorPrinter pp) {
        for (TypeInterfaceFiller filler : sFillerMap.values()) {
            filler.setLogPrinter(pp);
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
    public static MethodSpec.Builder[] getInterfaceMethodBuilders(TypeName returnReplace,
                                                                  TypeMirror mirror, ProcessorPrinter pp) {
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        String interfaceName = te.getQualifiedName().toString();
        pp.note("applyInterface() >>> interface name = " + interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }
        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            ExecutableElement ee = (ExecutableElement) e;
            MethodSpec.Builder builder = overriding(ee, pp, returnReplace)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
            builders[i++] = builder;
        }
        return builders;
    }

    //classname  : current class
    public static MethodSpec.Builder[] getImplClassMethodBuilders(String pkgName, String classname,
                                                                  TypeName returnReplace, TypeMirror mirror, ProcessorPrinter pp,
                                                                  Map<String, List<FieldData>> map) {
        pp.note("map = " + map);
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        String interfaceName = te.getQualifiedName().toString();
        pp.note("applyInterface() >>> interface name = " + interfaceName);
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if (list == null || list.size() == 0) {
            return null;
        }

        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for (Element e : list) {
            ExecutableElement ee = (ExecutableElement) e;
            MethodSpec.Builder builder = overriding(ee, pp, returnReplace)
                    .addModifiers(Modifier.PUBLIC);
            if (filler != null) {
                final List<FieldData> datas = map.get(filler.getInterfaceName());
                final String interName = te.getSimpleName().toString();
                pp.note("interface name = " + interName);
                pp.note("field datas = " + datas);
                filler.buildMethodStatement(pkgName, interName, classname,
                        ee, builder, datas);
            }
            builders[i++] = builder;
        }
        return builders;
    }

    //copy from javapoet and change some

    /**
     * Returns a new method spec builder that overrides {@code method}.
     *
     * @param returnTypeReplace the replaced return type .if super interface use param type(eg: T).
     *                          <p>This will copy its visibility modifiers, type parameters, return type, name, parameters, and
     *                          throws declarations. An {@link Override} annotation will be added.
     */
    public static MethodSpec.Builder overriding(ExecutableElement method, ProcessorPrinter pp,
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

        String methodName = method.getSimpleName().toString();
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

        TypeMirror returnType = method.getReturnType();
        switch (returnType.getKind()) {
            case TYPEVAR: //泛型
                methodBuilder.returns(returnTypeReplace);
                break;

            default:
                methodBuilder.returns(TypeName.get(returnType));
        }
        /*
         * class Taco extends Comparable<Taco>
         */
       /* final ParameterizedTypeName typeName = ParameterizedTypeName.get(
                ClassName.get(Comparable.class), ClassName.get("com.squareup.tacos", "Taco"));*/
        //pp.note("return type is " + returnType.getKind()); //TYPEVAR

        List<? extends VariableElement> parameters = method.getParameters();
        for (VariableElement parameter : parameters) {
            TypeName type = TypeName.get(parameter.asType());
            String name = parameter.getSimpleName().toString();
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

    //serializeable.
    static javax.lang.model.element.Modifier[] getFieldModifier(FieldData fieldData) {
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
        return (flags & require) == require;
    }

    /**
     * get property name. eg: name to Name(get, set)
     *
     * @param prop the target name
     * @return the changed name for get/set method
     */
    public static String getPropNameForMethod(String prop) {
        if (prop == null || "".equals(prop.trim())) {
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0, 1).toUpperCase().concat(prop.substring(1));
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
        final Class<?> clazz = fd.getType();
        if (clazz == boolean.class) {
            return false;
        } else if (clazz == byte.class || clazz == short.class || clazz == int.class
                || clazz == long.class) {
            return 0;
        } else if (clazz == char.class) {
            return Character.MIN_VALUE;
        } else if (clazz == float.class) {
            return 0f;
        } else if (clazz == double.class) {
            return 0d;
        } else {
            return null;
        }
    }
}
