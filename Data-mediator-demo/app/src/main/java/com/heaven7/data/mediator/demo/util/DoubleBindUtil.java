package com.heaven7.data.mediator.demo.util;

import android.text.Editable;
import android.widget.TextView;

import com.heaven7.android.util2.WeakObject;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.TextWatcherAdapter;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorCallback;
import com.heaven7.java.data.mediator.Property;

import java.lang.reflect.Method;

/**
 * this is a double bind util .
 * Created by heaven7 on 2017/9/15 0015.
 */

public class DoubleBindUtil {

    public static <T> void bindDouble(final DataMediator<T> mediator, TextView tv,
                                      final String propertyName){

        DataMediatorCallbackImpl<T> callback = new DataMediatorCallbackImpl<>(tv, propertyName);
        TextWatcherAdapterImpl<T> watcher = new TextWatcherAdapterImpl<>(tv, mediator, propertyName);
        callback.attachWatcher(watcher);
        mediator.addDataMediatorCallback(callback);
        tv.addTextChangedListener(watcher);
    }

    private static class TextWatcherAdapterImpl<T> extends TextWatcherAdapter{

        final TextView mTv;
        final String propertyName;
        Method setMethod;
        boolean enabled = true;

        public TextWatcherAdapterImpl(TextView mTv, DataMediator<T> mediator, String propertyName) {
            this.mTv = mTv;
            this.propertyName = propertyName;

            T target = mediator.getData();
            mTv.setTag(R.id.tv_desc, target);
            String name = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
            try {
                setMethod = target.getClass().getMethod("set" + name, String.class);//String. 应该动态获取
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!enabled){
                return;
            }
            T target= (T) mTv.getTag(R.id.tv_desc);
            try {
                setMethod.invoke(target, s.toString());
            } catch (Exception e) {
                Logger.w("init","TextWatcherAdapterImpl","" + target);
                throw new RuntimeException(e);
            }
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    private static class DataMediatorCallbackImpl<T> extends DataMediatorCallback<T>{

        final WeakObject<TextView> mWeakTextView;
        final String propertyName;
        WeakObject<TextWatcherAdapterImpl<T>> mWeakWatcher;

        public DataMediatorCallbackImpl(TextView tv, String propertyName) {
            this.mWeakTextView = new WeakObject<>(tv);
            this.propertyName = propertyName;
        }

        public void attachWatcher(TextWatcherAdapterImpl<T> watcher){
            mWeakWatcher = new WeakObject<>(watcher);
        }

        @Override
        public void onPropertyValueChanged(T data, Property prop,
                                           Object oldValue, Object newValue) {
            TextView tv = mWeakTextView.get();
            if(tv == null){
                //view is recycled
                return;
            }
            if(prop.getName().equals(propertyName)){
                TextWatcherAdapterImpl<T> watcher = mWeakWatcher.get();
                watcher.setEnabled(false);
                tv.setText(String.valueOf(newValue));
                watcher.setEnabled(true);
            }
        }
    }
}
