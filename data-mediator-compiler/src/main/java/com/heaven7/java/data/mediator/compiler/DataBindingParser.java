package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.data.mediator.bind.*;
import com.heaven7.java.data.mediator.internal.BindMethod;

import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.PKG_DATA_BINDING_ANNO;

/**
 * Created by heaven7 on 2017/11/5.
 * @since 1.4.0
 */
public class DataBindingParser {

    private static final String TAG = "DataBindingParser";
    private final ProcessorContext mContext;

    public DataBindingParser(ProcessorContext mContext) {
        this.mContext = mContext;
    }

    public boolean parseBinderClass(TypeElement te, DataBindingInfo info) {
        for(AnnotationMirror am :te.getAnnotationMirrors()){
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            String annoFullname = e1.getQualifiedName().toString();
            if(annoFullname.equals(BinderClass.class.getName())){
                info.setBinderClass(getSimpleTypeMirror(am));
            }else if(annoFullname.equals(BinderFactoryClass.class.getName())){
                info.setBinderFactoryClass(getSimpleTypeMirror(am));
            }
        }
        return true;
    }
    public boolean parseBinderFactoryClass(TypeElement te, DataBindingInfo info) {
        return parseBinderClass(te, info);
    }

    /** parse array properties */
    public boolean parseBindsProperty(Element element, DataBindingInfo info) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        final VariableElement ve = (VariableElement) element;
        final String varName = ve.getSimpleName().toString();

