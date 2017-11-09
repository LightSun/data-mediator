package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.Property;

/**
 * the property dispatcher. <br/>
 * Created by heaven7 on 2017/11/8.
 *
 * @since 1.4.1
 */
public interface PropertyDispatcher {

    /**
     * dispatch the change event of property.
     *
     * @param prop     the property which is changed.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    void dispatchValueChanged(Property prop, Object oldValue, Object newValue);

    /**
     * dispatch the apply event for target property.
     *
     * @param prop  the property
     * @param value the value of property.
     */
    void dispatchValueApplied(Property prop, Object value);
}
