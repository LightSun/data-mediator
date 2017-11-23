/**
 * Copyright 2017
 * group of data-mediator
 * member: heaven7(donshine723@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.util.Throwables;

/**
 *  the listener of property change.
 * Created by heaven7 on 2017/9/11 0011.
 */
public abstract class DataMediatorCallback<T> extends PropertyCallbackContext implements PropertyCallback<T>, ListPropertyCallback<T> {

    @CalledInternal
    public void onPreCallback(Params params) {
        super.onPreCallback(params);
        SparseArrayPropertyCallback<T> callback = getSparseArrayPropertyCallback();
        if (callback != null && callback instanceof SparseArrayPropertyCallback2) {
            ((SparseArrayPropertyCallback2) callback).onPreCallback(params);
        }
    }

    @CalledInternal
    public void onPostCallback() {
        super.onPostCallback();
        SparseArrayPropertyCallback<T> callback = getSparseArrayPropertyCallback();
        if (callback != null && callback instanceof SparseArrayPropertyCallback2) {
            ((SparseArrayPropertyCallback2) callback).onPostCallback();
        }
    }
    /**
     * create data mediator callback for Sparse by target callback and property.
     * @param propertyName the property
     * @param callback the callback
     * @param <T> the data module type
     * @return the data mediator callback.
     * @see com.heaven7.java.base.util.SparseArray
     * @since 1.4.4
     */
    public static <T> DataMediatorCallback<T> createForSparse(String propertyName,
                                                              SparseArrayPropertyCallback2<? super T> callback) {
        Throwables.checkNull(propertyName);
        Throwables.checkNull(callback);
        return new WrappedSparseArrayCallback<T>(propertyName, callback) {
            @Override
            protected void onPreCallback(SparseArrayPropertyCallback<? super T> mSAPC) {
                SparseArrayPropertyCallback2<? super T> callback = (SparseArrayPropertyCallback2<? super T>) mSAPC;
                callback.onPreCallback(getParams());
            }

            @Override
            protected void onPostCallback(SparseArrayPropertyCallback<? super T> mSAPC) {
                SparseArrayPropertyCallback2<? super T> callback = (SparseArrayPropertyCallback2<? super T>) mSAPC;
                callback.onPostCallback();
            }
        };
    }

    /**
     * create data mediator callback for Sparse by target callback and property.
     * @param propertyName the property
     * @param callback the callback
     * @param <T> the data module type
     * @return the data mediator callback.
     * @see com.heaven7.java.base.util.SparseArray
     * @since 1.1.3
     */
    public static <T> DataMediatorCallback<T> createForSparse(String propertyName,
                                                              SparseArrayPropertyCallback<? super T> callback) {
        Throwables.checkNull(propertyName);
        Throwables.checkNull(callback);
        return new WrappedSparseArrayCallback<T>(propertyName, callback);
    }

    /**
     * create  {@linkplain DataMediatorCallback} by target property name and callback.
     * @param propertyName the property name
     * @param callback the list property callback
     * @param <T> the module data type
     * @return the data mediator callback.
     * @since 1.0.8
     */
    public static <T> DataMediatorCallback<T> create(String propertyName,
                                                     ListPropertyCallback<? super T> callback) {
        Throwables.checkNull(propertyName);
        Throwables.checkNull(callback);
        return new WrappedListCallback<T>(propertyName, callback);
    }

