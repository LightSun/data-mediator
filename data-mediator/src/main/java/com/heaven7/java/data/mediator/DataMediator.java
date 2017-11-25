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
import com.heaven7.java.data.mediator.collector.PropertyEventReceiver;
import com.heaven7.java.data.mediator.util.EqualsComparator;
import com.heaven7.java.data.mediator.collector.CollectorManager;

import java.util.*;

/**
 * the data mediator .wrap a base mediator and module data.
 * Created by heaven7 on 2017/9/14 0014.
 * @since 1.0.4
 */
public final class DataMediator<T> {

    private final BaseMediator<T> mediator;

    private PropertyChainInflater<T> mInflater;
    private List<String> mFailedPropChains;

    /**
     * create data mediator
     * @param mediator the base mediator.
     */
    /*public*/ DataMediator(BaseMediator<T> mediator) {
        this.mediator = mediator;
    }

    /**
     * get base Mediator callback.
     * @return the mediator.
     * @see BaseMediator
     */
    public final BaseMediator<T> getBaseMediator(){
        return mediator;
    }

    /**
     * <p>Use {@linkplain #beginBatchedDispatches(int)} instead. this wll be removed in 2.x</p>
     * @since 1.4.1
     */
    @Deprecated
    public void beginBatchedDispatches(){
        getBaseMediator().beginBatchedDispatches();
    }
    /**
     * <p>Use {@linkplain #endBatchedDispatches(PropertyEventReceiver)} instead. this wll be removed in 2.x</p>
     * Ends the dispatch transaction and dispatches any remaining event to the callback.
     * @param receiver the receiver which used to receive batch dispatch events. null means use default receiver.
     * @since 1.4.1
     */
    @Deprecated
    public void endBatchedDispatches(@Nullable  PropertyReceiver receiver){
        getBaseMediator().endBatchedDispatches(receiver);
    }

    /**
     * Batch property dispatch  that happen by calling this method until {@linkplain #endBatchedDispatches(PropertyEventReceiver)}.
     * for example: if you have a item data in adapter. but we don't want to update adapter
     * on every property change. you should call this to resolve it.
     * <p> Here is a demo.
     * <pre>
     *     DataMediator{@literal <}Student{@literal >} dm = ...;
     *     dm.beginBatchedDispatches(CollectorManagerImpl.FLAGS_ALL);
     *     dm.getDataProxy().setId(xx)
     *               .setName(xxx)
     *               .setGrade(xxx)...;
     *      PropertyEventReceiver receiver = ...;
     *      dm.endBatchedDispatches(receiver);
     * </pre>
     * </p>
     * @param collectorFlags  the flags of collector. see {@linkplain CollectorManager#FLAG_SIMPLE},
     * {@linkplain CollectorManager#FLAG_LIST}, {@linkplain CollectorManager#FLAG_SPARSE_ARRAY}
     * @since 1.4.4
     */
    public void beginBatchedDispatches(int collectorFlags){
         getBaseMediator().beginBatchedDispatches(collectorFlags);
    }
    /**
     * Ends the dispatch transaction and dispatches any remaining event to the callback.
     * @param receiver the receiver which used to receive batch dispatch events. null means use default receiver.
     * @since 1.4.4
     */
    public void endBatchedDispatches(final @Nullable PropertyEventReceiver receiver){
        getBaseMediator().endBatchedDispatches(receiver);
    }
    /**
     * drop the all batch dispatch event
     * @since 1.4.4
     */
    public void dropBatchedDispatches(){
        getBaseMediator().dropBatchedDispatches();
    }

    /**
     * get the exact module data.
     * @return the module data.
     */
    public final T getData(){
        return getBaseMediator()._getTarget();
    }
    /**
     * get the data proxy which support additional features, such as: property change callback.
     * @return the data proxy.
     * @see DataMediatorCallback
     * @see BaseMediator
     * @since 1.0.6
     */
    @SuppressWarnings("unchecked")
    public final T getDataProxy(){
        return (T) mediator;
    }

    /**
     * inflate the property chain as callback .
     * @param propertyChain the property chain
     * @since 1.4.4
     */
    public void inflatePropertyChain(String propertyChain){
        Throwables.checkNull(propertyChain);
         if(!propertyChain.contains(".")){
             throw new IllegalArgumentException("only support property chain. like 'viewBind.background'.");
         }
         if(!inflatePropertyChain0(propertyChain)){
             if(mFailedPropChains == null){
                 mFailedPropChains = new ArrayList<>();
             }
             mFailedPropChains.add(propertyChain);
         }
    }

    /**
     * indicate should reinflate property chain or not, which was inflate failed previous.
     * @return true if should reinflate. false otherwise.
     * @since 1.4.4
     */
    public boolean shouldReinflatePropertyChains(){
        return mFailedPropChains != null && !mFailedPropChains.isEmpty();
    }

