package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.data.mediator.Property;

/**
 * list property collector
 * @author heaven7
 * @since 1.4.4
 */
/*public*/ class ListPropertyCollectorImpl extends PropertyCollectorImpl implements ListPropertyCollector {

    @Override
    public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                            Object newValue, Object addedValue) {
        addPropertyEvent(PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_ADD, data,
                original, prop, addedValue, newValue));
    }

    @Override
    public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                     Object newValue, Object addedValue, int index) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_ADD_INDEX,
                data, original, prop, addedValue, newValue);
        pe.setIndex(index);
        addPropertyEvent(pe);
    }

    @Override
    public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                               Object newValue, Object removeValue) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_REMOVE, data,
                original, prop, removeValue, newValue);
        addPropertyEvent(pe);
    }

    @Override
    public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                              Object oldItem, Object newItem, int index) {
        final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_SET_ITEM,
                data, original, prop, oldItem, newItem);
        pe.setIndex(index);
        addPropertyEvent(pe);
    }
}