    /**
     * create  {@linkplain DataMediatorCallback} by target property name and callback.
     * @param propertyName the property name
     * @param callback the common property callback
     * @param <T> the module data type
     * @return the data mediator callback.
     * @since 1.0.8
     */
    public static <T> DataMediatorCallback<T> create(final String propertyName,
                                                     final PropertyCallback<? super T> callback) {
        Throwables.checkNull(propertyName);
        Throwables.checkNull(callback);
        return new NameableCallback<T>(propertyName) {
            @Override
            public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
                if (allow(prop)) {
                    callback.onPropertyValueChanged(data, prop, oldValue, newValue);
                }
            }
            @Override
            public void onPropertyApplied(T data, Property prop, Object value) {
                if (allow(prop)) {
                    callback.onPropertyApplied(data, prop, value);
                }
            }
        };
    }

    /**
     * called on property value changed.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     * @since 1.0
     */
    @Override
    public abstract void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue);

    @Override
    public void onPropertyApplied(T data, Property prop, Object value) {
        onPropertyValueChanged(data, prop, null, value);
    }

    /**
     * called on add property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @since 1.0.8
     */
    public void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue) {

    }

    /**
     * called on add property values with index.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @param index  the index to add.
     * @since 1.0.8
     */
    public void onAddPropertyValuesWithIndex(T data, Property prop,
                                             Object newValue, Object addedValue, int index) {

    }

    /**
     * called on remove property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param removeValue the value which is removed.
     * @since 1.0.8
     */
    public void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue) {

    }

    /**
     * called on property item changed.
     * @param data the data
     * @param prop the property
     * @param oldItem the old item
     * @param newItem the new item
     * @param index the index of old item
     * @since 1.1.2
     */
    public void onPropertyItemChanged(T data, Property prop, Object oldItem, Object newItem, int index) {

    }

    /**
     * get property callback of sparse array. if not exist return null.
     * @return the property callback of sparse array
     * @since 1.1.3
     */
    public SparseArrayPropertyCallback<T> getSparseArrayPropertyCallback() {
        return null;
    }

    /**
     * the wrapped sparse array callback
     * @param <T> the module data type
     * @since 1.1.3
     * @see SparseArrayPropertyCallback
     * @see SparseArrayPropertyCallback2
     */
    private static class WrappedSparseArrayCallback<T> extends NameableCallback<T>
            implements SparseArrayPropertyCallback<T> {
        final SparseArrayPropertyCallback<? super T> mSAPC;
        SparseArrayPropertyCallback2<T> mSAPC2;

        WrappedSparseArrayCallback(String prop, SparseArrayPropertyCallback<? super T> mSAPC) {
            super(prop);
            this.mSAPC = mSAPC;
        }

        protected void onPreCallback(SparseArrayPropertyCallback<? super T> mSAPC) {

        }

        protected void onPostCallback(SparseArrayPropertyCallback<? super T> mSAPC) {

        }
        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onPropertyValueChanged(data, prop, oldValue, newValue);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public void onPropertyApplied(T data, Property prop, Object value) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onPropertyApplied(data, prop, value);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public void onEntryValueChanged(T data, Property prop, Integer key,
                                        Object oldValue, Object newValue) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onEntryValueChanged(data, prop, key, oldValue, newValue);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public void onAddEntry(T data, Property prop,
                               Integer key, Object value) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onAddEntry(data, prop, key, value);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public void onRemoveEntry(T data, Property prop,
                                  Integer key, Object value) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onRemoveEntry(data, prop, key, value);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public void onClearEntries(T data, Property prop, Object newMap) {
            if (allow(prop)) {
                onPreCallback(mSAPC);
                mSAPC.onClearEntries(data, prop, newMap);
                onPostCallback(mSAPC);
            }
        }

        @Override
        public SparseArrayPropertyCallback<T> getSparseArrayPropertyCallback() {
            if (mSAPC2 == null) {
                mSAPC2 = new SparseArrayPropertyCallback2.WrappedSparseArrayPropertyCallback2<T>(mSAPC);
            }
            return mSAPC2;
        }
    }

    /**
     * the wrapped list property callback
     * @param <T> the module data type
     * @since 1.0.8
     */
    private static class WrappedListCallback<T> extends NameableCallback<T> {
        final ListPropertyCallback<? super T> callback;

        public WrappedListCallback(String propertyName, ListPropertyCallback<? super T> callback) {
            super(propertyName);
            this.callback = callback;
        }

        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            if (allow(prop)) {
                callback.onPropertyValueChanged(data, prop, oldValue, newValue);
            }
        }

        @Override
        public void onPropertyApplied(T data, Property prop, Object value) {
            if (allow(prop)) {
                callback.onPropertyApplied(data, prop, value);
            }
        }

        @Override
        public void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue) {
            if (allow(prop)) {
                callback.onAddPropertyValues(data, prop, newValue, addedValue);
            }
        }

        @Override
        public void onAddPropertyValuesWithIndex(T data, Property prop, Object newValue,
                                                 Object addedValue, int index) {
            if (allow(prop)) {
                callback.onAddPropertyValuesWithIndex(data, prop, newValue, addedValue, index);
            }
        }

        @Override
        public void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue) {
            if (allow(prop)) {
                callback.onRemovePropertyValues(data, prop, newValue, removeValue);
            }
        }

        @Override
        public void onPropertyItemChanged(T data, Property prop, Object oldItem, Object newItem, int index) {
            if (allow(prop)) {
                callback.onPropertyItemChanged(data, prop, oldItem, newItem, index);
            }
        }
    }

    /*private*/ static abstract class NameableCallback<T> extends DataMediatorCallback<T> implements Nameable {

        protected final String name;

        public NameableCallback(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        protected boolean allow(Property property){
            return property.getName().equals(name)/* && (getParams() == null || getParams().getDepth() == 0)*/;
        }
    }

}
