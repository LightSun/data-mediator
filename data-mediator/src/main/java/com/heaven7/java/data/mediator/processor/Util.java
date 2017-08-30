package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.*;

import static com.heaven7.java.data.mediator.processor.FieldData.FLAG_TRANSIENT;
import static com.heaven7.java.data.mediator.processor.FieldData.FLAG_VOLATILE;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
/*public*/ class Util {

    //here: mirror - interface
    public static MethodSpec.Builder[] getInterfaceMethodBuilders(String interfaceName, TypeMirror mirror, ProcessorPrinter pp,
                                                                  boolean abstractMethod){
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        Name paramType = te.getQualifiedName();
        pp.note("applyInterface() >>> paramType = " + paramType.toString());
        //get all method element
        final List<? extends Element> list = te.getEnclosedElements();
        if(list == null || list.size() == 0){
            return null;
        }
        MethodSpec.Builder[] builders = new MethodSpec.Builder[list.size()];
        int i = 0;
        for(Element e: list){
            ExecutableElement ee = (ExecutableElement) e;
            MethodSpec.Builder builder = overriding(ee, pp, interfaceName)
           // MethodSpec.Builder builder = MethodSpec.overriding(ee)
                    //.addTypeVariable(TypeVariableName.get(paramTypeName))
                    .addModifiers(Modifier.PUBLIC);
            if(abstractMethod){
                builder.addModifiers(Modifier.ABSTRACT);
            }
            builders[i++] = builder;
        }
        return builders;
    }

    //copy from javapoet and change some
    /**
     * Returns a new method spec builder that overrides {@code method}.
     *
     * <p>This will copy its visibility modifiers, type parameters, return type, name, parameters, and
     * throws declarations. An {@link Override} annotation will be added.
     */
    public static MethodSpec.Builder overriding(ExecutableElement method, ProcessorPrinter pp, String nameToReplaceTypeVar){
        if(method == null){
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
        switch (returnType.getKind()){
            case TYPEVAR:
                try {
                    Class<?> aClass = Class.forName(nameToReplaceTypeVar);
                    pp.note("find class for TYPEVAR: " + aClass );
                    methodBuilder.returns(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    methodBuilder.returns(TypeName.get(returnType));
                }
                break;

             default:
                 methodBuilder.returns(TypeName.get(returnType));
        }
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

    public static void test(TypeMirror mirror, ProcessorPrinter pp){
        final TypeElement te = (TypeElement) ((DeclaredType) mirror).asElement();
        Name paramType = te.getQualifiedName();
        pp.note("test() >>> paramType = " + paramType.toString());

        final List<? extends Element> list = te.getEnclosedElements();
        for(Element e: list){
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
    static javax.lang.model.element.Modifier[] getFieldModifier(FieldData fieldData){
        final int flags = fieldData.getFlags();
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PRIVATE);
        if( (flags & FLAG_TRANSIENT) == FLAG_TRANSIENT){
            modifiers.add(Modifier.TRANSIENT);
        }
        //TODO
        if( (flags & FLAG_VOLATILE) == FLAG_VOLATILE){
            modifiers.add(Modifier.VOLATILE);
        }
        return modifiers.toArray(new Modifier[modifiers.size()]);
    }
    /**
     * get property name. eg: name to Name(get, set)
     * @param prop the target name
     * @return the changed name for get/set method
     */
    public static String getPropNameForMethod(String prop) {
        if(prop == null || "".equals(prop.trim())){
            throw new IllegalStateException("property name can't be empty");
        }
        return prop.substring(0,1).toUpperCase().concat(prop.substring(1));
    }

    /**
     * get parameter name . eg: int to int1 Object to Object1
     * @param name the raw name of param
     * @return the changed parameter name.
     */
    public static String getParamName(String name) {
        return name.substring(0,1).toLowerCase()
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
}
