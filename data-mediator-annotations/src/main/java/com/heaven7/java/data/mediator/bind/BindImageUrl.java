package com.heaven7.java.data.mediator.bind;

import com.heaven7.java.data.mediator.internal.BindMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bind image link url of view . eg: ImageView.
 * it will auto set image from url when you change image url from proxy.
 * <pre>
 *     {@literal @}BindImageUrl("url") ImageView iv;
 * </pre>
 * @author heaven7
 * @since  1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@BindMethod(value = "bindImageUrl", paramTypes = { String.class, Object.class, Object.class })
public @interface BindImageUrl {

    /**
     * the property name which will be used to bind object.
     * @return the property name
     */
    String value();

    /**
     * the object index. which used for bind multi objects .
     * @return the object index.
     */
    int index() default 0;
}
