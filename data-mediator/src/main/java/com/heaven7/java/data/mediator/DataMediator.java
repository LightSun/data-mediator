package com.heaven7.java.data.mediator;

import com.heaven7.java.data.mediator.util.EqualsComparator;

/**
 * the data mediator .wrap a base mediator and module data.
 * Created by heaven7 on 2017/9/14 0014.
 * @since 1.0.4
 */
public final class DataMediator<T> {

    private final BaseMediator<T> mediator;

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
     * get the exact module data.
     * @return the module data.
     */
    public final T getData(){
        return getBaseMediator().getTarget();
    }

    /**
     * get the data proxy which support additional features, such as: property change callback.
     * @return the data proxy.
     * @see DataMediatorCallback
     * @see BaseMediator
     * @since 1.0.6
     */
    public final T getDataProxy(){
        return (T) mediator;
    }

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

    /**
     * set the equals comparator which is used to judge the different between the old value and new value.
     * default is {@linkplain com.heaven7.java.data.mediator.util.DefaultEqualsComparator}.
     * @param comparator the equals comparator
     */
    public void setEqualsComparator(EqualsComparator comparator) {
        mediator.setEqualsComparator(comparator);
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
}
