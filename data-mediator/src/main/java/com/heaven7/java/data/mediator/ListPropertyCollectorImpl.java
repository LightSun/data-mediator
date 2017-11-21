package com.heaven7.java.data.mediator;

import com.heaven7.java.data.mediator.internal.ListPropertyDispatcher;

/*public*/ class ListPropertyCollectorImpl extends PropertyCollectorImpl implements ListPropertyDispatcher{

    @Override
    public void dispatchOnAddPropertyValues(Object data, Object original, Property prop, Object newValue, Object addedValue) {
        PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_ADD, data, original, prop, newValue, addedValue);
    }

    @Override
    public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop, Object newValue, Object addedValue, int index) {

    }

    @Override
    public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop, Object newValue, Object removeValue) {

    }

    @Override
    public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop, Object oldItem, Object newItem, int index) {

    }
}
