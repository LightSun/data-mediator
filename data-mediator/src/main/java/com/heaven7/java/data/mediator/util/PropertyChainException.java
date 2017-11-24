package com.heaven7.java.data.mediator.util;

/**
 * indicate the property chain is unexpected. so throws this exception
 * @author heaven7
 * @since 1.4.4
 */
public class PropertyChainException extends RuntimeException {

    public PropertyChainException(String message, Throwable cause) {
        super(message, cause);
    }
    public PropertyChainException(String message) {
        super(message);
    }
}
