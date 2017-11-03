package com.heaven7.java.data.mediator.internal;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define a bind method info.
 * @author heaven7
 * @since 1.1.5
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface BindMethod {

    /**
     * the method name
     * @return the method name
     */
    String value();
    /**
     * the parameter types
     * @return the parameter types
     */
    Class<?>[] paramTypes() default { String.class, Object.class };
}