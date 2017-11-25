package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.collector.*;

import java.util.LinkedList;

import static com.heaven7.java.data.mediator.collector.PropertyEvent.TYPE_PROPERTY_APPLY;
import static com.heaven7.java.data.mediator.collector.PropertyEvent.TYPE_PROPERTY_CHANGE;

/**
 * the collector manager.
 *
 * @author heaven7
 * @since 1.4.4
 */
@Hide
public final class CollectorManagerImpl implements CollectorManager {

    private PropertyCollector mSimpleCollector;
    private ListPropertyCollector mListCollector;
    private SparseArrayPropertyCollector mSparseCollector;

    @Override
    public SparseArrayPropertyCollector getSparseArrayCollector() {
        return mSparseCollector;
    }
    @Override
    public boolean isSparseArrayCollectorEnabled(){
        return mSparseCollector != null && mSparseCollector.isOpened();
    }

    @Override
    public void close(PropertyEventReceiver receiver2) {
        if (mSimpleCollector != null && mSimpleCollector.isOpened()) {
            mSimpleCollector.close(receiver2);
        }
        if (mListCollector != null && mListCollector.isOpened()) {
            mListCollector.close(receiver2);
        }
        if (mSparseCollector != null && mSparseCollector.isOpened()) {
            mSparseCollector.close(receiver2);
        }
    }

    @Override
    public void open(int flags) {
        boolean opened = false;
        if ((flags & FLAG_SIMPLE) == FLAG_SIMPLE) {
            if (mSimpleCollector == null) {
                mSimpleCollector = new PropertyCollectorImpl();
            }
            mSimpleCollector.open();
            opened = true;
        }
        if ((flags & FLAG_LIST) == FLAG_LIST) {
            if (mListCollector == null) {
                mListCollector = new ListPropertyCollectorImpl();
            }
            mListCollector.open();
            opened = true;
        }
        if ((flags & FLAG_SPARSE_ARRAY) == FLAG_SPARSE_ARRAY) {
            if (mSparseCollector == null) {
                mSparseCollector = new SparseArrayPropertyCollectorImpl();
            }
            mSparseCollector.open();
            opened = true;
        }
        if (!opened) {
            throw new IllegalArgumentException("unknown flags = " + flags);
        }
    }
    //===========================================================================

    @Override
    public boolean dispatchValueChanged(Object data, Object originalSource, Property prop,
                                        Object oldValue, Object newValue) {
        if(mSimpleCollector != null && mSimpleCollector.isOpened()) {
            mSimpleCollector.dispatchValueChanged(data, originalSource, prop, oldValue, newValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
        if(mSimpleCollector != null && mSimpleCollector.isOpened()) {
            mSimpleCollector.dispatchValueApplied(data, originalSource, prop, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                               Object newValue, Object addedValue) {
        if(mListCollector != null && mListCollector.isOpened()) {
            mListCollector.dispatchOnAddPropertyValues(data, original, prop, newValue, addedValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                        Object newValue, Object addedValue, int index) {
        if(mListCollector != null && mListCollector.isOpened()) {
            mListCollector.dispatchOnAddPropertyValuesWithIndex(data, original, prop, newValue, addedValue, index);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                                  Object newValue, Object removeValue) {
        if(mListCollector != null && mListCollector.isOpened()) {
            mListCollector.dispatchOnRemovePropertyValues(data, original, prop, newValue, removeValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                                 Object oldItem, Object newItem, int index) {
        if(mListCollector != null && mListCollector.isOpened()) {
            mListCollector.dispatchOnPropertyItemChanged(data, original, prop, oldItem, newItem, index);
            return true;
        }
        return false;
    }

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

    /**
     * list property collector
     * @author heaven7
     * @since 1.4.4
     */
/*public*/ class ListPropertyCollectorImpl extends PropertyCollectorImpl implements ListPropertyCollector {

        @Override
        public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                                Object newValue, Object addedValue) {
            addPropertyEvent(PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_ADD, data,
                    original, prop, addedValue, newValue));
        }

        @Override
        public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                         Object newValue, Object addedValue, int index) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_ADD_INDEX,
                    data, original, prop, addedValue, newValue);
            pe.setIndex(index);
            addPropertyEvent(pe);
        }

        @Override
        public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                                   Object newValue, Object removeValue) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_REMOVE, data,
                    original, prop, removeValue, newValue);
            addPropertyEvent(pe);
        }

        @Override
        public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                                  Object oldItem, Object newItem, int index) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_LIST_PROPERTY_SET_ITEM,
                    data, original, prop, oldItem, newItem);
            pe.setIndex(index);
            addPropertyEvent(pe);
        }
    }


    /**
     * Created by heaven7 on 2017/11/21.
     * @since 1.4.4
     */
/*public*/ class SparseArrayPropertyCollectorImpl extends PropertyCollectorImpl
            implements SparseArrayPropertyCollector {
        @Override
        public void dispatchOnEntryValueChanged(Object data, Object original, Property prop,
                                                Integer key, Object oldValue, Object newValue) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_CHANGE, data, original,
                    prop, oldValue, newValue);
            pe.setKey(key);
            addPropertyEvent(pe);
        }

        @Override
        public void dispatchOnAddEntry(Object data, Object original, Property prop,
                                       Integer key, Object value) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_ADD, data, original,
                    prop, null, value);
            pe.setKey(key);
            addPropertyEvent(pe);
        }

        @Override
        public void dispatchOnRemoveEntry(Object data, Object original, Property prop,
                                          Integer key, Object value) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_REMOVE, data, original,
                    prop, null, value);
            pe.setKey(key);
            addPropertyEvent(pe);
        }

        @Override
        public void dispatchOnClearEntries(Object data, Object original, Property prop,
                                           Object entries) {
            final PropertyEvent pe = PropertyEvent.of(PropertyEvent.TYPE_SPARSE_PROPERTY_CLEAR, data, original,
                    prop, entries, null);
            addPropertyEvent(pe);
        }
    }

}
