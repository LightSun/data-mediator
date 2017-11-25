package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.data.mediator.Property;

/**
 * the property event
 * @author heaven7
 * @since 1.4.4
 */
@Hide
public final class PropertyEvent {

    /** the property event type: change event */
    public static final byte TYPE_PROPERTY_CHANGE = 1;
    /** the property event type: apply event */
    public static final byte TYPE_PROPERTY_APPLY  = 2;
    /** indicate add property values. see
     * {@linkplain ListPropertyCollector#dispatchOnAddPropertyValues(
     * Object, Object, Property, Object, Object)} */
    public static final byte TYPE_LIST_PROPERTY_ADD        = 3;
    /** indicate add property values with index. see
     * {@linkplain ListPropertyCollector#dispatchOnAddPropertyValuesWithIndex(
     * Object, Object, Property, Object, Object, int)}*/
    public static final byte TYPE_LIST_PROPERTY_ADD_INDEX  = 4;
    /** indicate remove property values. see
     * {@linkplain ListPropertyCollector#dispatchOnRemovePropertyValues(
     * Object, Object, Property, Object, Object)}
     * */
    public static final byte TYPE_LIST_PROPERTY_REMOVE     = 5;
    /** indicate set property value. see
     * {@linkplain ListPropertyCollector#dispatchOnPropertyItemChanged(
     * Object, Object, Property, Object, Object, int)}}
     * */
    public static final byte TYPE_LIST_PROPERTY_SET_ITEM   = 6;
    /**
     * indicate change an entry value of sparse array.
     * see {@linkplain MapPropertyCollector#dispatchOnEntryValueChanged(
     * Object, Object, Property, Object, Object, Object)}
     */
    public static final byte TYPE_SPARSE_PROPERTY_CHANGE   = 7;
    /**
     * indicate add an entry of sparse array
     * see {@linkplain MapPropertyCollector#dispatchOnAddEntry(
     * Object, Object, Property, Object, Object)}
     */
    public static final byte TYPE_SPARSE_PROPERTY_ADD      = 8;
    /**
     * indicate remove an entry of sparse array
     * see {@linkplain MapPropertyCollector#dispatchOnRemoveEntry(
     * Object, Object, Property, Object, Object)}
     */
    public static final byte TYPE_SPARSE_PROPERTY_REMOVE   = 9;
    /**
     * indicate clear all entries of sparse array
     * see {@linkplain MapPropertyCollector#dispatchOnClearEntries(
     * Object, Object, Property, Object)}
     */
    public static final byte TYPE_SPARSE_PROPERTY_CLEAR    = 10;

    private final byte type;
    private final Object original;
    private final Object current;
    private final Property prop;
    private final Object oldValue;
    private final Object newValue;

    private int index;
    private Object key;

    private PropertyEvent(byte type, Object current, Object original, Property prop,
                          Object oldValue, Object newValue) {
        this.current = current;
        this.original = original;
        this.type = type;
        this.prop = prop;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public static PropertyEvent of(byte type, Object current, Object original, Property prop,
                                   Object oldValue, Object newValue){
        //TODO will get from pool.
        return new PropertyEvent(type, current, original, prop, oldValue, newValue);
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void setKey(Object key) {
        this.key = key;
    }
    public void fire(PropertyEventReceiver receiver2){
        switch (type) {
            case TYPE_PROPERTY_CHANGE:
                receiver2.dispatchValueChanged(current, original, prop, oldValue, newValue);
                break;
            case TYPE_PROPERTY_APPLY:
                receiver2.dispatchValueApplied(current, original, prop, newValue);
                break;
          //list
            case TYPE_LIST_PROPERTY_ADD:
                receiver2.dispatchOnAddPropertyValues(current, original, prop, newValue, oldValue);
                break;

            case TYPE_LIST_PROPERTY_ADD_INDEX:
                receiver2.dispatchOnAddPropertyValuesWithIndex(current, original, prop, newValue, oldValue, index);
                break;

            case TYPE_LIST_PROPERTY_REMOVE:
                receiver2.dispatchOnRemovePropertyValues(current, original, prop, newValue, oldValue);
                break;

            case TYPE_LIST_PROPERTY_SET_ITEM:
                receiver2.dispatchOnPropertyItemChanged(current, original, prop, newValue, oldValue, index);
                break;
            //sparse array
            case TYPE_SPARSE_PROPERTY_CHANGE: {
                MapPropertyDispatcher<Integer> saDispatcher = receiver2.getSparseArrayDispatcher();
                if(saDispatcher != null){
                    saDispatcher.dispatchOnEntryValueChanged(current, original, prop,
                            (Integer)key, oldValue, newValue);
                }
            }
                break;

            case TYPE_SPARSE_PROPERTY_ADD: {
                MapPropertyDispatcher<Integer> saDispatcher = receiver2.getSparseArrayDispatcher();
                if(saDispatcher != null){
                    saDispatcher.dispatchOnAddEntry(current, original, prop,
                            (Integer)key, newValue);
                }
            }
            break;

            case TYPE_SPARSE_PROPERTY_REMOVE: {
                MapPropertyDispatcher<Integer> saDispatcher = receiver2.getSparseArrayDispatcher();
                if(saDispatcher != null){
                    saDispatcher.dispatchOnRemoveEntry(current, original, prop,
                            (Integer)key, newValue);
                }
            }
            break;

            case TYPE_SPARSE_PROPERTY_CLEAR: {
                MapPropertyDispatcher<Integer> saDispatcher = receiver2.getSparseArrayDispatcher();
                if(saDispatcher != null){
                    saDispatcher.dispatchOnClearEntries(current, original, prop, oldValue);
                }
            }
            break;

            default:
                throw new UnsupportedOperationException("unknown PropertyEvent type = " + type);
        }
    }
}
