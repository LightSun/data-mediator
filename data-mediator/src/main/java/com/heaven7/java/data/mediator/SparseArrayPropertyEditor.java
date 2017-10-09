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
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.internal.DataMediatorDelegate;
import com.heaven7.java.data.mediator.internal.SparseArrayDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * the sparse array editor
 * Created by heaven7 on 2017/10/3.
 * @param <D> the module data type
 * @param <V> the value type of sparse array.
 * @since 1.1.3
 */
public final class SparseArrayPropertyEditor<D, V> {

    private final SparseArrayDelegate<V> mMap;
    private final Property mProperty;
    private final D mData;
    private final BaseMediator<D> mMediator;

    /**
     * create list property editor.
     * @param data the module data
     * @param map the current map of property value..
     * @param property the property to edit, can be nul
     * @param mediator the mediator of module. can be null.
     */
    public SparseArrayPropertyEditor(D data, SparseArrayDelegate<V> map,
                              @Nullable Property property,
                              @Nullable BaseMediator<D> mediator) {
        Throwables.checkNull(data);
        Throwables.checkNull(map);
        this.mMap = map;
        this.mData = data;
        this.mProperty = property;
        this.mMediator = mediator;
    }

    /**
     * put the key-value to the sparse array.
     * @param key the key
     * @param value the value
     * @return this.
     */
    public SparseArrayPropertyEditor<D, V> put(int key, V value){
        int[] result = new int[1];
        final V oldVal = mMap.put(key, value, result);
        switch (result[0]){
            case SparseArrayDelegate.STATE_CHANGED:
                if(mMediator != null){
                    mMediator._getSparseArrayDispatcher().dispatchChangeEntryValue(
                            mProperty, key ,oldVal, value);
                }
                break;
            case SparseArrayDelegate.STATE_NEW:
                if(mMediator != null){
                    mMediator._getSparseArrayDispatcher().dispatchAddEntry(mProperty, key ,value);
                }
                break;

            case SparseArrayDelegate.STATE_NO_CHANGE:
                //nothing
                break;

            default:
                throw new UnsupportedOperationException();
        }
        return this;
    }

    /**
     * remove the key-value by target key.
     * @param key the key
     * @return this
     */
    public SparseArrayPropertyEditor<D, V> remove(int key){
        final V value = mMap.remove(key);
        if(value != null && mMediator != null){
            mMediator._getSparseArrayDispatcher().dispatchRemoveEntry(mProperty, key ,value);
        }
        return this;
    }

    /**
     * remove the key-value by target value.
     * @param value the value
     * @return this.
     */
    public SparseArrayPropertyEditor<D, V> removeByValue(V value){
        int[] keyArr = new int[1];
        if(mMap.removeByValue(value, keyArr)){
            if (mMediator != null) {
                mMediator._getSparseArrayDispatcher().dispatchRemoveEntry(mProperty, keyArr[0], value);
            }
        }
        return this;
    }

    /**
     * clear all key-values .
     * @return this.
     */
    public SparseArrayPropertyEditor<D, V> clear(){
        final int size = mMap.size();
        if(size == 0){
            return this;
        }
        SparseArray<V> newMap = new SparseArray<>(size * 4 /3 + 1);
        mMap.clearTo(DataMediatorDelegate.getDefault().getSparseArrayDelegate(newMap));
        if(mMediator != null){
            mMediator._getSparseArrayDispatcher().dispatchClearEntries(mProperty, newMap);
        }
        return this;
    }

    /**
     * end editor and return to the module type.
     * @return the module .
     */
    public D end(){
        if(mMediator != null){
            return (D) mMediator;
        }
        return mData;
    }
}
