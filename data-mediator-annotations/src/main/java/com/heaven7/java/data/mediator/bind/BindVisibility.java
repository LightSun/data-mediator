package com.heaven7.java.data.mediator.bind;

import com.heaven7.java.data.mediator.internal.BindMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bind view visibility.
 * it will auto set Visibility when you change visibility from proxy.
 * <pre>
 *     {@literal @}BindVisibility("visible") TextView title;
 * </pre>
 * @author heaven7
 * @since 1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@BindMethod(value = "bindVisibility", paramTypes = {String.class, Object.class, boolean.class})
public @interface BindVisibility {

    /**
     * the property name which will be used to bind object.
     * @return the property name
     */
    String value();

    /**
     * force use the visibility as boolean or not. default is true.
     * @return true if the value type of visibility is boolean.
     */
    boolean forceAsBoolean() default true;
    /**
     * the object index. which used for bind multi objects .
     * @return the object index.
     */
    int index() default 0;
}
