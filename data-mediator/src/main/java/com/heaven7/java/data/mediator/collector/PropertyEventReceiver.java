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
package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.data.mediator.*;

/**
 * the new property receiver which used to receive property event.
 * Created by heaven7 on 2017/11/8.
 * @since 1.4.4
 */
public abstract class PropertyEventReceiver implements PropertyDispatcher, ListPropertyDispatcher{

    /**
     * the data mediator callback delegate
     * @author heaven7
     * @since 1.4.4
     */
    public interface DataMediatorCallbackDelegate{
        DataMediatorCallback[] getCallbacks();
    }

    public static PropertyEventReceiver of(DataMediatorCallbackDelegate delegate){
         return new InternalPropertyEventReceiver(delegate);
    }

    public void dispatchValueChanged(Object data, Object originalSource, Property prop,
                                     Object oldValue, Object newValue) {
    }

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

    public MapPropertyDispatcher<Integer> getSparseArrayDispatcher(){
        return null;
    }

    /**
     * @author heaven7
     * @since 1.4.4
     */
    private static class InternalPropertyEventReceiver extends PropertyEventReceiver{

        SparseArrayPropertyDispatcher mSparseDispatcher;
        final DataMediatorCallbackDelegate delegate;

        public InternalPropertyEventReceiver(DataMediatorCallbackDelegate delegate) {
            this.delegate = delegate;
        }

        @Override  @SuppressWarnings("unchecked")
        public void dispatchValueChanged(Object data, Object originalSource, Property prop,
                                         Object oldValue, Object newValue) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(originalSource);
                callback.onPropertyValueChanged(data, prop, oldValue, newValue);
                callback.setOriginalSource(null);
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(originalSource);
                callback.onPropertyApplied(data, prop, value);
                callback.setOriginalSource(null);
            }
        }

        @Override  @SuppressWarnings("unchecked")
        public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                                Object newValue, Object addedValue){
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(original);
                callback.onAddPropertyValues(data, prop, newValue, addedValue);
                callback.setOriginalSource(null);
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                         Object newValue, Object addedValue, int index){
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(original);
                callback.onAddPropertyValuesWithIndex(data, prop, newValue, addedValue, index);
                callback.setOriginalSource(null);
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                                   Object newValue, Object removeValue){
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(original);
                callback.onRemovePropertyValues(data, prop, newValue, removeValue);
                callback.setOriginalSource(null);
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                                  Object oldItem, Object newItem, int index){
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.setOriginalSource(original);
                callback.onPropertyItemChanged(data, prop, oldItem, newItem, index);
                callback.setOriginalSource(null);
            }
        }
        @Override
        public MapPropertyDispatcher<Integer> getSparseArrayDispatcher(){
            if(mSparseDispatcher == null){
                mSparseDispatcher = new SparseArrayPropertyDispatcher(delegate);
            }
            return mSparseDispatcher;
        }
    }
    /**
     * @author heaven7
     * @since 1.4.4
     */
    private static class SparseArrayPropertyDispatcher implements MapPropertyDispatcher<Integer>{

        final DataMediatorCallbackDelegate delegate;

        SparseArrayPropertyDispatcher(DataMediatorCallbackDelegate delegate) {
            this.delegate = delegate;
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                                                Integer key, Object oldValue, Object newValue) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(original);
                    sa.onEntryValueChanged(data, prop, key, oldValue, newValue);
                    callback.setOriginalSource(null);
                }
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnAddEntry(Object data, Object original, Property prop, Integer key, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(original);
                    sa.onAddEntry(data, prop, key, value);
                    callback.setOriginalSource(null);
                }
            }
        }

        @Override  @SuppressWarnings("unchecked")
        public void dispatchOnRemoveEntry(Object data, Object original, Property prop, Integer key, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(original);
                    sa.onRemoveEntry(data, prop, key, value);
                    callback.setOriginalSource(null);
                }
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnClearEntries(Object data, Object original, Property prop, Object entries) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(original);
                    sa.onClearEntries(data, prop, entries);
                    callback.setOriginalSource(null);
                }
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchValueChanged(Object data, Object originalSource, Property prop, Object oldValue, Object newValue) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(originalSource);
                    sa.onPropertyValueChanged(data, prop, oldValue, newValue);
                    callback.setOriginalSource(null);
                }
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    callback.setOriginalSource(originalSource);
                    sa.onPropertyApplied(data, prop, value);
                    callback.setOriginalSource(null);
                }
            }
        }
    }

}
