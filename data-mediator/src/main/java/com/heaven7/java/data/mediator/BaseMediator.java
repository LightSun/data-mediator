package com.heaven7.java.data.mediator;

import com.heaven7.java.data.mediator.util.DefaultEqualsComparator;
import com.heaven7.java.data.mediator.util.EqualsComparator;

import java.util.ArrayList;

/**
 * the base mediator class.
 * Created by heaven7 on 2017/9/11 0011.
 */
public class BaseMediator<T>{

    private final ArrayList<DataMediatorCallback<? super T>> mCallbacks;
    private final T mTarget;
    private EqualsComparator mEqualsComparator = DefaultEqualsComparator.getInstance();

    /**
     * create a base mediator by target object.
     * @param target the target object which is ready to been act as agent by this.
     */
    public BaseMediator(T target){
         this.mTarget = target;
         this.mCallbacks = new ArrayList<>();
    }

    /**
     * get the target object which is acted as agent by this object.
     * @return the target object
     */
    public T getTarget(){
        return mTarget;
    }

    /**
     * get the equals comparator.
     * @return the equals comparator.
     * @since 1.0.2
     */
    public EqualsComparator getEqualsComparator() {
        return mEqualsComparator;
    }

    /**
     * set the equals comparator
     * @param comparator the equals comparator
     * @since 1.0.2
     */
    public void setEqualsComparator(EqualsComparator comparator) {
        this.mEqualsComparator = comparator;
    }

    /**
     * add  a data mediator callback
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
     *  count the data mediator callback.
     * @return the count of the data mediator callbacks.
     */
    public synchronized int countCallbacks() {
        return mCallbacks.size();
    }

    /**
     * dispatch the change event to the callbacks.
     * @param prop the property which is changed.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    public void dispatchCallbacks(Property prop, Object oldValue, Object newValue) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        DataMediatorCallback[] arrLocal;

        synchronized (this) {
            arrLocal = mCallbacks.toArray(new DataMediatorCallback[mCallbacks.size()]);
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            arrLocal[i].onPropertyValueChanged(mTarget ,prop, oldValue, newValue);
    }
}
