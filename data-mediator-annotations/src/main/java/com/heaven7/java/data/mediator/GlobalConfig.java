package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the global setting for data-mediator.
 * @since 1.0.5
 * @author heaven7
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GlobalConfig {

    /**
     * the current gson version for serialize/deserialize json.
     * <p>this will cause create gson something like this: </p>
     * <code><pre>
     *      Gson gson = new GsonBuilder().setVersion(2.0).create();
     * </pre></code>
     * @return the current version of gson,
     */
    double currentGson() default 1.0;
}
