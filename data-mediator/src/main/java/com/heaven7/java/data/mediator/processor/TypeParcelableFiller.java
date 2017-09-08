package com.heaven7.java.data.mediator.processor;

import com.squareup.javapoet.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Created by heaven7 on 2017/9/8 0008.
 */
public class TypeParcelableFiller extends TypeInterfaceFiller {

    private final ClassName mParcelable = ClassName.get("android.os",
            "Parcelable");
    private final ClassName mCreator = ClassName.get("android.os",
            "Parcelable", "Creator");
    private final ClassName mParcel = ClassName.get("android.os",
            "Parcel");

    @Override
    public String getInterfaceName() {
        return Util.NAME_PARCELABLE;
    }

    @Override
    public int getInterfaceFlag() {
        return FieldData.FLAG_PARCEABLE;
    }

    @Override
    public MethodSpec.Builder[] createConstructBuilder(String pkgName, String interName, String classname,
                                                       List<FieldData> datas, boolean hasSuperClass) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(mParcel, "in");

        if (hasSuperClass) {
            builder.addStatement("super(in)");
        }
        for (FieldData fieldData : datas) {
            addParcelStatement(builder, fieldData, "in");
        }
        return super.createConstructBuilder(pkgName, interName, classname, datas, hasSuperClass);
    }

    @Override
    public void buildMethodStatement(String curPkg, String parentInterfaceName, String curClassName,
                                     ExecutableElement ee, MethodSpec.Builder builder, List<FieldData> list) {
        note(ee);
    }

    @Override
    public FieldSpec.Builder[] createFieldBuilder(String pkgName, String interName, String classname, List<FieldData> datas) {
        ClassName obj = ClassName.get(pkgName, classname);
        TypeName creatorOfobj = ParameterizedTypeName.get(mCreator, obj);

        ParameterSpec sizeParam = ParameterSpec.builder(TypeName.INT, "size")
                .build();

        TypeSpec creatorInitSpec = TypeSpec.anonymousClassBuilder(
                ""
        )
                .superclass(creatorOfobj)
                .addMethod(MethodSpec.methodBuilder("createFromParcel")
                        .addAnnotation(Override.class)
                        .returns(obj)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(mParcel, "in")
                        .addStatement("return new $T(in)", obj)
                        .build())
                .addMethod(MethodSpec.methodBuilder("newArray")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .returns(ArrayTypeName.of(obj))
                        .addParameter(sizeParam)
                        .addStatement("return new $T[size]", obj)
                        .build())
                .build();
        FieldSpec.Builder[] builders = new FieldSpec.Builder[1];
        builders[0] = FieldSpec.builder(creatorOfobj, "CREATOR")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", creatorInitSpec);

        return builders;
    }

    private static void addParcelStatement(MethodSpec.Builder builder, FieldData fieldData, String in) {

        final TypeMirror tm = fieldData.getTypeCompat().getTypeMirror();
        final String prop = fieldData.getPropertyName();
        final TypeName typeName = TypeName.get(tm);

        switch (fieldData.getComplexType()) {
            case FieldData.COMPLEXT_ARRAY: {
                switch (tm.getKind()) {
                    case INT:
                        builder.addStatement("this.$N = $N.createIntArray()", prop, in);
                        break;

                    case LONG:
                        builder.addStatement("this.$N = $N.createLongArray()", prop, in);
                        break;

                    case SHORT:
                        builder.addStatement("int[] arr = $N.createIntArray()", in)
                                .addStatement("final int size = arr.length")
                                .addStatement("this.$N = new short[size]" , prop)
                                .beginControlFlow("for(int i= 0 ; i < size ; i++)")
                                .addStatement("this.$N[i] = (short)arr[i]", prop)
                                .endControlFlow()
                                .build();
                        break;

                    case BYTE:
                        builder.addStatement("this.$N = $N.createByteArray()", prop, in);
                        break;

                    case BOOLEAN:
                        builder.addStatement("this.$N = $N.createBooleanArray()", prop, in);
                        break;

                    case FLOAT:
                        builder.addStatement("this.$N = $N.createFloatArray()", prop, in);
                        break;

                    case DOUBLE:
                        builder.addStatement("this.$N = $N.createDoubleArray()", prop, in);
                        break;

                    case CHAR:
                        builder.addStatement("this.$N = $N.createCharArray()", prop, in);
                        break;

                    default: {
                        //first judge is box type or not.
                        //like  this.testArrayInteger = (Integer[]) in.readArray(Integer[].class.getClassLoader());

                        switch (tm.toString().trim()){
                            case NAME_INTEGER:
                            case NAME_LONG:
                            case NAME_SHORT:
                            case NAME_BYTE:
                            case NAME_BOOLEAN:
                            case NAME_FLOAT:
                            case NAME_DOUBLE:
                            case NAME_CHARACTER:
                                builder.addStatement("this.$N = ($T[])$N.readArray($T[].class.getClassLoader())",
                                        prop, typeName, in, typeName);
                                break;

                            case NAME_STRING:
                                builder.addStatement("this.$N = $N.createStringArrayList()", prop, in);
                                break;

                            default:
                                //  this.testArrayResultData = in.createTypedArray(ResultData.CREATOR);
                                builder.addStatement("this.$N = $N.createTypedArray($T.CREATOR)",
                                        prop, in, typeName);
                                break;
                        }
                    }
                        break;
                }
            }
            break;

            case FieldData.COMPLEXT_LIST:{
//  this.datas = in.createTypedArrayList(ResultData.CREATOR);
                switch (tm.toString().trim()) {
                    case NAME_int:
                    case NAME_INTEGER:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Integer.class);
                        break;

                    case NAME_long:
                    case NAME_LONG:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Long.class);
                        break;

                    case NAME_short:
                    case NAME_SHORT:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Short.class);
                        break;

                    case NAME_byte:
                    case NAME_BYTE:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Byte.class);
                        break;

                    case NAME_boolean:
                    case NAME_BOOLEAN:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Boolean.class);
                        break;

                    case NAME_float:
                    case NAME_FLOAT:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Float.class);
                        break;

                    case NAME_double:
                    case NAME_DOUBLE:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Double.class);
                        break;

                    case NAME_char:
                    case NAME_CHARACTER:
                        builder.addStatement("$N.readList(this.$N, $T.class.getClassLoader())",
                                in, prop, Character.class);
                        break;

                        //in.createStringArrayList()
                    case NAME_STRING:
                        builder.addStatement("$N.createStringArrayList()", in);
                        break;

                    default:
                         //  this.intents = in.createTypedArrayList(Intent.CREATOR);
                        builder.addStatement("this.$N = $N.createTypedArrayList($T.CREATOR)", prop, in, typeName);
                        break;

                }
            }
                break;

            default:
                //TODO
                break;
        }
    }

    private static final String NAME_int      = "int";
    private static final String NAME_long     = "long";
    private static final String NAME_short    = "short";
    private static final String NAME_byte     = "byte";
    private static final String NAME_boolean  = "boolean";
    private static final String NAME_float    = "float";
    private static final String NAME_double   = "double";
    private static final String NAME_char     = "char";

    private static final String NAME_INTEGER = "java.lang.Integer";
    private static final String NAME_LONG = "java.lang.Long";
    private static final String NAME_SHORT = "java.lang.Short";
    private static final String NAME_BYTE = "java.lang.Byte";
    private static final String NAME_BOOLEAN = "java.lang.Boolean";
    private static final String NAME_FLOAT = "java.lang.Float";
    private static final String NAME_DOUBLE = "java.lang.Double";
    private static final String NAME_CHARACTER =  "java.lang.Character";

    private static final String NAME_STRING =  "java.lang.String";
}
