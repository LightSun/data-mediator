package com.heaven7.data.mediator.demo.util;

import android.widget.TextView;

import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorCallback;
import com.heaven7.java.data.mediator.Property;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by heaven7 on 2017/9/22.
 */
public class BinderImpl<T> {

    private final WeakHashMap<Object, DataMediatorCallback<T>> mMap;
    private final DataMediator<T> mMediator;

    public BinderImpl(DataMediator<T> mMediator) {
        this.mMediator = mMediator;
        this.mMap = new WeakHashMap<>();
    }

    public BinderImpl bind(TextView view , String property){
        final ViewCallbackImpl<T> callback = new ViewCallbackImpl<>(view, property);
        mMap.put(view, callback);
        mMediator.addDataMediatorCallback(callback);
        return this;
    }
    public  BinderImpl bindList(IDataMediatorAdapter adapter, String property){
        return this;
    }
    public BinderImpl unbind(TextView view){
        final DataMediatorCallback<T> callback = mMap.remove(view);
        if(callback != null){
            mMediator.removeDataMediatorCallback(callback);
        }
        return this;
    }
    public void unbindAll(){
        mMap.clear();
        mMediator.removeDataMediatorCallbacks();
    }

    public interface IDataMediatorAdapter{

    }

    private static class AdapterCallbckImpl<T> extends DataMediatorCallback<T>{
        final WeakReference<IDataMediatorAdapter> mWeakAdapter;
        final String propertyName;

        public AdapterCallbckImpl(IDataMediatorAdapter adapter, String propertyName) {
            this.mWeakAdapter = new WeakReference<>(adapter);
            this.propertyName = propertyName;
        }

        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            IDataMediatorAdapter adapter = mWeakAdapter.get();
            if(adapter == null){
                //view is recycled
                return;
            }
            if(prop.getName().equals(propertyName)){
                //  TODO adapter.apply(oldValue, newValue);
            }
        }
    }

    private static class ViewCallbackImpl<T> extends DataMediatorCallback<T>{

        final WeakReference<TextView> mWeakTextView;
        final String propertyName;

        public ViewCallbackImpl(TextView tv, String propertyName) {
            this.mWeakTextView = new WeakReference<>(tv);
            this.propertyName = propertyName;
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
                tv.setText(String.valueOf(newValue));
            }
        }
    }

}
