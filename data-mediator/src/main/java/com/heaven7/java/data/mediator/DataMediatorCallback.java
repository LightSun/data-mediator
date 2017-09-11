package com.heaven7.java.data.mediator;

/**
 *  the listener of property change.
 * Created by heaven7 on 2017/9/11 0011.
 */
public abstract class DataMediatorCallback<T>{

    /**
     * called on property value changed.
     * @param data  the property of target object .
     * @param prop  the property to describe the field.
     * @param oldValue the old value of property
     * @param newValue the new value of property
     */
    public abstract void onPropertyValueChanged(T data, Property prop, Object oldValue, Object newValue);


//will support for this.
    // void onCall(T data, Method m);
    //void onCallMethod((T data, );
    //boolean onInterceptMethod(data,  MethodSignature);

}
