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

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.collector.*;
import com.heaven7.java.data.mediator.util.DefaultEqualsComparator;
import com.heaven7.java.data.mediator.util.EqualsComparator;

import java.util.ArrayList;

/**
 * the base module mediator service. note the property of sub class.
 * all proxy class should extend this class.
 * Created by heaven7 on 2017/9/11 0011.
 */
public class BaseMediator<T>{

    private final ArrayList<DataMediatorCallback<? super T>> _mCallbacks;
    private final T _mTarget;
    private EqualsComparator _mEqualsComparator = DefaultEqualsComparator.getInstance();
    private DataConsumer<? super T> _mConsumer;
    private PropertyInterceptor _mInterceptor = PropertyInterceptor.NULL;
    //temps
    private SparseArrayDispatcher _mSparseArrayDispatcher;
    private CollectorManager _mCollector;

    /**
     * the event transit station
     */
    private final PropertyEventReceiver _mDispatcher = PropertyEventReceiver.of(
            new PropertyEventReceiver.DataMediatorCallbackDelegate() {
        @Override
        public DataMediatorCallback[] getCallbacks() {
            return _getCallbacks();
        }
    });

    /**
     * create a base mediator by target object. called often by framework.
     *
     * @param target the target object which is ready to been act as agent by this.
     */
    public BaseMediator(T target) {
        if (target == null) {
            throw new NullPointerException();
        }
        this._mTarget = target;
        this._mCallbacks = new ArrayList<>();
    }

    /**
     * get the target object which is acted as agent by this object.
     *
     * @return the target object
     */
    protected T _getTarget() { //make _ to avoid property conflict
        return _mTarget;
    }

    /**
     * get the data consumer.
     * @return the data consumer
     * @since 1.1.2
     */
    protected DataConsumer<? super T> _getDataConsumer() {
        return _mConsumer;
    }

    /**
     * set the data consumer
     * @param mConsumer the data consumer
     * @since 1.1.2
     */
    protected void _setDataConsumer(DataConsumer<? super T> mConsumer) {
        this._mConsumer = mConsumer;
    }

    /**
     * get the equals comparator.
     *
     * @return the equals comparator.
     * @since 1.0.2
     */
    protected EqualsComparator _getEqualsComparator() {
        return _mEqualsComparator;
    }

    /**
     * set the equals comparator
     *
     * @param comparator the equals comparator
     * @since 1.0.2
     */
    protected void _setEqualsComparator(EqualsComparator comparator) {
        Throwables.checkNull(comparator);
        this._mEqualsComparator = comparator;
    }

    /**
     * get the property interceptor , default is {@linkplain PropertyInterceptor#NULL}
     * @return the property interceptor.
     * @since 1.1.3
     */
    protected PropertyInterceptor _getPropertyInterceptor() {
        return _mInterceptor;
    }

    /**
     * set the property interceptor . default is {@linkplain PropertyInterceptor#NULL}
     * @param interceptor the target property interceptor.
     * @since 1.1.3
     */
    protected void _setPropertyInterceptor(PropertyInterceptor interceptor) {
        Throwables.checkNull(interceptor);
        this._mInterceptor = interceptor;
    }

    /**
     * add  a data mediator callback
     *
     * @param o the data mediator callback
     */
    public synchronized void addCallback(DataMediatorCallback<? super T> o) {
        if (o == null)
            throw new NullPointerException();
        if (!_mCallbacks.contains(o)) {
            _mCallbacks.add(o);
        }
    }

    /**
     * remove the data mediator callback
     *
     * @param o the data mediator callback
     */
    public synchronized void removeCallback(DataMediatorCallback<? super T> o) {
        _mCallbacks.remove(o);
    }

    /**
     * remove all  data mediator callbacks
     */
    public synchronized void removeCallbacks() {
        _mCallbacks.clear();
    }

    /**
     * count the data mediator callback.
     *
     * @return the count of the data mediator callbacks.
     */
    public synchronized int countCallbacks() {
        return _mCallbacks.size();
    }

