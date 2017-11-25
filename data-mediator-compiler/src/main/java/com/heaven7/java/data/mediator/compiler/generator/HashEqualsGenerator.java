package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.heaven7.java.data.mediator.compiler.util.Util;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.*;

/**
 * Created by heaven7 on 2017/9/28 0028.
 */
public class HashEqualsGenerator {

    private static boolean sTemp_defined = false;

    public static void generateForImpl(TypeSpec.Builder typeBuilder, Collection<FieldData> fields,
                                       Collection<FieldData> superFields,
                                       TargetClassInfo info, boolean hasSuperClass){
        final List<FieldData> hash_current = filterHash(fields);
        final List<FieldData> hash_super = filterHash(superFields);
        final List<FieldData> equals_current = filterEquals(fields);
        final List<FieldData> equals_super = filterEquals(superFields);
        generateHashCode(typeBuilder, hash_current, hash_super, hasSuperClass);
        generateEquals(typeBuilder, equals_current, equals_super, info, hasSuperClass);
    }

    private static void generateHashCode(TypeSpec.Builder typeBuilder, Collection<FieldData> fields,
                                         Collection<FieldData> superFields, boolean hasSuperClass){
        if(fields.isEmpty()){
            return;
        }
        MethodSpec.Builder hashBuilder = MethodSpec.methodBuilder("hashCode")
                .returns(TypeName.INT)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);

        if(hasSuperClass && !superFields.isEmpty()){
            hashBuilder.addStatement("int result = super.hashCode()");
        }else{
            hashBuilder.addStatement("int result = 0");
        }
        for(FieldData fd : fields){
            addStatementForHashcode(hashBuilder, fd);
        }
        hashBuilder.addStatement("return result");
        sTemp_defined = false;

