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

import com.heaven7.java.base.util.Throwables;

import java.util.List;

/**
 * the list binder callback
 * @param <T> the item module data type.
 * @author heaven7
 * @since 1.3.1
 */
public class BaseListPropertyCallback<T> implements ListPropertyCallback<Object> {

    private final IItemManager<T> mCallback;

    /**
     * create list binder callback with item manager.
     * @param mCallback the item manager.
     */
    public BaseListPropertyCallback(IItemManager<T> mCallback) {
        Throwables.checkNull(mCallback);
        this.mCallback = mCallback;
    }

    @Override
    public void onAddPropertyValues(Object data, Property prop, Object newValue, Object addedValue) {
        List<T> items = (List<T>) addedValue;
        mCallback.addItems(items);
    }
    @Override
    public void onAddPropertyValuesWithIndex(Object data, Property prop, Object newValue,
                                             Object addedValue, int index) {
        List<T> items = (List<T>) addedValue;
        mCallback.addItems(index, items);
    }
    @Override
    public void onRemovePropertyValues(Object data, Property prop, Object newValue, Object removeValue) {
        List<T> items = (List<T>) removeValue;
        mCallback.removeItems(items);
    }
    @Override
    public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
        if(newValue != null) {
            List<T> items = (List<T>) newValue;
            if(!items.isEmpty()) {
                mCallback.replaceItems(items);
            }
        }
    }
    @Override
    public void onPropertyApplied(Object data, Property prop, Object value) {
        onPropertyValueChanged(data, prop, null, value);
    }
    @Override
    public void onPropertyItemChanged(Object data, Property prop, Object oldItem, Object newItem, int index) {
        mCallback.onItemChanged(index, (T)oldItem, (T) newItem);
    }

    /**
     * the item manager.
     * @param <T> the item data type
     */
    public interface IItemManager<T>{

        /**
         * called on add items. should call notify in this when use in adapter.
         * @param items the items to add.
         */
        void addItems(List<T> items);

        /**
         * called on add items. should call notify in this when use in adapter.
         * @param index the start index to add
         * @param items the items to add
         */
        void addItems(int index, List<T> items);

        /**
         * called on remove items.should call notify in this when use in adapter.
         * @param items the items
         */
        void removeItems(List<T> items);

        /**
         * called on replace items.should call notify in this when use in adapter.
         * @param items the items.
         */
        void replaceItems(List<T> items);

        /**
         * called on item changed
         * @param index the index of item
         * @param oldItem the old item
         * @param newItem the new item
         */
        void onItemChanged(int index, T oldItem, T newItem);
    }

}
