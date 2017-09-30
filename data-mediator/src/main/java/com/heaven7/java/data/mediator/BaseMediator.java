/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.util.DefaultEqualsComparator;
import com.heaven7.java.data.mediator.util.EqualsComparator;
import sun.security.provider.MD2;

import java.util.ArrayList;

/**
 * the base module mediator service.
 * Created by heaven7 on 2017/9/11 0011.
 */
public class BaseMediator<T> {

    private final ArrayList<DataMediatorCallback<? super T>> mCallbacks;
    private final T mTarget;
    private EqualsComparator mEqualsComparator = DefaultEqualsComparator.getInstance();
    private DataConsumer<T> mConsumer;

    /**
     * create a base mediator by target object.
     *
     * @param target the target object which is ready to been act as agent by this.
     */
    public BaseMediator(T target) {
        if (target == null) {
            throw new NullPointerException();
        }
        this.mTarget = target;
        this.mCallbacks = new ArrayList<>();
    }

    /**
     * get the target object which is acted as agent by this object.
     *
     * @return the target object
     */
    public T getTarget() {
        return mTarget;
    }

    /**
     * get the data consumer.
     * @return the data consumer
     * @since 1.1.2
     */
    public DataConsumer<T> getDataConsumer() {
        return mConsumer;
    }

    /**
     * set the data consumer
     * @param mConsumer the data consumer
     * @since 1.1.2
     */
    public void setDataConsumer(DataConsumer<T> mConsumer) {
        this.mConsumer = mConsumer;
    }

    /**
     * get the equals comparator.
     *
     * @return the equals comparator.
     * @since 1.0.2
     */
    public EqualsComparator getEqualsComparator() {
        return mEqualsComparator;
    }

    /**
     * set the equals comparator
     *
     * @param comparator the equals comparator
     * @since 1.0.2
     */
    public void setEqualsComparator(EqualsComparator comparator) {
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.mEqualsComparator = comparator;
    }

    /**
     * add  a data mediator callback
     *
     * @param o the data mediator callback
     */
    public synchronized void addCallback(DataMediatorCallback<? super T> o) {
        if (o == null)
            throw new NullPointerException();
        if (!mCallbacks.contains(o)) {
            mCallbacks.add(o);
        }
    }

    /**
     * remove the data mediator callback
     *
     * @param o the data mediator callback
     */
    public synchronized void removeCallback(DataMediatorCallback<? super T> o) {
        mCallbacks.remove(o);
    }

    /**
     * remove all  data mediator callbacks
     */
    public synchronized void removeCallbacks() {
        mCallbacks.clear();
    }

    /**
     * count the data mediator callback.
     *
     * @return the count of the data mediator callbacks.
     */
    public synchronized int countCallbacks() {
        return mCallbacks.size();
    }

    /**
     * apply all current properties with default property interceptor.
     * that means notify all property value changed except empty(null or no element).
     *
     * @since 1.0.8
     */
    public final void applyProperties() {
        applyProperties(PropertyInterceptor.NULL);
    }

    /**
     * apply the all properties with target interceptor.
     *
     * @param interceptor the interceptor
     * @since 1.0.8
     */
    public void applyProperties(PropertyInterceptor interceptor) {
        Throwables.checkNull(interceptor);
        throw new UnsupportedOperationException("you must override this method.");
    }

    /**
     * apply the data to target consumer.
     * @param consumer the data consumer
     * @since 1.1.2
     */
    public void applyTo(DataConsumer<T> consumer){
        Throwables.checkNull(consumer);
        consumer.accept(getTarget());
    }
    /**
     * apply the data to current consumer. so you should calla {@linkplain #setDataConsumer(DataConsumer)} first.
     * @since 1.1.2
     */
    public void applyTo(){
        applyTo(mConsumer);
    }

//=========================================================================