    /**
     * apply all current properties with current property interceptor.
     * And the default interceptor is {@linkplain PropertyInterceptor#NULL}
     *
     * @since 1.0.8
     */
    public final void applyProperties() {
        applyProperties(_mInterceptor);
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
    public void applyTo(DataConsumer<? super T> consumer){
        Throwables.checkNull(consumer);
        consumer.accept(_getTarget());
    }
    /**
     * apply the data to current consumer. so you should calla {@linkplain #_setDataConsumer(DataConsumer)} first.
     * @since 1.1.2
     */
    public void applyTo(){
        applyTo(_mConsumer);
    }

    /**
     * <p>Use {@linkplain #beginBatchedDispatches(int)} instead. </p>
     * @since 1.4.1
     */
    @Deprecated
    public void beginBatchedDispatches(){
        if(_mCollector == null){
            _mCollector = new CollectorManagerImpl();
        }
        _mCollector.open(CollectorManagerImpl.FLAG_SIMPLE);
    }
    /**
     * <P>Use {@linkplain #endBatchedDispatches(PropertyEventReceiver)} or {@linkplain #dropBatchedDispatches()} instead.</P>
     * Ends the dispatch transaction and dispatches any remaining event to the callback.
     * @param receiver the receiver which used to receive batch dispatch events. null means use default receiver.
     * @since 1.4.1
     */
    @Deprecated
    public void endBatchedDispatches(final @Nullable PropertyReceiver receiver){
        PropertyEventReceiver real = receiver != null ?
                new PropertyEventReceiver() {
                    @Override
                    public void dispatchValueChanged(Object data, Object originalSource, Property prop, Object oldValue, Object newValue) {
                        receiver.dispatchValueChanged(prop, oldValue, newValue);
                    }
                    @Override
                    public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
                        receiver.dispatchValueApplied(prop, value);
                    }
                }
                : new PropertyEventReceiver() {
            @Override
            public void dispatchValueApplied(Object data, Object originalSource, Property prop, Object value) {
                BaseMediator.this.dispatchValueApplied(prop, value);
            }
            @Override
            public void dispatchValueChanged(Object data, Object originalSource, Property prop, Object oldValue, Object newValue) {
                BaseMediator.this.dispatchValueChanged(prop, oldValue, newValue);
            }
        };
        _mCollector.close(real);
    }

    /**
     * Batch property dispatch  that happen by calling this method until {@linkplain #endBatchedDispatches(PropertyEventReceiver)}.
     * for example: if you have a item data in adapter. but we don't want to update adapter
     * on every property change. you should call this to resolve it.
     * <p> Here is a demo.
     * <pre>
     *     DataMediator{@literal <}Student{@literal >} dm = ...;
     *     dm.getBaseMediator().beginBatchedDispatches(CollectorManagerImpl.FLAGS_ALL);
     *     dm.getDataProxy().setId(xx)
     *               .setName(xxx)
     *               .setGrade(xxx)...;
     *      PropertyEventReceiver receiver = ...;
     *      dm.getBaseMediator().endBatchedDispatches(receiver);
     * </pre>
     * </p>
     * @param collectorFlags  the flags of collector. see {@linkplain CollectorManagerImpl#FLAG_SIMPLE},
     * {@linkplain CollectorManagerImpl#FLAG_LIST}, {@linkplain CollectorManagerImpl#FLAG_SPARSE_ARRAY}
     * @since 1.4.4
     */
    public void beginBatchedDispatches(int collectorFlags){
        if(_mCollector == null){
            _mCollector = new CollectorManagerImpl();
        }
        _mCollector.open(collectorFlags);
    }
    /**
     * drop the all batch dispatch event
     * @since 1.4.4
     */
    public void dropBatchedDispatches(){
        _mCollector.close(null);
    }
    /**
     * Ends the dispatch transaction and dispatches any remaining event to the callback.
     * @param receiver the receiver which used to receive batch dispatch events. null means use default receiver.
     * @since 1.4.4
     */
    public void endBatchedDispatches(final @Nullable PropertyEventReceiver receiver){
        _mCollector.close(receiver != null ? receiver : _mDispatcher);
    }
//=========================================================================

    /**
     * dispatch the change event to the callbacks.
     * <p>use {@linkplain #dispatchValueChanged(Property, Object, Object)} instead, this will be removed in 2.x.</p>
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
    @SuppressWarnings("unchecked")
    public void dispatchValueChanged(Property prop, Object oldValue, Object newValue) {
        if(_mCollector != null && _mCollector.dispatchValueChanged(_mTarget, _mTarget, prop, oldValue, newValue)){
            return;
        }
        _mDispatcher.dispatchValueChanged(_mTarget, _mTarget, prop, oldValue, newValue);
    }

    /**
     * dispatch the apply event for target property.
     *
     * @param prop  the property
     * @param value the value of property.
     * @since 1.0.8
     */
    @SuppressWarnings("unchecked")
    public void dispatchValueApplied(Property prop, Object value) {
        if(_mCollector != null && _mCollector.dispatchValueApplied(_mTarget, _mTarget, prop, value)){
            return;
        }
        _mDispatcher.dispatchValueApplied(_mTarget, _mTarget, prop, value);
    }

