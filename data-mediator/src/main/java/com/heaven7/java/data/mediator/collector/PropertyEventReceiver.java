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
public abstract class PropertyEventReceiver extends PropertyCallbackContext
        implements PropertyDispatcher, ListPropertyDispatcher{

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
            final DataMediatorCallback[] callbacks = delegate.getCallbacks();
            final Params params = getParams();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onPropertyValueChanged(data, prop, oldValue, newValue);
                callback.onPostCallback();
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
            final Params params = getParams();
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onPropertyApplied(data, prop, value);
                callback.onPostCallback();
            }
        }

        @Override  @SuppressWarnings("unchecked")
        public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                                Object newValue, Object addedValue){
            final Params params = getParams();
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onAddPropertyValues(data, prop, newValue, addedValue);
                callback.onPostCallback();
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                         Object newValue, Object addedValue, int index){
            final Params params = getParams();
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onAddPropertyValuesWithIndex(data, prop, newValue, addedValue, index);
                callback.onPostCallback();
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                                   Object newValue, Object removeValue){
            final Params params = getParams();
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onRemovePropertyValues(data, prop, newValue, removeValue);
                callback.onPostCallback();
            }
        }
        @Override @SuppressWarnings("unchecked")
        public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                                  Object oldItem, Object newItem, int index){
            final Params params = getParams();
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                callback.onPropertyItemChanged(data, prop, oldItem, newItem, index);
                callback.onPostCallback();
            }
        }
        @Override
        public MapPropertyDispatcher<Integer> getSparseArrayDispatcher(){
            if(mSparseDispatcher == null){
                mSparseDispatcher = new SparseArrayPropertyDispatcher(delegate, getParams());
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
        final Params params;

        public SparseArrayPropertyDispatcher(DataMediatorCallbackDelegate delegate, Params params) {
            this.delegate = delegate;
            this.params = params;
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                                                Integer key, Object oldValue, Object newValue) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    sa.onEntryValueChanged(data, prop, key, oldValue, newValue);
                }
                callback.onPostCallback();
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnAddEntry(Object data, Object original, Property prop, Integer key, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null) {
                    sa.onAddEntry(data, prop, key, value);
                }
                callback.onPostCallback();
            }
        }

        @Override  @SuppressWarnings("unchecked")
        public void dispatchOnRemoveEntry(Object data, Object original, Property prop, Integer key, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    sa.onRemoveEntry(data, prop, key, value);
                }
                callback.onPostCallback();
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchOnClearEntries(Object data, Object original, Property prop, Object entries) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    sa.onClearEntries(data, prop, entries);
                }
                callback.onPostCallback();
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchValueChanged(Object data, Object original, Property prop, Object oldValue, Object newValue) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    sa.onPropertyValueChanged(data, prop, oldValue, newValue);
                }
                callback.onPostCallback();
            }
        }

        @Override @SuppressWarnings("unchecked")
        public void dispatchValueApplied(Object data, Object original, Property prop, Object value) {
            DataMediatorCallback[] callbacks = delegate.getCallbacks();
            for(DataMediatorCallback callback : callbacks){
                callback.onPreCallback(params);
                SparseArrayPropertyCallback sa = callback.getSparseArrayPropertyCallback();
                if(sa != null){
                    sa.onPropertyApplied(data, prop, value);
                }
                callback.onPostCallback();
            }
        }
    }

}
