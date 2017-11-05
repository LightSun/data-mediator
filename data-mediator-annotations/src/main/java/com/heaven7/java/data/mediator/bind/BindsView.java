package com.heaven7.java.data.mediator.bind;

import com.heaven7.java.data.mediator.internal.BindMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bind an array of property names in order, currently like: 'backgroundRes'. 'visibility'. 'enable'.<br>
 * Created by heaven7 on 2017/11/5.
 * @since 1.2.0
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindsView {

    /**
     * bind an array of property names in order. currently like: 'backgroundRes'. 'visibility'. 'enable'.
     * <p>Note: the property must BE correspond to the {@linkplain #methods()}</p>
     * @return an array of property names in order.
     */
    String[] value();

    /**
     * define the all bind method infos.
     * @return the all bind method infos.
     */
    BindMethod[] methods() default {
        @BindMethod("bindBackgroundRes"),
        @BindMethod("bindVisibility"),
        @BindMethod("bindEnable"),
    };

    /**
     * the object index. which used for bind multi objects .
     * @return the object index.
     */
    int index() default 0;
}