    /**
     * dispatch the change event to the callbacks.
     * <p>use {@linkplain #dispatchValueChanged(Property, Object, Object)} instead</p>
     *
     * @param prop     the property which is changed.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    @Deprecated
    public void dispatchCallbacks(Property prop, Object oldValue, Object newValue) {
        dispatchValueChanged(prop, oldValue, newValue);
    }

    /**
     * dispatch the change event of property.
     *
     * @param prop     the property which is changed.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     * @since 1.0.8
     */
    public void dispatchValueChanged(Property prop, Object oldValue, Object newValue) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onPropertyValueChanged(mTarget, prop, oldValue, newValue);
        }
    }

    /**
     * dispatch the apply event for target property.
     *
     * @param prop  the property
     * @param value the value of property.
     * @since 1.0.8
     */
    public void dispatchValueApplied(Property prop, Object value) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onPropertyApplied(mTarget, prop, value);
        }
    }

    /**
     * dispatch the add event of property.
     *
     * @param prop       the property which is changed.
     * @param newValue   the newest value of property
     * @param addedValue the values which is added.
     * @since 1.0.8
     */
    public void dispatchAddValues(Property prop, Object newValue, Object addedValue) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onAddPropertyValues(mTarget, prop, newValue, addedValue);
        }
    }

    /**
     * dispatch the add event with index of property.
     *
     * @param prop     the property which is changed.
     * @param newValue the newest value of property
     * @param addValue the values which is added.
     * @param index    the index to add.
     * @since 1.0.8
     */
    public void dispatchAddValuesWithIndex(Property prop, Object newValue, Object addValue, int index) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onAddPropertyValuesWithIndex(mTarget, prop,
                    newValue, addValue, index);
        }
    }

    /**
     * dispatch the remove event of property.
     *
     * @param prop         the property which is changed.
     * @param newValue     the newest value of property
     * @param removeValues the values which is removed..
     * @since 1.0.8
     */
    public void dispatchRemoveValues(Property prop, Object newValue, Object removeValues) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onRemovePropertyValues(mTarget, prop, newValue, removeValues);
        }
    }

    /**
     * dispatch item changed which is changed between old item and new item on target index.
     * @param prop the property of list items
     * @param oldItem the old item
     * @param newItem the new item
     * @param index the index.
     * @since 1.1.2
     */
    public void dispatchItemChanged(Property prop, Object oldItem, Object newItem , int index) {
        final DataMediatorCallback[] arrLocal = getCallbacks();
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            arrLocal[i].onPropertyItemChanged(mTarget, prop, oldItem, newItem, index);
        }
    }

    /**
     * get callbacks/
     *
     * @return the callbacks.
     */
    protected DataMediatorCallback[] getCallbacks() {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        DataMediatorCallback[] arrLocal;

        synchronized (this) {
            arrLocal = mCallbacks.toArray(new DataMediatorCallback[mCallbacks.size()]);
        }
        return arrLocal;
    }
    /**
     * start batch applier.
     * @param interceptor the property  interceptor
     * @return the batch applier.
     * @since 1.0.8
     */
    protected BatchApplier<T> startBatchApply(PropertyInterceptor interceptor) {
        return new BatchApplier<>(this, interceptor);
    }
    /**
     * the batch applier.
     * @param <T> the module data type
     * @since 1.0.8
     */
    public static class BatchApplier<T> {
        private final ArrayList<Property> mProps;
        private final ArrayList<Object> mValue;
        private final PropertyInterceptor mInterceptor;
        private final BaseMediator<T> mMediator;

        /*public*/ BatchApplier(BaseMediator<T> mediator, PropertyInterceptor interceptor) {
            this.mMediator = mediator;
            this.mInterceptor = interceptor;
            mProps = new ArrayList<>();
            mValue = new ArrayList<>();
        }

        /**
         * add property with it's value.
         * @param prop the property
         * @param value the value of property
         * @return this.
         */
        public BatchApplier addProperty(Property prop, Object value) {
            Throwables.checkNull(prop);
            if(!mInterceptor.shouldIntercept(mMediator.getTarget(), prop, value)) {
                this.mProps.add(prop);
                this.mValue.add(value);
            }
            return this;
        }

        /**
         * batch apply the properties with theirs' value.
         */
        public void apply() {
            final T data = mMediator.getTarget();
            final DataMediatorCallback[] arrLocal = mMediator.getCallbacks();
            final int size = mProps.size();

            DataMediatorCallback callback;
            for (int i = arrLocal.length - 1; i >= 0; i--) {
                callback = arrLocal[i];
                for (int j = 0; j < size; j++) {
                    callback.onPropertyApplied(data, mProps.get(j), mValue.get(j));
                }
            }
        }
    }
}
