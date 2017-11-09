package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.PropertyReceiver;

/**
 * the property collector.
 * Created by heaven7 on 2017/11/8.
 * @since 1.4.1
 */
public interface PropertyCollector extends PropertyDispatcher {

    /**
     * open property collector.
     */
    void open();

    /**
     * is the collector opened or not.
     * @return true if the collector is opened.
     */
    boolean isOpened();

    /**
     * close property collector. and fire all event. after dispatch the event pool is cleared.
     * @param receiver the property receiver. if receiver == null, the all event will be discard.
     */
    void close(PropertyReceiver receiver);
}
