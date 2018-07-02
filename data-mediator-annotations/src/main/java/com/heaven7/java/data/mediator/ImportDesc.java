package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by heaven7 on 2018/6/30.
 * @since 1.2.4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ImportDesc {

    /**
     * the all alia names which are used in expression.
     * @return the alia names
     */
    String[] names();

    /**
     * the class which mapped by {@linkplain #names()}.
     * @return the import classes which is used as import static class.
     */
    Class<?>[] classes();
}
