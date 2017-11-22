package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.data.mediator.Property;

/**
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
     * get sparse array collector
     * @return the sparse array collector
     */
    SparseArrayPropertyCollector getSparseArrayCollector();

    boolean isSparseArrayCollectorEnabled();

    void close(PropertyEventReceiver receiver2);

    void open(int flags);

    boolean dispatchValueChanged(Object data, Object originalSource, Property prop,
                                 Object oldValue, Object newValue);

    boolean dispatchValueApplied(Object data, Object originalSource, Property prop, Object value);

    boolean dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                        Object newValue, Object addedValue);

    boolean dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                 Object newValue, Object addedValue, int index);

    boolean dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                           Object newValue, Object removeValue);

    boolean dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                          Object oldItem, Object newItem, int index);
}
