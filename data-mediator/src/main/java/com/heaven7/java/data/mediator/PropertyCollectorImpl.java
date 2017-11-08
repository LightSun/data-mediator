package com.heaven7.java.data.mediator;

import java.util.LinkedList;

/**
 * the property collector.
 * @author heaven7 on 2017/11/8.
 * @since 1.4.1
 */
/*public*/ class PropertyCollectorImpl implements PropertyCollector {

    /** the property event type: change event */
    private static final byte TYPE_PROPERTY_CHANGE = 1;
    /** the property event type: apply event */
    private static final byte TYPE_PROPERTY_APPLY  = 2;
    /** indicate the collector is open */
    private static final byte STATE_OPEN    = 1;
    /** indicate the collector is closing */
    private static final byte STATE_CLOSING = 2;
    /** indicate the collector is closed */
    private static final byte STATE_CLOSED  = 3;

    private final LinkedList<PropertyEvent> mEvents = new LinkedList<>();
    private byte mState = STATE_CLOSED;

    @Override
    public boolean isOpened() {
        return mState == STATE_OPEN;
    }

    @Override
    public void open() {
        if(mState != STATE_CLOSED){
             throw new IllegalStateException("can't open collector twice.");
        }
        mState = STATE_OPEN;
    }

    @Override
    public void close(PropertyReceiver receiver) {
        if(mState != STATE_OPEN){
            throw new IllegalStateException("can't close collector twice.");
        }
        mState = STATE_CLOSING ;
        if(receiver == null){
            mEvents.clear();
        }else {
            for (PropertyEvent pe = mEvents.pollFirst(); pe != null; pe = mEvents.pollFirst()) {
                firePropertyEvent(pe, receiver);
            }
        }
        mState = STATE_CLOSED;
    }
    @Override
    public void dispatchValueChanged(Property prop, Object oldValue, Object newValue) {
        mEvents.addLast(new PropertyEvent(TYPE_PROPERTY_CHANGE, prop, oldValue, newValue));
    }
    @Override
    public void dispatchValueApplied(Property prop, Object value) {
        mEvents.addLast(new PropertyEvent(TYPE_PROPERTY_APPLY, prop, null, value));
    }

    private static void firePropertyEvent(PropertyEvent event, PropertyReceiver receiver){
        switch (event.type) {
            case TYPE_PROPERTY_CHANGE:
                receiver.dispatchValueChanged(event.prop, event.oldValue, event.newValue);
                break;
            case TYPE_PROPERTY_APPLY:
                receiver.dispatchValueApplied(event.prop, event.newValue);
                break;
            default:
                throw new UnsupportedOperationException("unknown type = " + event.type);
        }
    }
    private static class PropertyEvent {
        final byte type;
        final Property prop;
        final Object oldValue;
        final Object newValue;

        PropertyEvent(byte type, Property prop, Object oldValue, Object newValue) {
            this.type = type;
            this.prop = prop;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }
}
