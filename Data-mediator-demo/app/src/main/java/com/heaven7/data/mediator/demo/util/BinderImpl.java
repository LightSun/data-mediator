package com.heaven7.data.mediator.demo.util;

/**
 * Created by heaven7 on 2017/9/22.
 */
/*
public class BinderImpl<T> {

    private final WeakHashMap<Object, DataMediatorCallback<T>> mMap;
    private final DataMediator<T> mMediator;

    public BinderImpl(DataMediator<T> mMediator) {
        this.mMediator = mMediator;
        this.mMap = new WeakHashMap<>();
    }
    public BinderImpl bind(String property, TextView view){
        return bind(property, view, new TextPropertyCallback<T>(view));
    }
    public BinderImpl bind(String property, View key, PropertyCallback<T> callback){
        Throwables.checkNull(key);
        final DataMediatorCallback<T> temp = DataMediatorCallback.create(property, callback);
        mMap.put(key, temp);
        mMediator.addDataMediatorCallback(temp);
        return this;
    }
    public  BinderImpl bind(String property, RecyclerView view){
        final RecyclerView.Adapter adapter = view.getAdapter();
        if(adapter == null || !(adapter instanceof ListPropertyCallback)){
            throw new IllegalStateException("must set adapter and the adapter " +
                    "must impl ListPropertyCallback !");
        }
        return bind(property, view, (ListPropertyCallback<T>) adapter);
    }
    public  BinderImpl bind(String property, View key, ListPropertyCallback<T> callback){
        Throwables.checkNull(key);
        final DataMediatorCallback<T> temp = DataMediatorCallback.create(property, callback);
        mMap.put(key, temp);
        mMediator.addDataMediatorCallback(temp);
        return this;
    }
    public BinderImpl unbind(View key){
        final DataMediatorCallback<T> callback = mMap.remove(key);
        if(callback != null){
            mMediator.removeDataMediatorCallback(callback);
        }
        return this;
    }
    public void unbindAll(){
        mMap.clear();
        mMediator.removeDataMediatorCallbacks();
    }

    private static class TextPropertyCallback<T> extends SimplePropertyCallback<T>{
        public TextPropertyCallback(View tv) {
            super(tv);
        }
        @Override
        protected void apply(View view, Object newValue) {
            ((TextView)view).setText(String.valueOf(newValue));
        }
    }

    public static abstract class SimplePropertyCallback<T> implements PropertyCallback<T> {

        final WeakReference<View> mWeakTextView;

        public SimplePropertyCallback(View tv) {
            this.mWeakTextView = new WeakReference<>(tv);
        }
        @Override
        public void onPropertyValueChanged(T data, Property prop,
                                           Object oldValue, Object newValue) {
            View view = mWeakTextView.get();
            if(view == null){
                //view is recycled
                return;
            }
            apply(view, newValue);
        }

        protected abstract void apply(View view, Object newValue);
    }

}
*/
