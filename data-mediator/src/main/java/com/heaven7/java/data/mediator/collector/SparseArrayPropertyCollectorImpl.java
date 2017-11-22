package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.data.mediator.Property;

/**
 * Created by heaven7 on 2017/11/21.
 * @since 1.4.4
 */
/*public*/ class SparseArrayPropertyCollectorImpl extends PropertyCollectorImpl
        implements SparseArrayPropertyCollector {
    @Override
    public void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                                    Integer key, Object oldValue, Object newValue) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_CHANGE, data, original,
                prop, oldValue, newValue);
        pe.setKey(key);
        addPropertyEvent(pe);
    }

    @Override
    public void dispatchOnAddEntry(Object data, Object original, Property prop,
                           Integer key, Object value) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_ADD, data, original,
                prop, null, value);
        pe.setKey(key);
        addPropertyEvent(pe);
    }

    @Override
    public void dispatchOnRemoveEntry(Object data, Object original, Property prop,
                              Integer key, Object value) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_REMOVE, data, original,
                prop, null, value);
        pe.setKey(key);
        addPropertyEvent(pe);
    }

    @Override
    public void dispatchOnClearEntries(Object data, Object original, Property prop,
                               Object entries) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_CLEAR, data, original,
                prop, entries, null);
        addPropertyEvent(pe);
    }
}
