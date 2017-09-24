package com.heaven7.android.data.mediator;

import android.view.View;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.Property;

import java.lang.ref.WeakReference;

/**
 * simple binder callback.
 * @param <T> the module data type.
 */
public abstract class SimpleBinderCallback2<T> extends Binder.SimpleBinderCallback<T> {

    private final WeakReference<View> mWeakTextView;
   // private final Object mDefaultValue;

    public SimpleBinderCallback2(View tv) {
        this.mWeakTextView = new WeakReference<>(tv);
       // this.mDefaultValue = getDefaultValue(tv);
    }

    @Override
    public Object getTag() {
        return mWeakTextView.get();
    }

    @Override
    public void onPropertyValueChanged(T data, Property prop,
                                       Object oldValue, Object newValue) {
        View view = mWeakTextView.get();
        if (view == null) {
            //view is recycled
            return;
        }
        /*Object value = newValue;
        if(newValue == null){
            value = mDefaultValue;
        }else{
            if(newValue instanceof Integer || newValue instanceof Long ||
                    newValue instanceof Short || newValue instanceof Byte
                    ){
                if(Long.valueOf(newValue.toString()) == 0){
                    value = mDefaultValue;
                }
            }else if(newValue instanceof Float || newValue instanceof Double){
                if(Double.valueOf(newValue.toString()) == 0d){
                    value = mDefaultValue;
                }
            }
            //char, boolean ignore
        }*/
        apply(prop, view, newValue);
    }

    /**
     * apply the new value to the view.
     *
     * @param prop     the property desc
     * @param view     the view
     * @param newValue the new value.
     */
    protected abstract void apply(Property prop, View view, Object newValue);

    /**
     * get the default value.
     * @param view the default value.
     * @return the default value of property.
     */
    //protected abstract Object getDefaultValue(View view);
}
