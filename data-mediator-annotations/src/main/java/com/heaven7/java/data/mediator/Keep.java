package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate that the field or method should be keep. it will not effect by the intellij plugin of 'data-mediator'.
 * <code><pre>
 *      {@literal @}keep
        int STATE_OK = 1;
 * </pre></code>
 * @author heaven7
 * @since 1.1.3
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Keep {
}
