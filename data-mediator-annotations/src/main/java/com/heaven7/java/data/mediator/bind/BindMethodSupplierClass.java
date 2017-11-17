package com.heaven7.java.data.mediator.bind;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the bind method supplier class. which supplier the method parameter types of {@linkplain BindAny} or {@linkplain BindsAny}.
 * It must be used in conjunction with {@linkplain BindAny} or {@linkplain BindsAny} and self Binder.<br>
 * @since 1.2.1
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BindMethodSupplierClass {

    /**
     * the bind method parameter type supplier. see 'com.heaven7.java.data.mediator.BindMethodSupplier'.
     * @return the child class which implements 'com.heaven7.java.data.mediator.BindMethodSupplier'.
     */
    Class<?> value();
}
