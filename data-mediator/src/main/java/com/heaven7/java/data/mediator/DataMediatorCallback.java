package com.heaven7.java.data.mediator;

/**
 *  the listener of property change.
 * Created by heaven7 on 2017/9/11 0011.
 */
public abstract class DataMediatorCallback<T>{

    /**
     * called on property value changed.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    public abstract void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue);

    /**
     * called on add property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @since 1.0.8
     */
    public void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue){

    }

    /**
     * called on add property values with index.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @param index  the index to add.
     * @since 1.0.8
     */
    public void onAddPropertyValuesWithIndex(T data, Property prop,
                                             Object newValue, Object addedValue, int index){

    }

    /**
     * called on remove property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param removeValue the value which is removed.
     * @since 1.0.8
     */
    public void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue){

    }

}
