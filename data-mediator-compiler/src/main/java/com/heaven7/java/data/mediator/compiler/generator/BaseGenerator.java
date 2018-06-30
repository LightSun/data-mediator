package com.heaven7.java.data.mediator.compiler.generator;

import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.compiler.FieldData;
import com.heaven7.java.data.mediator.compiler.ProcessorContext;
import com.heaven7.java.data.mediator.compiler.ProcessorPrinter;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

/**
 * Created by heaven7 on 2017/11/5.
 * @since 1.4.0
 */
public class BaseGenerator {

    protected final String TAG = getClass().getSimpleName();

    private final ProcessorContext mContext;

    public BaseGenerator(ProcessorContext context) {
        this.mContext = context;
    }

    public ProcessorContext getContext() {
        return mContext;
    }
    public ProcessorPrinter getProcessorPrinter() {
        return mContext.getProcessorPrinter();
    }
    public Filer getFiler() {
        return mContext.getFiler();
    }
    public Elements getElements() {
        return mContext.getElements();
    }
    public Types getTypes() {
        return mContext.getTypes();
    }

    /**
     * call this you need override {@linkplain #hasAnnotation(TypeElement)}.
     * @param te the type element.
     * @param delegate the delegate
     * @return the super interface as type element.
     */
    public final TypeElement getSuperInterface(TypeElement te, GroupPropertyGenerator.TypeElementDelegate delegate) {
        List<? extends TypeMirror> interfaces = te.getInterfaces();
        for (TypeMirror tm : interfaces){
            TypeElement result = getSuperInterfaceImpl(tm, delegate);
            if(result != null){
                return result;
            }
        }
        return null;
    }

    private TypeElement getSuperInterfaceImpl(TypeMirror tm, GroupPropertyGenerator.TypeElementDelegate delegate) {
        String superName = tm.toString();
        if(tm instanceof NoType){
            //no super.
        }else if(superName.startsWith("java.") || superName.startsWith("android.")){
            // no super too.
        }else {
            TypeElement newTe = new FieldData.TypeCompat(getTypes(), tm).getElementAsType();
            TypeElement ele = delegate.get(superName);
            if(ele == null){
                if(hasAnnotation(newTe)){
                    return newTe;
                }
                return getSuperInterface(newTe, delegate);
            }else {
                return newTe;
            }
        }
        return null;
    }

    protected boolean hasAnnotation(TypeElement te) {
        Fields fields = te.getAnnotation(Fields.class);
        return fields != null && fields.groups().length > 0;
    }
}
