package com.heaven7.java.data.mediator.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the annotation which can bind an array of properties to any view's array of methods.
 * It must be used in conjunction with {@linkplain BindMethodSupplierClass} and self Binder.<br>
 *     <code><pre>
             {@literal @}BindsAny(value = {"prop1", "prop2"}, methods = {"bindAddText1", "bindAddText2"})
              TextView mTv_supplier;
 *     </pre></code>
 * Created by heaven7 on 2017/11/16.
 * @since 1.4.3
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindsAny {
    /**
     * the property names of data-module which will be used to bind object.
     * @return the property name
     */
    String[] value();

    /**
     * the method names of binder
     * @return the method supplier classes.
     */
    String[] methods();

    /**
     * the object index. which used for bind multi objects .
     * @return the object index.
     */
    int index() default 0;
}
