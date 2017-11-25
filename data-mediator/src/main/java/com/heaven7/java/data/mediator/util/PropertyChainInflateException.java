package com.heaven7.java.data.mediator.util;

/**
 * indicate the property chain is unexpected. so throws this exception
 * @author heaven7
 * @since 1.4.4
 */
public class PropertyChainInflateException extends RuntimeException {

    public PropertyChainInflateException(String message, Throwable cause) {
        super(message, cause);
    }
    public PropertyChainInflateException(String message) {
        super(message);
    }
}
