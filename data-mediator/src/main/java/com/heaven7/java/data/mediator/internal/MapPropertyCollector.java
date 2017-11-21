package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.Property;

/**
 * the sparse array property dispatcher.
 * @author heaven7
 * @since 1.4.4
 */
public interface MapPropertyCollector<K> extends PropertyCollector{

    /**
     * called on value changed.
     * @param data the module data.
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key in map
     * @param oldValue the old value of entry
     * @param newValue the new value of entry
     * @since 1.1.3
     */
    void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                             K key, Object oldValue, Object newValue);

    /**
     * called on add entry for the map
     * @param data the module data
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void dispatchOnAddEntry(Object data, Object original, Property prop,
                    K key, Object value);
    /**
     * called on remove entry for the map
     * @param data the module data
     * @param original   the original module where occurs this event.
     * @param prop the property
     * @param key the key of entry
     * @param value the value of entry
     * @since 1.1.3
     */
    void dispatchOnRemoveEntry(Object data, Object original,Property prop,
                       K key, Object value);

    /**
     * called on clear all entries.
     * @param data the module data
     * @param original  the original module where occurs this event.
     * @param prop the property
     * @param entries the all entries which were removed. type is like map.
     * @since 1.1.3
     */
    void dispatchOnClearEntries(Object data, Object original, Property prop, Object entries);

}
