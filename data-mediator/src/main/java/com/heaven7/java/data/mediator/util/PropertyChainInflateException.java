package com.heaven7.java.data.mediator.util;

/**
 * indicate inflate the property chain failed as property chain is incorrect.
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
