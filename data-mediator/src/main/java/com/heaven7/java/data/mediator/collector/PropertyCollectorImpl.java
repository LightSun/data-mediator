package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.data.mediator.Property;
import java.util.LinkedList;

import static com.heaven7.java.data.mediator.collector.PropertyEvent.TYPE_PROPERTY_APPLY;
import static com.heaven7.java.data.mediator.collector.PropertyEvent.TYPE_PROPERTY_CHANGE;

/**
 * the property collector.
 * @author heaven7 on 2017/11/8.
 * @since 1.4.1
 */
/*public*/ class PropertyCollectorImpl implements PropertyCollector {

    /** indicate the collector is open */
    private static final byte STATE_OPEN    = 1;
    /** indicate the collector is closing */
    private static final byte STATE_CLOSING = 2;
    /** indicate the collector is closed */
    private static final byte STATE_CLOSED  = 3;

    private final LinkedList<PropertyEvent> mEvents = new LinkedList<>();
    private byte mState = STATE_CLOSED;

    protected void addPropertyEvent(PropertyEvent event){
        mEvents.addLast(event);
    }

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
    public void close(PropertyEventReceiver receiver) {
        if(mState != STATE_OPEN){
            throw new IllegalStateException("can't close collector twice.");
        }
        mState = STATE_CLOSING ;
        if(receiver == null){
            mEvents.clear();
        }else {
            for (PropertyEvent pe = mEvents.pollFirst(); pe != null; pe = mEvents.pollFirst()) {
                pe.fire(receiver);
            }
        }
        mState = STATE_CLOSED;
    }
    @Override
    public void dispatchValueChanged(Object current, Object source, Property prop, Object oldValue, Object newValue) {
        mEvents.addLast(PropertyEvent.of(TYPE_PROPERTY_CHANGE, current, source, prop, oldValue, newValue));
    }
    @Override
    public void dispatchValueApplied(Object current, Object source, Property prop, Object value) {
        mEvents.addLast(PropertyEvent.of(TYPE_PROPERTY_APPLY, current, source, prop, null, value));
    }

}