        typeBuilder.addMethod(hashBuilder.build());
    }

    private static void generateEquals(TypeSpec.Builder typeBuilder, Collection<FieldData> fields,
                                       Collection<FieldData> superFields, TargetClassInfo info, boolean hasSuperClass){
        if(fields.isEmpty()){
            return;
        }
        MethodSpec.Builder equalsBuilder = MethodSpec.methodBuilder("equals")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.BOOLEAN)
                .addAnnotation(Override.class)
                .addParameter(TypeName.OBJECT, "o");

        ClassName cn_cur = ClassName.get(info.getPackageName(), info.getCurrentClassname());
        /*
         if (this == o) return true;
         if (!(o instanceof TestInterface2Module_Impl)) return false;
         if (!super.equals(o)) return false;
         */
        equalsBuilder.beginControlFlow("if (this == o)")
                    .addStatement("return true")
                    .endControlFlow()
                .beginControlFlow("if (!(o instanceof $T))", cn_cur)
                .addStatement("return false")
                .endControlFlow();
        if(hasSuperClass && !superFields.isEmpty()){
            equalsBuilder.beginControlFlow("if (!super.equals(o))")
                    .addStatement("return false")
                    .endControlFlow();
        }

        equalsBuilder.addStatement(" $T that = ($T) o", cn_cur, cn_cur);
        for(FieldData fd : fields){
            addStatementForEquals(equalsBuilder, fd);
        }
        equalsBuilder.addStatement("return true");
        typeBuilder.addMethod(equalsBuilder.build());
    }

    private static List<FieldData> filterHash(Collection<FieldData> fds){
        List<FieldData> set = new ArrayList<>();
        for (FieldData fd : fds){
            if(Util.hasFlag(fd.getFlags(), FieldData.FLAG_HASH)) {
                set.add(fd);
            }
        }
        return set;
    }
    private static List<FieldData> filterEquals(Collection<FieldData> fds){
        List<FieldData> set = new ArrayList<>();
        for (FieldData fd : fds){
            if(Util.hasFlag(fd.getFlags(), FieldData.FLAG_EQUALS)) {
                set.add(fd);
            }
        }
        return set;
    }

    private static void addStatementForEquals(MethodSpec.Builder equalsBuilder, FieldData fd) {
        final String getName = fd.getGetMethodPrefix() + Util.getPropNameForMethod(fd);

        final FieldData.TypeCompat typeCompat = fd.getTypeCompat();
        final TypeMirror tm = typeCompat.getTypeMirror();
        switch (fd.getComplexType()) {
            case FieldData.COMPLEXT_SPARSE_ARRAY:
                equalsBuilder.beginControlFlow("if ($N() != null ? !$N().equals(that.$N()) : that.$N() != null)",
                        getName, getName , getName, getName)
                        .addStatement("return false")
                        .endControlFlow();
                break;

            case FieldData.COMPLEXT_ARRAY: {
                //  if (!Arrays.equals(getTest_int_array(), that.getTest_int_array())) return false;
                equalsBuilder.beginControlFlow("  if (!$T.equals($N(), that.$N()))", Arrays.class, getName, getName)
                        .addStatement("return false")
                        .endControlFlow();
            }
            break;

            case FieldData.COMPLEXT_LIST:{
                /*
  if (getTest_double_list() != null ? !getTest_double_list().equals(that.getTest_double_list()) : that.getTest_double_list() != null)
       return false;
                 */
                equalsBuilder.beginControlFlow("if ($N() != null ? !$N().equals(that.$N()) : that.$N() != null)"
                                 , getName, getName, getName, getName)
                        .addStatement("return false")
                        .endControlFlow();
            }
            break;

            default: {
                switch (tm.toString().trim()){
                    case NAME_short:
                    case NAME_byte:
                    case NAME_char:
                    case NAME_int:
                    case NAME_long:
                    case NAME_boolean:
                        //if (getTest_int() != that.getTest_int()) return false;
                        equalsBuilder.beginControlFlow("if ($N() != that.$N())", getName, getName)
                                .addStatement("return false")
                                .endControlFlow();
                        break;


                    case NAME_float:
                        //  if (Float.compare(that.getTest_float(), getTest_float()) != 0) return false;
                        equalsBuilder.beginControlFlow("if ($T.compare(that.$N(), $N()) != 0)",
                                          Float.class,getName, getName)
                                .addStatement("return false")
                                .endControlFlow();
                        break;

                    case NAME_double:
                        //  if (Double.compare(that.getTest_double(), getTest_double()) != 0) return false;
                        equalsBuilder.beginControlFlow("if ($T.compare(that.$N(), $N()) != 0)",
                                        Double.class, getName, getName)
                                .addStatement("return false")
                                .endControlFlow();
                        break;

                    case NAME_BOOLEAN:
                    case NAME_DOUBLE:
                    case NAME_FLOAT:
                    case NAME_BYTE:
                    case NAME_SHORT:
                    case NAME_LONG:
                    case NAME_INTEGER:
                    case NAME_CHARACTER:
                    case NAME_STRING:
                    default:
                        /*
                        if (getTest_Short() != null ? !getTest_Short().equals(that.getTest_Short()) : that.getTest_Short() != null)
                          return false;
                         */
                        equalsBuilder.beginControlFlow("if ($N() != null ? !$N().equals(that.$N()) : that.$N() != null)",
                                              getName, getName , getName, getName)
                                .addStatement("return false")
                                .endControlFlow();
                        break;
                }
            }
            break;
        }
    }

    private static void addStatementForHashcode(MethodSpec.Builder hashBuilder, FieldData fieldData) {
        final String getName = fieldData.getGetMethodPrefix() + Util.getPropNameForMethod(fieldData);

        final FieldData.TypeCompat typeCompat = fieldData.getTypeCompat();
        final TypeMirror tm = typeCompat.getTypeMirror();

        switch (fieldData.getComplexType()) {

            case FieldData.COMPLEXT_SPARSE_ARRAY:
                hashBuilder.addStatement("result = 31 * result + ($N() != null ? $N().hashCode() : 0)", getName, getName);
                break;

            case FieldData.COMPLEXT_ARRAY: {
                // result = 31 * result + Arrays.hashCode(getTest_ResultData_array());
                hashBuilder.addStatement("result = 31 * result + $T.hashCode($N())", Arrays.class,getName);
            }
            break;

            case FieldData.COMPLEXT_LIST:{
                // result = 31 * result + (getTest_ResultData_list() != null ? getTest_ResultData_list().hashCode() : 0);
                hashBuilder.addStatement("result = 31 * result + ($N() != null ? $N().hashCode() : 0)",
                        getName, getName);
            }
            break;

            default: {
                switch (tm.toString().trim()){
                    case NAME_short:
                    case NAME_byte:
                    case NAME_char:
                        // result = 31 * result + (int) getTest_byte();
                        hashBuilder.addStatement("result = 31 * result + (int) $N()", getName);
                        break;

                    case NAME_int:
                        hashBuilder.addStatement("result = 31 * result + $N()", getName);
                        break;

                    case NAME_long:
                        //result = 31 * result + (int) (getTest_long() ^ (getTest_long() >>> 32));
                        hashBuilder.addStatement("result = 31 * result + (int) ($N() ^ ($N() >>> 32))",
                                getName, getName);
                        break;


                    case NAME_float:
                       /*
                       result = 31 * result + (getTest_float() != +0.0f ? Float.floatToIntBits(getTest_float()) : 0);
                        */
                       hashBuilder.addStatement("result = 31 * result + ($N() != +0.0f ? $T.floatToIntBits($N()) : 0)",
                               getName, Float.class ,getName);
                        break;

                    case NAME_double:
                        //  temp = Double.doubleToLongBits(getTest_double());
                        if(sTemp_defined) {
                            hashBuilder.addStatement("temp = $T.doubleToLongBits($N())", Double.class, getName);
                        }else{
                            hashBuilder.addStatement("long temp = $T.doubleToLongBits($N())", Double.class, getName);
                            sTemp_defined = true;
                        }
                        // result = 31 * result + (int) (temp ^ (temp >>> 32));
                        hashBuilder.addStatement("result = 31 * result + (int) (temp ^ (temp >>> 32))");
                        break;
                    case NAME_boolean:
                        //   result = 31 * result + (isTest_boolean() ? 1 : 0);
                        hashBuilder.addStatement("result = 31 * result + ($N() ? 1 : 0)", getName);
                        break;

                    case NAME_BOOLEAN:
                    case NAME_DOUBLE:
                    case NAME_FLOAT:
                    case NAME_BYTE:
                    case NAME_SHORT:
                    case NAME_LONG:
                    case NAME_INTEGER:
                    case NAME_CHARACTER:
                    case NAME_STRING:
                    default:
               //  result = 31 * result + (getTest_Integer() != null ? getTest_Integer().hashCode() : 0);
                        hashBuilder.addStatement("result = 31 * result + ($N() != null ? $N().hashCode() : 0)", getName, getName);
                        break;
                }
            }
            break;
        }
    }
}
