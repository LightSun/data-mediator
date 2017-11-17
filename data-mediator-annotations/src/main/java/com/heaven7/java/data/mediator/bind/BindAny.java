package com.heaven7.java.data.mediator.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the annotation which can bind any property to any view's any method.
 * It must be used in conjunction with {@linkplain BindMethodSupplierClass} and self Binder.<br>
 *     <code><pre>
 *          {@literal @}BindAny(value = "text", method = "bindAppendText")
              TextView mTv_supplier;
 *     </pre></code>
 * Created by heaven7 on 2017/11/16.
 * @since 1.4.3
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindAny {
    /**
     * the property name of data-module which will be used to bind object.
     * @return the property name
     */
    String value();

    /**
     * the bind method name of Binder
     */
    String method();

    /**
     * the object index. which used for bind multi objects .
     * @return the object index.
     */
    int index() default 0;
}
