package com.heaven7.android.data.mediator;

import android.view.View;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import java.lang.ref.WeakReference;

/**
 * simple binder callback.
 * @param <T> the module data type.
 */
public abstract class SimpleBinderCallback2<T> extends Binder.SimpleBinderCallback<T> {

    private final WeakReference<View> mWeakTextView;
    private final PropertyInterceptor mInterceptor;

    /**
     * create binder callback with default interceptor.
     * @param tv the view.
     */
    public SimpleBinderCallback2(View tv) {
        this(tv, PropertyInterceptor.NULL_AND_ZERO);
    }

    /**
     * create binder callback with view and property interceptor.
     * @param tv the view
     * @param interceptor the interceptor.
     */
    public SimpleBinderCallback2(View tv, PropertyInterceptor interceptor) {
        this.mWeakTextView = new WeakReference<>(tv);
        this.mInterceptor = interceptor;
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
        if(mInterceptor != null && mInterceptor.shouldIntercept(data, prop, newValue)){
            return;
        }
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

}
