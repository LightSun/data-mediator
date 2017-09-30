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
 * a callback of simple property. if you use list property please use {@linkplain ListPropertyCallback}.
 * Created by heaven7 on 2017/9/23.
 * @since 1.0.8
 * @see ListPropertyCallback
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