    /**
     * reinflate the property chains if previous inflate failed.
     * @return true if no need inflate or reinflate all left property chains success.
     * @since 1.4.4
     */
    public boolean reinflatePropertyChains(){
        if(mFailedPropChains != null && !mFailedPropChains.isEmpty()){
            final Iterator<String> it = mFailedPropChains.iterator();
            do {
                if(inflatePropertyChain0(it.next())){
                    it.remove();
                }
            }while (it.hasNext());
            return mFailedPropChains.isEmpty();
        }
        return true;
    }
    /**
     * get the inflate callbacks of target child data.
     * @param data the child data.
     * @return the inflate callbacks. may be empty, but never be null.
     * @since 1.4.4
     */
    public List<DataMediatorCallback> getInflateCallbacks(Object data){
        Throwables.checkNull(data);
        if(mInflater == null){
            return Collections.emptyList();
        }
        return mInflater.getInflateCallbacks(data);
    }

    /**
     * get the data consumer.
     * @return the data consumer
     * @since 1.1.2
     */
    public DataConsumer<? super T> getDataConsumer() {
        return mediator._getDataConsumer();
    }

    /**
     * set the data consumer
     * @param mConsumer the data consumer
     * @since 1.1.2
     */
    public void setDataConsumer(DataConsumer<? super T> mConsumer) {
        mediator._setDataConsumer(mConsumer);
    }

    /**
     * get the property interceptor , default is {@linkplain PropertyInterceptor#NULL}
     * @return the property interceptor.
     * @since 1.1.3
     */
    public PropertyInterceptor getPropertyInterceptor() {
        return mediator._getPropertyInterceptor();
    }

    /**
     * set the property interceptor . default is {@linkplain PropertyInterceptor#NULL}
     * @param interceptor the target property interceptor.
     * @since 1.1.3
     */
    public void setPropertyInterceptor(PropertyInterceptor interceptor) {
        mediator._setPropertyInterceptor(interceptor);
    }

    /**
     * get the equals comparator.
     *
     * @return the equals comparator.
     * @since 1.1.2
     */
    public EqualsComparator getEqualsComparator() {
        return mediator._getEqualsComparator();
    }

    /**
     * set the equals comparator which is used to judge the different between the old value and new value.
     * default is {@linkplain com.heaven7.java.data.mediator.util.DefaultEqualsComparator}.
     * @param comparator the equals comparator
     */
    public void setEqualsComparator(EqualsComparator comparator) {
        mediator._setEqualsComparator(comparator);
    }

    /**
     * apply all properties.
     * @see BaseMediator#applyProperties()
     * @since 1.0.8
     */
    public void applyProperties(){
        mediator.applyProperties();
    }

    /**
     * apply all properties with target interceptor.
     * @param interceptor the property interceptor
     * @see BaseMediator#applyProperties(PropertyInterceptor)
     * @since 1.0.8
     */
    public void applyProperties(PropertyInterceptor interceptor){
        mediator.applyProperties(interceptor);
    }

    /**
     * start action mode with target callback. here is a simple demo.
     * <code><pre>
     AnalysisManager.getInstance()
         .getDataMediator()
         .startActionMode(new ActionMode.Callback<AnalysisDataModule>() {
            {@literal }@Override
            public void onPrepareActionMode(ActionMode<AnalysisDataModule> mode) {
                mode.getData().setOccurTime(System.currentTimeMillis())
                .setEventType("click")
                .setItem(item)
                .setItemIndex(position);
            }
        }).applyTo();
     * </pre></></code>
     * @param callback the callback of action mode
     * @return the action mode
     * @since 1.1.3
     */
    public ActionMode<T> startActionMode(ActionMode.Callback<T> callback){
        ActionMode<T> mode = new ActionMode<T>(this);
        callback.onPrepareActionMode(mode);
        return mode;
    }

    //=======================================================
    /**
     * add a data mediator callback
     * @param callback  the data mediator callback
     */
    public void addDataMediatorCallback(DataMediatorCallback<? super T> callback){
        mediator.addCallback(callback);
    }
    /**
     * remove data mediator callback
     * @param callback the data mediator callback
     */
    public void removeDataMediatorCallback(DataMediatorCallback<? super T> callback) {
        mediator.removeCallback(callback);
    }

    /**
     * remove the all data mediator callbacks.
     */
    public void removeDataMediatorCallbacks() {
        mediator.removeCallbacks();
    }


    //============================ private methods ============================
    private boolean inflatePropertyChain0(String propertyChain){
        if(mInflater == null){
            mInflater = new PropertyChainInflater<T>(this);
        }
        return mInflater.inflatePropertyChain(propertyChain);
    }

}
