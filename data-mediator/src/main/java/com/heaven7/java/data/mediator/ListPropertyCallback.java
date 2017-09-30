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
