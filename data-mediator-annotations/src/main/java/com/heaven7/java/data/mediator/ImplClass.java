package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define the impl class of module's 'self-method', this can only used to interface in 'data-mediator'
 * Created by heaven7 on 2017/10/24.
 * @since 1.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ImplClass {
    /**
     * indicate the main class which hold the impl of 'self-method'.
     * @return the main class which hold the impl of 'self-method'.
     */
    Class<?> value();
}
