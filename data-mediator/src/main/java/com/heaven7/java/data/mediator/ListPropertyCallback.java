package com.heaven7.java.data.mediator;

/**
 * the list property callback.
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 */
public interface ListPropertyCallback<T> extends PropertyCallback<T>{

    /**
     * called on add property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @since 1.0.8
     */
    void onAddPropertyValues(T data, Property prop, Object newValue, Object addedValue);

    /**
     * called on add property values with index.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param addedValue the value which is added.
     * @param index  the index to add.
     * @since 1.0.8
     */
     void onAddPropertyValuesWithIndex(T data, Property prop,
                                             Object newValue, Object addedValue, int index);

    /**
     * called on remove property values.
     * @param data  the target module data .
     * @param prop  the property to describe the field.
     * @param newValue the newest value of property
     * @param removeValue the value which is removed.
     * @since 1.0.8
     */
    void onRemovePropertyValues(T data, Property prop, Object newValue, Object removeValue);


    /**
     * called on property item changed.
     * @param data the data
     * @param prop the property
     * @param oldItem the old item
     * @param newItem the new item
     * @param index the index of old item
     * @since 1.1.2
     */
    void onPropertyItemChanged(T  data, Property prop, Object oldItem, Object newItem, int index);


}