    /**
     * dispatch the add event of property.
     *
     * @param prop       the property which is changed.
     * @param newValue   the newest value of property
     * @param addedValue the values which were added.
     * @since 1.0.8
     */
    @SuppressWarnings("unchecked")
    public void dispatchAddValues(Property prop, Object newValue, Object addedValue) {
        if(_mCollector != null && _mCollector.dispatchOnAddPropertyValues(_mTarget, _mTarget, prop, newValue, addedValue)){
            return;
        }
        _mDispatcher.dispatchOnAddPropertyValues(_mTarget, _mTarget, prop, newValue, addedValue);
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
    @SuppressWarnings("unchecked")
    public void dispatchAddValuesWithIndex(Property prop, Object newValue, Object addValue, int index) {
        if(_mCollector != null && _mCollector.dispatchOnAddPropertyValuesWithIndex(_mTarget, _mTarget,
                prop, newValue, addValue, index)){
            return;
        }
        _mDispatcher.dispatchOnAddPropertyValuesWithIndex(_mTarget, _mTarget,
                prop, newValue, addValue, index);
    }

    /**
     * dispatch the remove event of property.
     *
     * @param prop         the property which is changed.
     * @param newValue     the newest value of property
     * @param removeValues the values which is removed..
     * @since 1.0.8
     */
    @SuppressWarnings("unchecked")
    public void dispatchRemoveValues(Property prop, Object newValue, Object removeValues) {
        if(_mCollector != null && _mCollector.dispatchOnRemovePropertyValues(_mTarget, _mTarget,
                prop, newValue, removeValues)){
            return;
        }
        _mDispatcher.dispatchOnRemovePropertyValues(_mTarget, _mTarget,
                prop, newValue, removeValues);
    }

    /**
     * dispatch item changed which is changed between old item and new item on target index.
     * @param prop the property of list items
     * @param oldItem the old item
     * @param newItem the new item
     * @param index the index.
     * @since 1.1.2
     */
    @SuppressWarnings("unchecked")
    public void dispatchItemChanged(Property prop, Object oldItem, Object newItem , int index) {
        if(_mCollector != null && _mCollector.dispatchOnPropertyItemChanged(_mTarget, _mTarget,
                prop, oldItem, newItem, index)){
            return;
        }
        _mDispatcher.dispatchOnPropertyItemChanged(_mTarget, _mTarget,
                prop, oldItem, newItem, index);
    }

    /**
     * get callbacks/
     *
     * @return the callbacks.
     */
    private DataMediatorCallback[] _getCallbacks() {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        DataMediatorCallback[] arrLocal;

        synchronized (this) {
            arrLocal = _mCallbacks.toArray(new DataMediatorCallback[_mCallbacks.size()]);
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
     * get the callback dispatcher of SparseArray
     * @return the callback dispatcher of SparseArray
     * @see com.heaven7.java.base.util.SparseArray
     * @since 1.1.3
     */
    /*protected*/ SparseArrayDispatcher _getSparseArrayDispatcher(){
        if(_mSparseArrayDispatcher == null){
            _mSparseArrayDispatcher = new SparseArrayDispatcher();
        }
        return _mSparseArrayDispatcher;
    }

    /**
     * get internal dispatcher
     * @since 1.4.4
     */
    PropertyEventReceiver _getInternalDispatcher(){
        return _mDispatcher;
    }

    /**
     * dispatch callback of SparseArray.
     * @since 1.1.3
     * @see com.heaven7.java.base.util.SparseArray
     */
    class SparseArrayDispatcher{

        private SparseArrayDispatcher(){}
        /**
         * dispatch add entry event.
         * @param prop the property
         * @param key the key of entry
         * @param value the value of entry
         */
        @SuppressWarnings("unchecked")
        public void dispatchAddEntry(Property prop, int key, Object value){
            _mDispatcher.getSparseArrayDispatcher().dispatchOnAddEntry(_mTarget, _mTarget,  prop, key, value);
        }
        /**
         * dispatch change entry value.
         * @param prop the property
         * @param key the key of entry
         * @param oldValue the old value of entry
         * @param newValue the new value of entry
         */
        @SuppressWarnings("unchecked")
        public void dispatchChangeEntryValue(Property prop, int key, Object oldValue, Object newValue){
            _mDispatcher.getSparseArrayDispatcher().dispatchOnEntryValueChanged(_mTarget, _mTarget,
                    prop, key, oldValue, newValue);
        }
        /**
         * dispatch remove entry event.
         * @param prop the property
         * @param key the key of entry
         * @param value the value of entry
         */
        @SuppressWarnings("unchecked")
        public void dispatchRemoveEntry(Property prop, int key, Object value){
            _mDispatcher.getSparseArrayDispatcher().dispatchOnRemoveEntry(_mTarget, _mTarget,
                    prop, key, value);
        }
        /**
         * dispatch clear all entries.
         * @param prop the property
         * @param entries the all entries which were  removed.
         *                type is {@linkplain com.heaven7.java.base.util.SparseArray}.
         */
        @SuppressWarnings("unchecked")
        public void dispatchClearEntries(Property prop, Object entries){
            _mDispatcher.getSparseArrayDispatcher().dispatchOnClearEntries(_mTarget, _mTarget,
                    prop, entries);
        }
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
            if(!mInterceptor.shouldIntercept(mMediator._getTarget(), prop, value)) {
                this.mProps.add(prop);
                this.mValue.add(value);
            }
            return this;
        }

        /**
         * batch apply the properties with theirs' value.
         */
        @SuppressWarnings("unchecked")
        public void apply() {
            final T data = mMediator._getTarget();
            final DataMediatorCallback[] arrLocal = mMediator._getCallbacks();
            final int size = mProps.size();

            PropertyCallbackContext.Params params = new PropertyCallbackContext.Params(data, 0);
            DataMediatorCallback callback;
            for (int i = arrLocal.length - 1; i >= 0; i--) {
                callback = arrLocal[i];
                for (int j = 0; j < size; j++) {
                    callback.onPreCallback(params);
                    callback.onPropertyApplied(data, mProps.get(j), mValue.get(j));
                    callback.onPostCallback();
                }
            }
        }
    }
}
