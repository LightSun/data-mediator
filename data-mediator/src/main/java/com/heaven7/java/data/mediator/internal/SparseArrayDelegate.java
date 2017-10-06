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
package com.heaven7.java.data.mediator.internal;

/**
 * the sparse array delegate .
 * @param <V> the value type
 * @since 1.1.3
 */
public interface SparseArrayDelegate<V> {

    /**
     * indicate that the entry is no-change.
     */
    int STATE_NO_CHANGE = 0;
    /**
     * indicate that the entry is changed.
     */
    int STATE_CHANGED   = 1;
    /**
     * indicate that the entry is a new item. that means previous has no mapping.
     */
    int STATE_NEW       = 2;

    /**
     * put the key-value to the sparse array
     * @param key the key of type.
     * @param value the value
     * @return the state of put result.
     */
    int put(int key, V value);

    /**
     * remove the key-value by target key.
     * @param key the key
     * @return the value of the key
     */
    V remove(int key);

    /**
     * remove by value and return the old key
     * @param value the value
     * @return the key of target value
     */
    int removeByValue(V value);

    /**
     * get the size of sparse array
     * @return the size
     */
    int size();

    /**
     * get the key of target index
     * @param index the index
     * @return the key of target index
     */
    int keyAt(int index);
    /**
     * get the value of target index
     * @param index the index
     * @return the value of target index
     */
    V valueAt(int index);

    /**
     * remove at target index
     * @param index the index
     */
    void removeAt(int index);

    /**
     * set the value at target index
     * @param index the index
     * @param value the new value.
     */
    void setValueAt(int index, V value);

    /**
     * drain to target and clear.
     * @param out can be null.
     * @return  true if changed.
     */
    boolean clearTo(SparseArrayDelegate<V> out);

    /**
     * get the index of target value
     * @param value the value
     * @return the index
     */
    int indexOfValue(V value);

    /**
     * get the value by key
     * @param key the key
     * @return the value.
     */
    V get(int key);
}
