package com.heaven7.java.data.mediator.bind;

import com.heaven7.java.data.mediator.internal.BindMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bind checkable of view . eg: android.widget.CheckBox.
 * it will auto set checked or not when you change check state from proxy.
 * <pre>
 *     {@literal @}BindCheckable("checkable") CheckBox cb;
 * </pre>
 * @author heaven7
 * @since  1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
@BindMethod("bindCheckable")
public @interface BindCheckable {

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
