package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.*;
import com.heaven7.java.data.mediator.compiler.Util;
import com.heaven7.java.data.mediator.compiler.insert.InsertManager;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;
import static com.heaven7.java.data.mediator.compiler.Util.getTypeName;

/**
 * the proxy generator
 * Created by heaven7 on 2017/9/14 0014.
 */
public class ProxyGenerator {

    private static final String TAG = ProxyGenerator.class.getSimpleName();

    //allFields include supers'
    public static boolean generateProxy(TargetClassInfo info, Set<FieldData> allFields, boolean normalJavaBean ,
                                        List<MethodSpec.Builder> superMethods,
                                        Filer filer, ProcessorPrinter pp) {
        //String interfaceName  = "IStudent";
       // String pkg  = "com.heaven7.java.data.mediator.compiler.test";
        final String interfaceName = info.getDirectParentInterfaceName();
        final String pkg = info.getPackageName();

        ClassName cn_inter = ClassName.get(pkg, interfaceName);
        ParameterizedTypeName superTypeName = ParameterizedTypeName.get(
                ClassName.get(PKG_PROP, SIMPLE_NAME_BASE_MEDIATOR), cn_inter);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(interfaceName + PROXY_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superTypeName)
                .addSuperinterface(cn_inter);

        //constructor
        typeBuilder.addMethod(MethodSpec.constructorBuilder().addParameter(cn_inter, "base")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(base)")
                .build());
        //build field and methods.
        buildFieldsAndMethods(allFields, cn_inter, typeBuilder, normalJavaBean);

        //override super methods from super interface .like IResetable.
        if(superMethods != null) {
            for (MethodSpec.Builder builder : superMethods) {
                typeBuilder.addMethod(builder.build());
            }
        }
        //addToString
        /**
         @Override
        public String toString() {
        return _getTarget().toString();
        }
         */
        MethodSpec.Builder toString = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(String.class)
                .addStatement("return _getTarget().toString()");
        typeBuilder.addMethod(toString.build());

        //add insert override methods(insert interfaces)
        InsertManager.overrideMethodsForProxy(typeBuilder, null);

        TypeSpec typeSpec = typeBuilder.build();
        try {
            JavaFile.builder(pkg, typeSpec)
                    .build().writeTo(filer);
        } catch (IOException e) {
            pp.note(TAG, "generateProxy", Util.toString(e));
            return false;
        }
        return true;
    }

    private static void buildFieldsAndMethods(Set<FieldData> set, ClassName cn_inter,
                                              TypeSpec.Builder typeBuilder, boolean normalJavaBean) {

        ClassName cn_prop_interceptor = ClassName.get(PKG_PROP, SIMPLE_NAME_PROP_INTERCEPTOR);
        ClassName cn_throwables = ClassName.get(PKG_JAVA_BASE_UTIL, SIMPLE_NAME_THROWABLES);


        //all set/add/remove return this type.
        final TypeName returnType = normalJavaBean ? TypeName.VOID : cn_inter;
        //for list prop
        final ClassName cn_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_LIST_PROP_EDITOR);
        //sparseArray
        final ClassName cn_sa_editor = ClassName.get(PKG_PROP, SIMPLE_NAME_SPARSE_ARRAY_EDITOR);
       // DataMediatorDelegate
        final ClassName cn_dm_delegate = ClassName.get(PKG_DM_INTERNAL, SIMPLE_NAME_DM_DELEGATE);
        /*
    @Override
    public void applyProperties(PropertyInterceptor interceptor) {
        Throwables.checkNull(interceptor);
        startBatchApply(interceptor)
                .addProperty(PROP_AGE, getAge())
                .addProperty(PROP_NAME, getName())
                .addProperty(PROP_ID, getId())
                .addProperty(PROP_TAGS, getTags())
                .apply();
    }
         */
        //override apply from BaseMediator.
        final MethodSpec.Builder applyBuilder = MethodSpec.methodBuilder("applyProperties")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(cn_prop_interceptor, "interceptor")
                .addStatement("$T.checkNull(interceptor)", cn_throwables)
                .addCode("startBatchApply(interceptor)\n");

