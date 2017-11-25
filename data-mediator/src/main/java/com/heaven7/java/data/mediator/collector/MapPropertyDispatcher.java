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
package com.heaven7.java.data.mediator.collector;


import com.heaven7.java.data.mediator.Property;

/**
 * the sparse array dispatcher
 * @param <K> the key type of map
 * @author heaven7
 * @since 1.4.4
 */
public interface MapPropertyDispatcher<K> extends PropertyDispatcher{

    /**
     * called on value changed.
     * @param data the module data.
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key in map
     * @param oldValue the old value of entry
     * @param newValue the new value of entry
     */
    void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                                     K key, Object oldValue, Object newValue);

    /**
     * called on add entry for the map
     * @param data the module data
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     */
    void dispatchOnAddEntry(Object data, Object original, Property prop,
                            K key, Object value);
    /**
     * called on remove entry for the map
     * @param data the module data
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     */
    void dispatchOnRemoveEntry(Object data, Object original, Property prop,
                               K key, Object value);

    /**
     * called on clear all entries.
     * @param data the module data
     * @param original  the original module where occurs this event.
     * @param prop the property
     * @param entries the all entries which were removed. type is like map.
     */
    void dispatchOnClearEntries(Object data, Object original, Property prop, Object entries);
}
