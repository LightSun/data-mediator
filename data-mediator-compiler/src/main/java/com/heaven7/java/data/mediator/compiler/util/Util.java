package com.heaven7.java.data.mediator.compiler.util;

import com.heaven7.java.data.mediator.compiler.*;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.FieldData.*;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public final class Util {

    public static MethodSpec.Builder createToStringBuilderForImpl(List<FieldData> dataList, boolean hasSuper){
        ClassName cn_objects = ClassName.get(PKG_JAVA_BASE_UTIL, SIMPLE_NAME_ObJECTS);
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
        //special handle FD_SELECTABLE
        if(field == DataMediatorConstants.FD_SELECTABLE){
            info.setParamName(field.getPropertyName());
            info.setSimpleTypeName(TypeName.BOOLEAN);
            info.setTypeName(TypeName.BOOLEAN);
            return;
        }
        final TypeCompat typeCompat = field.getTypeCompat();
        TypeName rawTypeName = typeCompat.getTypeName();
        info.setSimpleTypeName(rawTypeName);
        info.setParamName(field.getPropertyName() + "1");

        switch (field.getComplexType()) {
            case FieldData.COMPLEXT_SPARSE_ARRAY: {
                TypeName typeName = ParameterizedTypeName.get(
                        ClassName.get(PKG_JAVA_BASE_UTIL, SIMPLE_NAME_SPARSE_ARRAY),
                        rawTypeName.box());
                info.setTypeName(typeName);
            }
                break;

            case FieldData.COMPLEXT_ARRAY:
                info.setTypeName(ArrayTypeName.of(rawTypeName));
                break;

            case FieldData.COMPLEXT_LIST:
                TypeName typeName = ParameterizedTypeName.get(ClassName.get(List.class),
                        rawTypeName.box());
                info.setTypeName(typeName);
                break;

            default:
                info.setTypeName(rawTypeName);
                break;
        }
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

        final BaseTypeReplacer replacer = OutInterfaceManager.getTypeReplacer(interfaceName);
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
        final boolean isParcelable = interfaceName.equals(DataMediatorConstants.NAME_PARCELABLE);
        final boolean isCopy = interfaceName.equals(DataMediatorConstants.NAME_COPYA);
        final boolean isSelectable = interfaceName.equals(DataMediatorConstants.NAME_SELECTABLE);

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
            }else if(isSelectable){
                if (methodName.equals("setSelected")) {
                    name = "selected";
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

    public static String getTypeAdapterName(String type){
       return TYPE_ADAPTER_PREFIX + type + TYPE_ADAPTER_PREFIX + "TypeAdapter";
    }

    //serializeable.
    public static Modifier[] getFieldModifier(FieldData fieldData) {
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

    public static void addInitStatement(FieldData fd, MethodSpec.Builder builder){
        switch (fd.getComplexType()) {
            case COMPLEXT_ARRAY:
            case COMPLEXT_LIST:
            case COMPLEXT_SPARSE_ARRAY:
                builder.addStatement("this.$N = null", fd.getPropertyName());
                break;

            default:{
                switch (fd.getTypeCompat().toString()){
                    case NAME_float:
                        builder.addStatement("this.$N = 0f", fd.getPropertyName());
                        break;

                    case NAME_double:
                        builder.addStatement("this.$N = 0d", fd.getPropertyName());
                        break;

                    default:
                        builder.addStatement("this.$N = $L", fd.getPropertyName(), Util.getInitValue(fd));
                }
            }

        }
    }

    private static Object getInitValue(FieldData fd) {
        switch (fd.getComplexType()) {
            case COMPLEXT_ARRAY:
            case COMPLEXT_LIST:
            case COMPLEXT_SPARSE_ARRAY:
                return null;
        }
        if(fd == DataMediatorConstants.FD_SELECTABLE){
            return false;
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

            //have a bug when in generate code. javapoet.
            case FLOAT:
                return 0.0f;

            case DOUBLE:
                return 0.0d;

            case CHAR:
                return 0;
            default:
                return null;
        }
    }
}
