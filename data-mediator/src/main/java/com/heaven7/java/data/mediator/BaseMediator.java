package com.heaven7.java.data.mediator;

import java.util.ArrayList;

/**
 * Created by heaven7 on 2017/9/11 0011.
 */
public class BaseMediator<T>{

    private final ArrayList<DataMediatorCallback<? super T>> mCallbacks;
    private final T mTarget;

    public BaseMediator(T target){
         this.mTarget = target;
         this.mCallbacks = new ArrayList<>();
    }

    public T getTarget(){
        return mTarget;
    }

    public synchronized void addCallback(DataMediatorCallback<? super T> o) {
        if (o == null)
            throw new NullPointerException();
        if (!mCallbacks.contains(o)) {
            mCallbacks.add(o);
        }
    }

    public synchronized void removeCallback(DataMediatorCallback<? super T> o) {
        mCallbacks.remove(o);
    }

    public synchronized void removeCallbacks() {
        mCallbacks.clear();
    }

    public synchronized int countCallbacks() {
        return mCallbacks.size();
    }

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
