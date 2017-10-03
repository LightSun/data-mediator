package com.heaven7.java.data.mediator;

/**
 * Created by heaven7 on 2017/10/3.
 * @since 1.1.3
 */
/*public*/ interface SparseArrayPropertyCallback<T> {

    /**
     * called on value changed
     * @param data the module data.
     * @param prop the property
     * @param key the key in sparse array
     * @param oldVal the old value of sparse array
     * @param newVal the new value of sparse array
     * @since 1.1.3
     */
    void onMapValueChanged(T data , Property prop, int key, Object oldVal, Object newVal);

    void onAddEntry(T data , Property prop, int key, Object oldVal, Object newVal);
}
