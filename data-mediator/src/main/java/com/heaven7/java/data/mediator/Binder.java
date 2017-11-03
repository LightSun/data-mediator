/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.batchbind.BatchTextViewBinder;
import com.heaven7.java.data.mediator.batchbind.BatchViewBinder;

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
     * get the property interceptor which is used by calling {@linkplain #applyProperties()} and all bind method,
     * default is {@linkplain PropertyInterceptor#NULL}
     * @return the property interceptor.
     * @since 1.1.3
     */
    public PropertyInterceptor getPropertyInterceptor() {
        return mMediator.getPropertyInterceptor();
    }

    /**
     * set the property interceptor which is used by calling {@linkplain #applyProperties()} and all bind method.
     * default is {@linkplain PropertyInterceptor#NULL}
     * @param interceptor the target property interceptor.
     * @since 1.1.3
     */
    public void setPropertyInterceptor(PropertyInterceptor interceptor) {
        mMediator.setPropertyInterceptor(interceptor);
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
     * apply all current properties. that means notify all property value apply.
     * @since 1.0.8
     */
    public void applyProperties(){
        getDataMediator().applyProperties();
    }
    /**
     * apply all current properties with target interceptor. that means notify all property value apply.
     * @param interceptor the property interceptor
     * @since 1.0.8
     */
    public void applyProperties(PropertyInterceptor interceptor){
        getDataMediator().applyProperties(interceptor);
    }

    /**
     * bind the property callback for target property.
     * @param property the property. it often auto generate by 'data-mediator-compiler' lib.
     * @param callback the property callback of binder
     * @return this.
     * @since 1.1.3
     */
    public Binder<T> bind(Property property, BinderCallback<? super T> callback){
        return bind(property.getName(), callback);
    }
    /**
     * bind the property callback for target property.
     * @param property the property. like 'name' of student.
     * @param callback the property callback of binder
     * @return this.
     */
    public Binder<T> bind(String property, BinderCallback<? super T> callback){
        DataMediatorCallback<T> temp = DataMediatorCallback.create(property, callback);
        if(callback.getTag() != null){
            mMap.put(callback.getTag(), temp);
        }
        mMediator.addDataMediatorCallback(temp);
        return this;
    }
    /**
     * bind the property callback as Sparse({@linkplain com.heaven7.java.base.util.SparseArray})
     * for target property.
     * @param property the property.
     * @param callback the property callback of sparse array.
     * @return this.
     * @see com.heaven7.java.base.util.SparseArray
     * @since 1.1.3
     */
    public Binder<T> bind(Property property, SparseArrayPropertyCallback<? super T> callback){
        return bind(property.getName(), callback);
    }
    /**
     * bind the property callback as Sparse({@linkplain com.heaven7.java.base.util.SparseArray})
     * for target property.
     * @param property the property. like 'name' of student.
     * @param callback the property callback of sparse array.
     * @return this.
     * @see com.heaven7.java.base.util.SparseArray
     * @since 1.1.3
     */
    public Binder<T> bind(String property, SparseArrayPropertyCallback<? super T> callback){
        DataMediatorCallback<T> temp = DataMediatorCallback.createForSparse(property,
                callback);
        if(callback instanceof Tagable){
            Object tag = ((Tagable) callback).getTag();
            if(tag != null){
                mMap.put(tag, temp);
            }
        }
        mMediator.addDataMediatorCallback(temp);
        return this;
    }

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
     * unbind all property callbacks. and auto recycle if need.
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

    //============================ batch binder =================================================

    /**
     * begin batch view binder
     * @param view the view. on android is any child of "android.view.View"
     * @return batch view binder.
     */
    public BatchViewBinder<T> beginBatchViewBinder(Object view){
        return new BatchViewBinder<T>(this, view);
    }
    /**
     * begin batch text view binder
     * @param view the text view. on android is any child of "android.widget.TextView"
     * @return batch text view binder.
     */
    public BatchTextViewBinder<T> beginBatchTextViewBinder(Object view){
        return new BatchTextViewBinder<T>(this, view);
    }

    //============================== special =========================================//

    /**
     * bind checkable of view.
     * @param property the property of data
     * @param checkableView the check view. eg: android.widget.CheckBox on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindCheckable(Property property, Object checkableView){
        return bindCheckable(property.getName(), checkableView);
    }

    /**
     * bind visibility of view (on android the visibility is masked as int.).
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @param forceAsBoolean  force to use property as boolean or not. if true that means on android:
     *                      only support  View.GONE(false) and View.VISIBLE(true).
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindVisibility(Property property, Object view, boolean forceAsBoolean){
        return bindVisibility(property.getName(), view, forceAsBoolean);
    }
    /**
     * bind visibility of view (on android the visibility is masked as int.).
     * here force use property of visibility as boolean.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindVisibility(Property property, Object view){
        return bindVisibility(property.getName(), view, true);
    }

    /**
     * bind enable of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindEnable(Property property, Object view){
        return bindEnable(property.getName(), view);
    }
    /**
     * bind background resource of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindBackgroundRes(Property property, Object view){
        return bindBackgroundRes(property.getName(), view);
    }

    /**
     * bind background drawable of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindBackground(Property property, Object view){
        return bindBackground(property.getName(), view);
    }
    /**
     * bind background color of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindBackgroundColor(Property property, Object view){
        return bindBackgroundColor(property.getName(), view);
    }

    /**
     * bind text of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindText(Property property, Object textView){
        return bindText(property.getName(), textView);
    }
    /**
     * bind text resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextRes(Property property, Object textView){
        return bindTextRes(property.getName(), textView);
    }
    /**
     * bind text color of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextColor(Property property, Object textView){
        return bindTextColor(property.getName(), textView);
    }
    /**
     * bind text color resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextColorRes(Property property, Object textView){
        return bindTextColorRes(property.getName(), textView);
    }
    /**
     * bind text size resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextSizeRes(Property property,Object textView){
        return bindTextSizeRes(property.getName(), textView);
    }
    /**
     * bind text size of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextSize(Property property, Object textView){
        return bindTextSize(property.getName(), textView);
    }
    /**
     * bind text dimension size(dp value) of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindTextSizeDp(Property property, Object textView){
        return bindTextSizeDp(property.getName(), textView);
    }
    /**
     * bind image url of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @param imgLoader the image loader which can load image.
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindImageUrl(Property property, Object imageView, Object imgLoader){
        return bindImageUrl(property.getName(), imageView, imgLoader);
    }
    /**
     * bind image uri (android.net.Uri) of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     * @since 1.1.2
     */
    public  Binder<T> bindImageUri(Property property, Object imageView){
        return bindImageUri(property.getName(), imageView);
    }
    /**
     * bind image resource of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindImageRes(Property property, Object imageView){
        return bindImageRes(property.getName(), imageView);
    }
    /**
     * bind image drawable of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindImageDrawable(Property property, Object imageView){
        return bindImageDrawable(property.getName(), imageView);
    }
    /**
     * bind image bitmap of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindImageBitmap(Property property,  Object imageView){
        return bindImageBitmap(property.getName(), imageView);
    }
    /**
     * bind list of recycler view.
     * @param property the property of data
     * @param recyclerView the recycler view. eg: android.support.v7.widget.RecyclerView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindRecyclerList(Property property, Object recyclerView){
        return bindRecyclerList(property.getName(), recyclerView);
    }
    /**
     * bind list of view.
     * @param property the property of data
     * @param listView the list view. eg: android.widget.ListView on android platform
     * @return this.
     * @since 1.1.2
     */
    public Binder<T> bindList(Property property, Object listView){
        return bindList(property.getName(), listView);
    }
    /**
     * bind visibility of view (on android the visibility is masked as int.).
     * here force use property of visibility as boolean.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public Binder<T> bindVisibility(String property, Object view){
        return bindVisibility(property, view, true);
    }
    //============================== abstract =========================================//
    /**
     * bind checkable of view.
     * @param property the property of data
     * @param checkableView the check view. eg: android.widget.CheckBox on android platform
     * @return this.
     */
    public abstract Binder<T> bindCheckable(String property, Object checkableView);
    /**
     * bind visibility of view (on android the visibility is masked as int.).
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @param forceAsBoolean  force to use property as boolean or not. if true that means on android:
     *                      only support  View.GONE(false) and View.VISIBLE(true).
     * @return this.
     */
    public abstract Binder<T> bindVisibility(String property, Object view, boolean forceAsBoolean);
    /**
     * bind enable of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindEnable(String property, Object view);
    /**
     * bind background resource of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackgroundRes(String property, Object view);
    /**
     * bind background drawable of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackground(String property, Object view);
    /**
     * bind background color of view.
     * @param property the property of data
     * @param view the view. eg: android.view.View on android platform
     * @return this.
     */
    public abstract Binder<T> bindBackgroundColor(String property, Object view);
    /**
     * bind text of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindText(String property, Object textView);
    /**
     * bind text resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextRes(String property, Object textView);
    /**
     * bind text color of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextColor(String property, Object textView);
    /**
     * bind text color resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextColorRes(String property, Object textView);
    /**
     * bind text size resource of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSizeRes(String property,Object textView);
    /**
     * bind text dimension size(pixes) of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSize(String property, Object textView);
    /**
     * bind text dimension size(dp value) of Text view.
     * @param property the property of data
     * @param textView the text view. eg: android.widget.TextView on android platform
     * @return this.
     */
    public abstract Binder<T> bindTextSizeDp(String property, Object textView);
    /**
     * bind image url of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @param imgLoader the image loader which can load image.
     * @return this.
     */
    public abstract Binder<T> bindImageUrl(String property, Object imageView, Object imgLoader);
    /**
     * bind image uri (android.net.Uri) of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageUri(String property, Object imageView);
    /**
     * bind image resource of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageRes(String property, Object imageView);

    /**
     * bind image drawable of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageDrawable(String property, Object imageView);
    /**
     * bind image bitmap of image view.
     * @param property the property of data
     * @param imageView the image view. eg: android.widget.ImageView on android platform
     * @return this.
     */
    public abstract Binder<T> bindImageBitmap(String property,  Object imageView);
    /**
     * bind list of recycler view.
     * @param property the property of data
     * @param recyclerView the recycler view. eg: android.support.v7.widget.RecyclerView on android platform
     * @return this.
     */
    public abstract Binder<T> bindRecyclerList(String property, Object recyclerView);
    /**
     * bind list of view.
     * @param property the property of data
     * @param listView the list view. eg: android.widget.ListView on android platform
     * @return this.
     */
    public abstract Binder<T> bindList(String property, Object listView);

    /**
     * represent the object can be tagged or not.
     * @since 1.1.3
     */
    public interface Tagable{
        /**
         * get the tag
         * @return the tag
         */
        Object getTag();
    }
    /**
     * the callback of binder.
     * @param <T> the module data type.
     * @since 1.0.8
     */
    public interface BinderCallback<T> extends ListPropertyCallback<T>, Tagable{

        /**
         * get the tag of this callback. if not null. it can be unbind as the key by call
         * {@linkplain Binder#unbind(Object)}.
         * @return the tag. or null if no need unbind.
         */
         @Nullable Object getTag();
    }

    /**
     * a adapter class that adapt the {@linkplain SparseArrayPropertyCallback} and {@linkplain Tagable}
     *  @param <T> the module data type
     *  @since 1.1.3
     */
    public static class SimpleSparseArrayPropertyCallback<T> implements SparseArrayPropertyCallback<T>, Tagable{

        @Override
        public void onEntryValueChanged(T data, Property prop, Integer key, Object oldValue, Object newValue) {

        }
        @Override
        public void onAddEntry(T data, Property prop, Integer key, Object value) {

        }
        @Override
        public void onRemoveEntry(T data, Property prop, Integer key, Object value) {

        }
        @Override
        public void onClearEntries(T data, Property prop, Object entries) {

        }
        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {

        }
        @Override
        public void onPropertyApplied(T data, Property prop, Object value) {

        }
        @Override
        public Object getTag() {
            return null;
        }
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
        public void onPropertyItemChanged(T data, Property prop, Object oldItem, Object newItem, int index) {

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
