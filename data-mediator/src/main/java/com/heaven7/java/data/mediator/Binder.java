package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * the  binder of module data. help we bind the data to the view.
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public abstract class Binder<T> {

    private final Map<Object, DataMediatorCallback<T>> mMap;
    private final DataMediator<T> mMediator;

    /**
     * create binder for target data mediator.
     * @param mMediator the target data mediator.
     */
    protected Binder(DataMediator<T> mMediator) {
        Throwables.checkNull(mMediator);
        this.mMediator = mMediator;
        this.mMap = shouldUseWeakMap() ? new WeakHashMap<Object, DataMediatorCallback<T>>()
                : new HashMap<Object, DataMediatorCallback<T>>();
    }

    /**
     * get the target data mediator.
     * @return  the data mediator.
     */
    public DataMediator<T> getDataMediator(){
        return mMediator;
    }

    /**
     * apply all current properties. that means notify all property value changed except
     * empty(null or no element).
     * @since 1.0.8
     */
    public void apply(){
        getDataMediator().apply();
    }
    /**
     * get the real module data.
     * @return the real module data.
     */
    public T getData(){
        return getDataMediator().getData();
    }

    /**
     * get the proxy of module data.
     * @return the proxy of module data.
     */
    public T getDataProxy(){
        return getDataMediator().getDataProxy();
    }

    /**
     * bind the property callback for target property.
     * @param property the property. like 'name' of student.
     * @param callback the property callback of binder
     * @return this.
     */
    public Binder<T> bind(String property, BinderCallback<T> callback){
        DataMediatorCallback<T> temp = DataMediatorCallback.create(property, callback);
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
     * should use weak map to save callback which can later call
     * {@linkplain #unbind(Object)} if not weak.
     * @return true if should use weak map to save.
     */
    protected boolean shouldUseWeakMap(){
        return false;
    }

    //============================== special =========================================//

    /**
     * bind checkable of view.
     * @param property the property
     * @param checkableView the check view. eg: android.widget.CheckBox on android platform
     * @return this.
     */
    public abstract Binder<T> bindCheckable(String property, Object checkableView);
    /**
     * bind enable of view.
     * @param property the property
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindEnable(String property, Object view);
    /**
     * bind text of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindText(String property, Object textView);
    /**
     * bind text resource of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextRes(String property, Object textView);
    /**
     * bind text color of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextColor(String property, Object textView);
    /**
     * bind text color resource of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextColorRes(String property, Object textView);
    /**
     * bind text size resource of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSizeRes(String property,Object textView);
    /**
     * bind text size of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSize(String property, Object textView);
    /**
     * bind text dimension size(dp value) of Text view.
     * @param property the property
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSizeDp(String property, Object textView);
    /**
     * bind background resource of view.
     * @param property the property
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackgroundRes(String property, Object view);
    /**
     * bind background drawable of view.
     * @param property the property
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackground(String property, Object view);
    /**
     * bind background color of view.
     * @param property the property
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackgroundColor(String property, Object view);
    /**
     * bind image url of image view.
     * @param property the property
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @param imgLoader the image loader which can load image.
     * @return this.
     */
    public abstract Binder<T> bindImageUrl(String property, Object imageView, Object imgLoader);
    /**
     * bind image uri (android.net.Uri) of image view.
     * @param property the property
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageUri(String property, Object imageView);
    /**
     * bind image resource of image view.
     * @param property the property
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageRes(String property, Object imageView);
    /**
     * bind image drawable of image view.
     * @param property the property
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageDrawable(String property, Object imageView);
    /**
     * bind image bitmap of image view.
     * @param property the property
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageBitmap(String property,  Object imageView);

    /**
     * bind list of recycler view.
     * @param property the property
     * @param recyclerView the recycler view. eg: android.support.v7.widget.RecyclerView on android platform
     * @return this.
     */
    public abstract Binder<T> bindRecyclerList(String property, Object recyclerView);
    /**
     * bind list of view.
     * @param property the property
     * @param listView the list view. eg: android.widget.ListView on android platform
     * @return this.
     */
    public abstract Binder<T> bindList(String property, Object listView);

    /**
     * the callback of binder.
     * @param <T> the module data type.
     * @since 1.0.8
     */
    public interface BinderCallback<T> extends ListPropertyCallback<T>{

        /**
         * get the tag of this callback. if not null. it can be unbind as the key by call
         * {@linkplain Binder#unbind(Object)}.
         * @return the tag. or null if no need unbind.
         */
         @Nullable Object getTag();
    }

    /**
     * simple binder callback
     * @param <T> the module data type
     * @since 1.0.8
     */
    public static class SimpleBinderCallback<T> implements BinderCallback<T>{

        @Override
        public Object getTag() {
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
        @Override
        public void onPropertyApplied(T data, Property prop, Object value) {
            onPropertyValueChanged(data, prop, null, value);
        }
    }

}
