package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public interface PropertyCallback<T> {

    /**
     * called on property value changed.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue);

    /**
     * called on property applied.
     * @param data the module data.
     * @param prop the property
     * @param value the applied value.
     * @since 1.0.8
     */
    void onPropertyApplied(T data, Property prop, Object value);
}
