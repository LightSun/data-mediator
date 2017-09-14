package com.heaven7.java.data.mediator;

/**
 * the proxy helper .
 * Created by heaven7 on 2017/9/14 0014.
 * @since 1.0.4
 */
public final class ProxyHelper<T> {

    private final BaseMediator<T> mediator;

    public ProxyHelper(BaseMediator<T> mediator) {
        this.mediator = mediator;
    }

    /**
     * regard the proxy as mediator.
     * @return the mediator.
     */
    public final BaseMediator<T> asMediator(){
        return mediator;
    }
    /**
     * regard the proxy as module data.
     * @return the module.
     */
    public final T asModule(){
        return (T) mediator;
    }
}
