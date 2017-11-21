package com.heaven7.java.data.mediator;

/**
 * the property event
 * @author heaven7
 * @since 1.4.4
 */
/*public*/ class PropertyEvent {

    /** the property event type: change event */
    public static final byte TYPE_PROPERTY_CHANGE = 1;
    /** the property event type: apply event */
    public static final byte TYPE_PROPERTY_APPLY  = 2;
    /** indicate add property values. see
     * {@linkplain com.heaven7.java.data.mediator.internal.ListPropertyDispatcher#dispatchOnAddPropertyValues(
     * Object, Object, Property, Object, Object)} */
    public static final byte TYPE_LIST_PROPERTY_ADD        = 3;
    /** indicate add property values with index. see
     * {@linkplain com.heaven7.java.data.mediator.internal.ListPropertyDispatcher#dispatchOnAddPropertyValuesWithIndex(
     * Object, Object, Property, Object, Object, int)}*/
    public static final byte TYPE_LIST_PROPERTY_ADD_INDEX  = 4;
    /** indicate remove property values. see
     * {@linkplain com.heaven7.java.data.mediator.internal.ListPropertyDispatcher#dispatchOnRemovePropertyValues(Object, Object, Property, Object, Object)}
     * */
    public static final byte TYPE_LIST_PROPERTY_REMOVE     = 5;
    /** indicate set property value. see
     * {@linkplain com.heaven7.java.data.mediator.internal.ListPropertyDispatcher#dispatchOnPropertyItemChanged(Object, Object, Property, Object, Object, int)}}
     * */
    public static final byte TYPE_LIST_PROPERTY_SET_ITEM   = 6;

    final byte type;
    final Object original;
    final Object current;
    final Property prop;
    final Object oldValue;
    final Object newValue;

    private int index;

    private PropertyEvent(byte type,Object current, Object original, Property prop, Object oldValue, Object newValue) {
        this.current = current;
        this.original = original;
        this.type = type;
        this.prop = prop;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public static PropertyEvent of(byte type,Object current, Object original, Property prop, Object oldValue, Object newValue){
        return new PropertyEvent(type, current, original, prop, oldValue, newValue);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void fire(PropertyReceiver2 receiver2){
        switch (type) {
            case TYPE_PROPERTY_CHANGE:
                receiver2.dispatchValueChanged(current, original, prop, oldValue, newValue);
                break;
            case TYPE_PROPERTY_APPLY:
                receiver2.dispatchValueApplied(current, original, prop, newValue);
                break;

            case TYPE_LIST_PROPERTY_ADD:
                receiver2.dispatchOnAddPropertyValues(current, original, prop, oldValue, newValue);
                break;

            case TYPE_LIST_PROPERTY_ADD_INDEX:
                receiver2.dispatchOnAddPropertyValuesWithIndex(current, original, prop, oldValue, newValue, index);
                break;

            case TYPE_LIST_PROPERTY_REMOVE:
                receiver2.dispatchOnRemovePropertyValues(current, original, prop, newValue, oldValue);
                break;

            case TYPE_LIST_PROPERTY_SET_ITEM:
                receiver2.dispatchOnPropertyItemChanged(current, original, prop, newValue, oldValue, index);
                break;

            default:
                throw new UnsupportedOperationException("unknown type = " + type);
        }
    }

}
