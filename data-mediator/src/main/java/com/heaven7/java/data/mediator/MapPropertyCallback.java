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

/**
 * the common property callback of map.
 * @param <T> the module data type
 * @param <K> the key type
 * @author heaven7 on 2017/10/3.
 * @since 1.1.3
 */
/*public*/ interface MapPropertyCallback<T,K> {

    /**
     * called on value changed.
     * @param data the module data.
     * @param prop the property
     * @param key the key in map
     * @param oldValue the old value of entry
     * @param newValue the new value of entry
     * @since 1.1.3
     */
    void onEntryValueChanged(T data , Property prop, K key, Object oldValue, Object newValue);

    /**
     * called on add entry for the map
     * @param data the module data
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void onAddEntry(T data , Property prop, K key, Object value);
    /**
     * called on remove entry for the map
     * @param data the module data
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void onRemoveEntry(T data , Property prop, K key, Object value);

    /**
     * called on clear all entries.
     * @param data the module data
     * @param prop the property
     * @param entries the all entries which were removed. type is like map.
     * @since 1.1.3
     */
    void onClearEntries(T data, Property prop, Object entries);
}
