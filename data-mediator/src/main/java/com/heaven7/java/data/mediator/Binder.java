package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * the  binder of module data.
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public class Binder<T> {

    private final Map<Object, DataMediatorCallback<T>> mMap;
    private final DataMediator<T> mMediator;

    /**
     * create binder for target data mediator.
     * @param mMediator the target data mediator.
     */
    protected Binder(DataMediator<T> mMediator) {
        this.mMediator = mMediator;
        this.mMap = new HashMap<>();
    }

    /**
     * get the target data mediator.
     * @return  the data mediator.
     */
    public DataMediator<T> getDataMediator(){
        return mMediator;
    }

    /**
     * get the real module data.
     * @return the real module data.
     */
    public T getData(){
        return mMediator.getData();
    }

    /**
     * get the proxy of module data.
     * @return the proxy of module data.
     */
    public T getDataProxy(){
        return mMediator.getDataProxy();
    }

    /**
     * cast current binder to the target .
     * @param clazz the target binder class.
     * @param <B> the binder type. eg: AndroidBinder.
     * @return the new binder or exception if cast failed .
     */
    public <B extends Binder<T>> B castTo(Class<B> clazz){
        return (B) this;
    }

    /**
     * bind the property callback for target property.
     * @param property the property. like 'name' of student.
     * @param callback the property callback of binder
     * @return this.
     */
    public Binder<T> bind(String property, BinderCallback<T> callback){
        DataMediatorCallback<T> temp = DataMediatorCallback.create(property, (PropertyCallback<T>) callback);
        if(callback.getTag() != null){
            mMap.put(callback.getTag(), temp);
        }
        mMediator.addDataMediatorCallback(temp);
        return this;
    }
   // public abstract Binder<T> unbind(String property);

    /**
     * unbind the property callback for target view object.
     * @param tag the tag of property callback
     * @return this.
     */
    public Binder<T> unbind(Object tag){
        final DataMediatorCallback<T> callback = mMap.remove(tag);
        if(callback != null){
            mMediator.removeDataMediatorCallback(callback);
        }
        return this;
    }
    /**
     * unbind all property callbacks.
     */
    public void unbindAll(){
        mMap.clear();
        mMediator.removeDataMediatorCallbacks();
    }

    /**
     * the callback of binder.
     * @param <T> the module data type.
     * @since 1.0.8
     */
    public static abstract class BinderCallback<T> implements ListPropertyCallback<T>{

        /**
         * get the tag of this callback. if not null. it can be unbind as the key by call
         * {@linkplain Binder#unbind(Object)}.
         * @return the tag. or null if no need unbind.
         */
        public @Nullable Object getTag(){
            return null;
        }

        @Override
        public void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue) {

        }
        @Override
        public void onAddPropertyValuesWithIndex(T data, Property prop, Object newValue,
                                                 Object addedValue, int index) {

        }
        @Override
        public void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue) {

        }
        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {

        }
    }

}
