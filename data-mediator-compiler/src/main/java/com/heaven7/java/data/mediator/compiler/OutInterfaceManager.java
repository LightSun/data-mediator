package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.data.mediator.compiler.fillers.*;
import com.heaven7.java.data.mediator.compiler.replacer.CopyReplacer;
import com.heaven7.java.data.mediator.compiler.replacer.TargetClassInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_COPYA;
import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_SELECTABLE;
import static com.heaven7.java.data.mediator.compiler.Util.hasFlag;
import static com.heaven7.java.data.mediator.compiler.Util.overriding;

/**
 * used for override other interface. impl by module define.
 * Created by heaven7 on 2017/9/28 0028.
 */
public class OutInterfaceManager {

    private static final String TAG = "OutInterfaceManager";
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
        final TypeInterfaceFiller sSelectable = new TypeSelectableFiller();

        sFillerMap.put(sCopyFiller.getInterfaceName(), sCopyFiller);
        sFillerMap.put(sResetFiller.getInterfaceName(), sResetFiller);
        sFillerMap.put(sShareFiller.getInterfaceName(), sShareFiller);
        sFillerMap.put(sSnapFiller.getInterfaceName(), sSnapFiller);
        sFillerMap.put(sParcelable.getInterfaceName(), sParcelable);
        sFillerMap.put(sSerializable.getInterfaceName(), sSerializable);
        sFillerMap.put(sSelectable.getInterfaceName(), sSelectable);

        sReplacerMap = new HashMap<>();
        sReplacerMap.put(NAME_COPYA, new CopyReplacer());
    }

    /**
     * get field builder.
     *
     * @param pkgName   the package name
     * @param classname the class name
     * @param tc        the type element wrapper.
     * @param map       the map.
     * @param superFlagsForParent the flags for super interface/class
     * @return the field builders.
     */
    public static MethodSpec.Builder[] getImplClassConstructBuilders(String pkgName, String classname,
                                                                     FieldData.TypeCompat tc, Map<String, List<FieldData>> map,
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

    /**
     * get field builder.
     *
     * @param pkgName   the package name
     * @param classname the class name
     * @param tc        the type element wrapper.
     * @param map       the map.
     * @param superFlagsForParent super flags for parent
     * @return the field builders.
     */
    public static FieldSpec.Builder[] getImplClassFieldBuilders(String pkgName, String classname,
                                                                FieldData.TypeCompat tc, Map<String,
                                                        List<FieldData>> map, int superFlagsForParent) {
        final TypeElement te = tc.getElementAsType();
        String interfaceIname = te.getQualifiedName().toString();
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceIname);
        if (filler != null) {
            final List<FieldData> datas = map.get(filler.getInterfaceName());
            final String interName = te.getSimpleName().toString();
            return filler.createFieldBuilder(pkgName, interName, classname, datas, superFlagsForParent);
        }
        return null;
    }

    //classname  : current class , here mirror is interface .can't be primitive.
    //tc indicate interface
    //superFlagsForParent  super interface flags for parent
    public static MethodSpec.Builder[] getImplClassMethodBuilders(TargetClassInfo info,
                                                                  TypeName returnReplace, FieldData.TypeCompat tc,
                                                                  ProcessorPrinter pp, Map<String, List<FieldData>> map,
                                                                  boolean usedSuperClass, int superFlagsForParent) {
        final TypeElement te = tc.getElementAsType();
        final String interfaceName = te.getQualifiedName().toString();
        pp.note(TAG, "getImplClassMethodBuilders() >>> interface name = " + interfaceName);
        final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);

        //if super has selectable. and current 'interfaceName' is it. just ignore.
        if(NAME_SELECTABLE.equals(interfaceName) && hasFlag(superFlagsForParent, FieldData.FLAG_SELECTABLE)){
            return null;
        }
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

    //te is the target element to generate  Proxy.
    public static List<MethodSpec.Builder> getProxyClassMethodBuilders(TargetClassInfo info, TypeElement te,
                                                                       Types types, ProcessorPrinter pp){

        //the method builders for super interface.
        final List<MethodSpec.Builder> methodBuilders =  new ArrayList<>();
        final ClassName cn_inter = ClassName.get(info.getPackageName(), info.getDirectParentInterfaceName());

        List<? extends TypeMirror> interfaces = getAttentionInterfaces(te, types, pp);
        pp.note(TAG, "getProxyClassMethodBuilders", "te : " + te + " ,superinterfaces = " + interfaces);
        for(TypeMirror tm : interfaces){
            FieldData.TypeCompat tc = new FieldData.TypeCompat(types, tm);
            TypeElement type = tc.getElementAsType();
            String interfaceName = type.getQualifiedName().toString();
            final TypeInterfaceFiller filler = sFillerMap.get(interfaceName);
            //for proxy .method build is in ProxyGenerator.
            if(filler instanceof TypeSelectableFiller){
                continue;
            }

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
     * @see #getAttentionInterfaces(TypeElement, Types, List, ProcessorPrinter)
     */
    public static List<? extends TypeMirror> getAttentionInterfaces(TypeElement te,
                                                                    Types types, ProcessorPrinter pp){
        return getAttentionInterfaces(te, types , new ArrayList<String>(), pp);
    }

    /**
     * get the super interface flags for parent
     * @param te the current type element
     * @param pp the log printer
     * @return the flags.
     */
    public static int getSuperInterfaceFlagForParent(TypeElement te,
                                                     Elements mElements, Types types, ProcessorPrinter pp){
        final String tag = te.getSimpleName().toString();
        List<? extends TypeMirror> interfaces = te.getInterfaces();
        for(TypeMirror tm: interfaces) {
            // pp.note(TAG, "getSuperInteraceFlagForParent_" + tag, "TypeMirror : " + tm);
            FieldData.TypeCompat tc = new FieldData.TypeCompat(types, tm);
            tc.replaceIfNeed(mElements, pp, null);
            pp.note(TAG, "getSuperInteraceFlagForParent_" + tag, "TypeMirror : " + tm
                    + " , hasAnnotationFields = " + tc.hasAnnotationFields());
            if(tc.hasAnnotationFields()){
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
            FieldData.TypeCompat tc = new FieldData.TypeCompat(types, tm);
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

    //==============================================================================================================

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
        for (TypeInterfaceFiller filler : sFillerMap.values()) {
            filler.reset();
        }
    }

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
            FieldData.TypeCompat tc = new FieldData.TypeCompat(types, tm);
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

    public static BaseTypeReplacer getTypeReplacer(String interfaceName) {
        return sReplacerMap.get(interfaceName);
    }
}
