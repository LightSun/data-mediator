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
     * the current version for gson serialize/deserialize json.
     * <p>this will cause create gson something like this: </p>
     * <code><pre>
     *      Gson gson = new GsonBuilder().setVersion(2.0).create();
     * </pre></code>
     * @return the current version of gson,
     */
    double gsonVersion() default 1.0;

    /**
     * use {@literal @}JsonAdapter or not. that means is this is enabled .
     * it will auto generate TypeAdapter and generate {@literal @}JsonAdapter.
     * @return true if enable json adapter or not. you can see more in 'Google/Gson'.
     */
    boolean jsonAdapter() default true;
}
