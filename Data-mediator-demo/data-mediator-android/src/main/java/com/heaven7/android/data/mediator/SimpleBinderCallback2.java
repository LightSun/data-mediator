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

    public SimpleBinderCallback2(View tv) {
        this.mWeakTextView = new WeakReference<>(tv);
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
