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
 * the collector manager. which owns the all collectors. like {@linkplain PropertyCollector},
 * {@linkplain ListPropertyCollector}, {@linkplain SparseArrayPropertyCollector}.
 * @author heaven7
 * @since 1.4.4
 */
public interface CollectorManager {
    /**
     * collector flags: simple
     */
    int FLAG_SIMPLE = 0x0001;
    /**
     * collector flags: list
     */
    int FLAG_LIST = 0x0002;
    /**
     * collector flags: sparse array
     */
    int FLAG_SPARSE_ARRAY = 0x0004;
    /**
     * the flags indicate collect all.
     */
    int FLAGS_ALL = FLAG_SIMPLE | FLAG_LIST | FLAG_SPARSE_ARRAY;

    /**
     * get sparse array collector, or null if it is not enabled, see .
     * @return the sparse array collector
     * @see #isSparseArrayCollectorEnabled()
     */
    SparseArrayPropertyCollector getSparseArrayCollector();

    /**
     * is the sparse array collector enabled or not.
     * @return true if it is enabled.
     */
    boolean isSparseArrayCollectorEnabled();

    /**
     * close the collector manager and make target receiver receive the {@linkplain PropertyEvent}.
     * @param receiver the property event receiver. can be null.
     */
    void close(PropertyEventReceiver receiver);

    /**
     * open the collector manager with flags.
     * @param flags the flags. like {@linkplain #FLAG_SIMPLE},
     *      {@linkplain #FLAG_LIST}, {@linkplain #FLAG_SPARSE_ARRAY}
     */
    void open(int flags);

    /**
     * dispatch value changed event.
     * @param data the data
     * @param originalSource the original source
     * @param prop the property
     * @param oldValue the old value
     * @param newValue the new value
     * @return true if dispatch success. false otherwise.
     */
    boolean dispatchValueChanged(Object data, Object originalSource, Property prop,
                                 Object oldValue, Object newValue);
    /**
     * dispatch value apply event.
     * @param data the data
     * @param originalSource the original source
     * @param prop the property
     * @param value the old value
     * @return true if dispatch success. false otherwise.
     */
    boolean dispatchValueApplied(Object data, Object originalSource, Property prop, Object value);

    /**
     * dispatch add values event for list property.
     * @param data the data
     * @param original the original source
     * @param prop the property
     * @param newValue the new value of property
     * @param addedValue the added value
     * @return true if dispatch success. false otherwise.
     * @see com.heaven7.java.data.mediator.ListPropertyCallback
     */
    boolean dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                        Object newValue, Object addedValue);
    /**
     * dispatch add values event for list property.
     * @param data the data
     * @param original the original source
     * @param prop the property
     * @param newValue the new value of property
     * @param addedValue the added value
     * @param index the index of list
     * @return true if dispatch success. false otherwise.
     * @see com.heaven7.java.data.mediator.ListPropertyCallback
     */
    boolean dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                 Object newValue, Object addedValue, int index);
    /**
     * dispatch remove values event for list property.
     * @param data the data
     * @param original the original source
     * @param prop the property
     * @param newValue the new value of property
     * @param removeValue the removed values
     * @return true if dispatch success. false otherwise.
     * @see com.heaven7.java.data.mediator.ListPropertyCallback
     */
    boolean dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                           Object newValue, Object removeValue);
    /**
     * dispatch change item event for list property.
     * @param data the data
     * @param original the original source
     * @param prop the property
     * @param oldItem the old item of list
     * @param newItem the new item of list
     * @param index the index of list
     * @return true if dispatch success. false otherwise.
     * @see com.heaven7.java.data.mediator.ListPropertyCallback
     */
    boolean dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                          Object oldItem, Object newItem, int index);
}
