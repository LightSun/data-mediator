package com.heaven7.java.data.mediator.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define the binder factory class.<br>
 * Created by heaven7 on 2017/11/5.
 * @since 1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BinderFactoryClass {

    /**
     * get the binder factory class. see 'com.heaven7.java.data.mediator.BinderFactory' for more info.
     * @return the binder factory class.
     */
    Class<?> value();
}
