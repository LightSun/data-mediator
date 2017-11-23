package com.heaven7.java.data.mediator;


/**
 * a extend class of {@linkplain SparseArrayPropertyCallback}.
 * @author heaven7
 * @since 1.4.4
 */
public abstract class SparseArrayPropertyCallback2<T> extends PropertyCallbackContext implements SparseArrayPropertyCallback<T> {

    /**
     * the {@linkplain SparseArrayPropertyCallback2} wrapped
     * @param <T> the data module type
     *  @author heaven7
     *  @since 1.4.4
     */
    static class WrappedSparseArrayPropertyCallback2<T> extends SparseArrayPropertyCallback2<T>{

        final SparseArrayPropertyCallback<? super T> callback;

        WrappedSparseArrayPropertyCallback2(SparseArrayPropertyCallback<? super T> callback) {
            this.callback = callback;
        }

        @Override
        public void onPreCallback(Params params) {
            super.onPreCallback(params);
            if(callback instanceof SparseArrayPropertyCallback2){
                ((SparseArrayPropertyCallback2) callback).onPreCallback(params);
            }
        }

        @Override
        public void onPostCallback() {
            super.onPostCallback();
            if(callback instanceof SparseArrayPropertyCallback2){
                ((SparseArrayPropertyCallback2) callback).onPostCallback();
            }
        }

        @Override
        public void onEntryValueChanged(T data, Property prop, Integer key, Object oldValue, Object newValue) {
            callback.onEntryValueChanged(data, prop, key, oldValue, newValue);
        }

        @Override
        public void onAddEntry(T data, Property prop, Integer key, Object value) {
            callback.onAddEntry(data, prop, key, value);
        }

        @Override
        public void onRemoveEntry(T data, Property prop, Integer key, Object value) {
            callback.onRemoveEntry(data, prop, key, value);
        }

        @Override
        public void onClearEntries(T data, Property prop, Object entries) {
            callback.onClearEntries(data, prop, entries);
        }

        @Override
        public void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue) {
            callback.onPropertyValueChanged(data, prop, oldValue, newValue);
        }

        @Override
        public void onPropertyApplied(T data, Property prop, Object value) {
            callback.onPropertyApplied(data, prop, value);
        }
    }

}
