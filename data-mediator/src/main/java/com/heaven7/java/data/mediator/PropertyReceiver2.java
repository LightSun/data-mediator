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

import com.heaven7.java.data.mediator.internal.MapPropertyCollector;
import com.heaven7.java.data.mediator.internal.PropertyDispatcher;

/**
 * the new property receiver which used to receive property event.
 * Recommend you use this class instead. shouldn't use {@linkplain PropertyReceiver} it will be removed in 2.x.
 * Created by heaven7 on 2017/11/8.
 * @since 1.4.4
 */
public abstract class PropertyReceiver2 implements PropertyDispatcher{

    static PropertyReceiver2 from(final PropertyReceiver receiver){
        return new PropertyReceiver2() {
            @Override
            public void dispatchValueChanged(Object data, Object originalSource, Property prop, Object oldValue, Object newValue) {
                receiver.dispatchValueChanged(prop, oldValue, newValue);
            }
            @Override
            public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
                receiver.dispatchValueApplied(prop, value);
            }
        };
    }
    @Override
    public void dispatchValueChanged(Object data, Object originalSource, Property prop,
                                     Object oldValue, Object newValue) {
    }

    @Override
    public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {

    }

    public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                            Object newValue, Object addedValue){

    }

    public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                      Object newValue, Object addedValue, int index){

    }

    public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                               Object newValue, Object removeValue){

    }

    public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                              Object oldItem, Object newItem, int index){

    }

    public MapPropertyCollector<Integer> getSparseArrayDispatcher(){
        return null;
    }

}
