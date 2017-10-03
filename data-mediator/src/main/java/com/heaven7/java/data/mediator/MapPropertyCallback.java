package com.heaven7.java.data.mediator;

/**
 * the common property callback of map.
 * @param <T> the module data type
 * @param <K> the key type
 * @author heaven7 on 2017/10/3.
 * @since 1.1.3
 */
public interface MapPropertyCallback<T,K> {

    /**
     * called on value changed.
     * @param data the module data.
     * @param prop the property
     * @param key the key in map
     * @param oldValue the old value of entry
     * @param newValue the new value of entry
     * @since 1.1.3
     */
    void onEntryValueChanged(T data , Property prop, K key, Object oldValue, Object newValue);

    /**
     * called on add entry for the map
     * @param data the module data
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void onAddEntry(T data , Property prop, K key, Object value);
    /**
     * called on remove entry for the map
     * @param data the module data
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void onRemoveEntry(T data , Property prop, K key, Object value);
}
