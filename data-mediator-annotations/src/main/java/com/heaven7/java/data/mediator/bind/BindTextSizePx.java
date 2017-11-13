package com.heaven7.java.data.mediator.bind;

import com.heaven7.java.data.mediator.internal.BindMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bind text size (dip value) of view . eg: TextView.
 * it will auto set text size as pix when you change text size from proxy.
 * <pre>
 *     {@literal @}BindTextSizePx("textSizePx") TextView title;
 * </pre>
 * @author heaven7
 * @since  1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@BindMethod("bindTextSizePx")
public @interface BindTextSizePx {

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