        //fields and methods.
        for(FieldData field : set){
            final TypeInfo info = new TypeInfo();
            getTypeName(field, info);

            //static field name (PROP_xxx) which is generated in Interface(like xxxModule).
            final String fieldName = field.getFieldConstantName();

            //get
            final String nameForMethod = Util.getPropNameForMethod(field);
            final String getMethodName = field.getGetMethodPrefix() + nameForMethod;
            typeBuilder.addMethod(MethodSpec.methodBuilder(getMethodName)
                    .returns(info.getTypeName())
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return _getTarget().$N()", getMethodName)
                    .build());

            //set
            final String setMethodName = SET_PREFIX + nameForMethod;
            final String paramName = info.getParamName();

            MethodSpec.Builder setBuilder = MethodSpec.methodBuilder(setMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(field == FD_SELECTABLE ? TypeName.VOID : returnType)
                    .addParameter(info.getTypeName(), paramName)
                    .addStatement("$T target = _getTarget()", cn_inter)
                    .addStatement("$T oldValue = target.$N()", info.getTypeName(), getMethodName);
            if(!normalJavaBean && field != FD_SELECTABLE){
                setBuilder.beginControlFlow("if(_getEqualsComparator().isEquals(oldValue, $N))", paramName)
                        .addCode("return this;\n")
                        .endControlFlow()
                        .addStatement("target.$N($N)", setMethodName, paramName)
                        .addStatement("dispatchCallbacks($N, oldValue, $N)", fieldName, paramName)
                        .addCode("return this;\n");
            }else{
                setBuilder.beginControlFlow("if(_getEqualsComparator().isEquals(oldValue, $N))", paramName)
                        .addCode("return ;\n")
                        .endControlFlow()
                        .addStatement("target.$N($N)", setMethodName, paramName)
                        .addStatement("dispatchCallbacks($N, oldValue, $N)", fieldName, paramName);
            }
            typeBuilder.addMethod(setBuilder.build());
            //add apply statement.
            applyBuilder.addCode("  .addProperty($N, $N())\n", fieldName, getMethodName);

            //like :  ListPropertyEditor<IStudent,String> newTagsEditor();
            if(field.isList()){
                  /*
             IStudent target = _getTarget();
             List<String> tags = target.getTags();
             if(tags == null){
             tags = new ArrayList<>();
             target.setTags(tags);
             }
             return new ListPropertyEditor<IStudent, String>(target, tags, PROP_TAGS, this);
             */
                final MethodSpec.Builder listEditor = PropertyEditorBuildUtils.buildListEditorWithoutModifier(
                              field, nameForMethod, info, cn_inter)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$T target = _getTarget()",cn_inter)
                        .addStatement("List<$T> values = target.$N()", info.getSimpleTypeNameBoxed(), getMethodName)
                        .beginControlFlow("if(values == null)")
                        .addStatement("values = new $T<>()", ArrayList.class)
                        .addStatement("target.$N(values)", setMethodName)
                        .endControlFlow()
                        .addStatement("return new $T<$T,$T>(target, values, $N, this)",
                                cn_editor, cn_inter, info.getSimpleTypeNameBoxed(), fieldName);
                typeBuilder.addMethod(listEditor.build());
            }else if(field.getComplexType() == FieldData.COMPLEXT_SPARSE_ARRAY){
                /*
                @Override
                public SparseArrayPropertyEditor<IStudent, String> beginCityDataEditor() {
                    IStudent target = _getTarget();
                    SparseArray<String> cityData = target.getCityData();
                    if(cityData == null){
                    cityData = new SparseArray<>();
                    target.setCityData(cityData);
                    }
                    return new SparseArrayPropertyEditor<IStudent, String>(this,
                    DataMediatorDelegate.getDefault().getSparseArrayDelegate(cityData),
                    PROP_cityData, this);
                }
                 */
                ClassName cn_sa = ClassName.get(PKG_JAVA_BASE_UTIL, SIMPLE_NAME_SPARSE_ARRAY);

                final MethodSpec.Builder sparseEditor = PropertyEditorBuildUtils.buildSparseArrayEditorWithoutModifier(
                        field, nameForMethod, info, cn_inter)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$T target = _getTarget()",cn_inter)
                        .addStatement("$T<$T> values = target.$N()", cn_sa, info.getSimpleTypeNameBoxed(), getMethodName)
                            .beginControlFlow("if(values == null)")
                            .addStatement("values = new $T<>()", cn_sa)
                            .addStatement("target.$N(values)", setMethodName)
                            .endControlFlow()
                        .addStatement("return new $T<$T,$T>(target, " +
                                        "$T.getDefault().getSparseArrayDelegate(values), $N, this)",
                                cn_sa_editor, cn_inter, info.getSimpleTypeNameBoxed(),
                                cn_dm_delegate, fieldName);
                typeBuilder.addMethod(sparseEditor.build());
            }
        }
        applyBuilder.addCode(".apply();\n");
        typeBuilder.addMethod(applyBuilder.build());
    }
}