        BindsView bindsView = element.getAnnotation(BindsView.class);
        pp.note(TAG, "parseBindsProperty", "bindsView = " + bindsView);
        if(bindsView != null){
            if(!parseBindsInternal(info, varName, bindsView.index(), bindsView.value(), bindsView.methods())){
                return false;
            }
        }
        BindsTextView bindsTextViewRes = element.getAnnotation(BindsTextView.class);
        if(bindsTextViewRes != null){
            if(!parseBindsInternal(info, varName, bindsTextViewRes.index(), bindsTextViewRes.value(), bindsTextViewRes.methods())){
                return false;
            }
        }
        return true;
    }

    public boolean parseBindProperty(Element element, DataBindingInfo info) {
        final VariableElement ve = (VariableElement) element;
        final String varName = ve.getSimpleName().toString();

        for(AnnotationMirror am : element.getAnnotationMirrors()){
            TypeElement e1 = (TypeElement) am.getAnnotationType().asElement();
            String annoFullname = e1.getQualifiedName().toString();
            if(!annoFullname.startsWith(PKG_DATA_BINDING_ANNO)){
                continue;
            }
            //get @BindMethod
            DataBindingInfo.BindMethodInfo bmi = getBindMethodInfo(e1);
            DataBindingInfo.BindInfo bindInfo = new DataBindingInfo.BindInfo(varName, bmi.name, bmi.types);
            if(annoFullname.equals(BindCheckable.class.getName())){
                //get @BindMethod
                BindCheckable checkable = element.getAnnotation(BindCheckable.class);
                bindInfo.setIndex(checkable.index());
                bindInfo.setPropName(checkable.value());
            }else if(annoFullname.equals(BindVisibility.class.getName())){

                BindVisibility annotation = element.getAnnotation(BindVisibility.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
                bindInfo.setExtras(new Object[]{annotation.forceAsBoolean()});
            }else if(annoFullname.equals(BindEnable.class.getName())){

                BindEnable annotation = element.getAnnotation(BindEnable.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindBackground.class.getName())){

                BindBackground annotation = element.getAnnotation(BindBackground.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindBackgroundRes.class.getName())){

                BindBackgroundRes annotation = element.getAnnotation(BindBackgroundRes.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindBackgroundColor.class.getName())){
                BindBackgroundColor annotation = element.getAnnotation(BindBackgroundColor.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }
           // text view
            else if(annoFullname.equals(BindText.class.getName())){
                BindText annotation = element.getAnnotation(BindText.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            } else if(annoFullname.equals(BindTextRes.class.getName())){

                BindTextRes annotation = element.getAnnotation(BindTextRes.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            } else if(annoFullname.equals(BindTextSize.class.getName())){

                BindTextSize annotation = element.getAnnotation(BindTextSize.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindTextSizeRes.class.getName())){

                BindTextSizeRes annotation = element.getAnnotation(BindTextSizeRes.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindTextSizePx.class.getName())){

                BindTextSizePx annotation = element.getAnnotation(BindTextSizePx.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindTextColor.class.getName())){

                BindTextColor annotation = element.getAnnotation(BindTextColor.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindTextColorRes.class.getName())){

                BindTextColorRes annotation = element.getAnnotation(BindTextColorRes.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }
            //image view
            else if(annoFullname.equals(BindImageBitmap.class.getName())){

                BindImageBitmap annotation = element.getAnnotation(BindImageBitmap.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            } else if(annoFullname.equals(BindImageDrawable.class.getName())){

                BindImageDrawable annotation = element.getAnnotation(BindImageDrawable.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            } else if(annoFullname.equals(BindImageRes.class.getName())){

                BindImageRes annotation = element.getAnnotation(BindImageRes.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindImageUri.class.getName())){

                BindImageUri annotation = element.getAnnotation(BindImageUri.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }else if(annoFullname.equals(BindImageUrl.class.getName())){
                BindImageUrl annotation = element.getAnnotation(BindImageUrl.class);
                bindInfo.setIndex(annotation.index());
                bindInfo.setPropName(annotation.value());
            }

            //add
            if(bindInfo.isValid()) {
                info.addBindInfo(bindInfo);
            }
        }
        return true;
    }

    private DataBindingInfo.BindMethodInfo getBindMethodInfo(TypeElement e1) {
        DataBindingInfo.BindMethodInfo bmi = new DataBindingInfo.BindMethodInfo();
        for(AnnotationMirror am : e1.getAnnotationMirrors()) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
                ExecutableElement key = en.getKey();//the method of annotation
                final AnnotationValue enVal = en.getValue();
                String fullName = key.getSimpleName().toString();
                if (fullName.equals("value")) {
                    bmi.name = String.valueOf(enVal.getValue().toString());
                } else if (fullName.equals("paramTypes")) {
                    List mirrorList = (List) enVal.getValue();
                    convertToClassname(mirrorList, bmi.types);
                }
            }
        }
        return bmi;
    }

    private boolean parseBindsInternal(DataBindingInfo info, String varName,  int index,String[] props, BindMethod[] methods) {
        final ProcessorPrinter pp = mContext.getProcessorPrinter();
        if(props.length == 0){
            pp.error(TAG, "parseBindsProperty", "props.length must > 0");
            return false;
        }
        if(props.length > methods.length){
            pp.error(TAG, "parseBindsProperty", "props.length can't > methods.length");
            return false;
        }
        //truncate if props.length < methods.length
        if(props.length < methods.length){
            methods = truncate(methods, props.length);
        }
        for(int i =0 ; i < props.length ; i ++){
            final String prop = props[i];
            if(prop == null || prop.isEmpty()){
                continue;
            }
            List<String> types = null;
            //read Class<?> in compile time is wrong. see https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/.
            try {
                methods[i].paramTypes();
            }catch (MirroredTypesException mte){
                List<? extends TypeMirror> mirrors = mte.getTypeMirrors();
                types = convertToClassname(mirrors, null);
            }
            info.addBindInfo(new DataBindingInfo.BindInfo(varName, prop, index, methods[i].value(), types));
        }
        return true;
    }

    //often is TypeMirror , but primitive may cause ClassCastException
    private List<String> convertToClassname(List<?> mirrors,@Nullable List<String> out) {
        if(out == null){
            out = new ArrayList<>();
        }else{
            out.clear();
        }
        for(int i = 0 , size = mirrors.size() ; i < size ; i++){
            String type = mirrors.get(i).toString();
            if(type.endsWith(".class")){
                type = type.substring(0, type.lastIndexOf("."));
            }
            out.add(type);
        }
        return out;
    }

    private BindMethod[] truncate(BindMethod[] methods, int length) {
        BindMethod[] arr = new BindMethod[length];
        for(int i = 0 ; i < length ; i++){
            arr[i] = methods[i];
        }
        return arr;
    }

    //eg: @BinderClass(XXX.class)
    private TypeMirror getSimpleTypeMirror(AnnotationMirror am) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> map = am.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> en : map.entrySet()) {
            ExecutableElement key = en.getKey();//the method of annotation
            if(key.getSimpleName().toString().equals("value")){
                return (TypeMirror) en.getValue().getValue();
            }
        }
        return null;
    }
}
