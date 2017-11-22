package com.heaven7.java.data.mediator.collector;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.data.mediator.Property;

/**
 * the collector manager. <p><h2>Note: Outside application/framework must not use this. this is used internal.</h2></p>
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


}
