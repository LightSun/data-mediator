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
     * @param originalSource the original source which occurs this event. may be null if on old version( <1.4.4 ) of 'Data-Mediator'
     * @param prop     the property which is changed.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    void dispatchValueChanged(Object data, Object originalSource, Property prop, Object oldValue, Object newValue);

    /**
     * dispatch the apply event for target property.
     *
     * @param prop  the property
     * @param value the value of property.
     */
    void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value);
}
