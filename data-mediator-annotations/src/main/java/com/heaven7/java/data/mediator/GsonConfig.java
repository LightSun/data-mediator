package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the global gson setting for data-mediator.
 * @since 1.0.5
 * @author heaven7
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GsonConfig {

    /**
     * the current version for gson serialize/deserialize json.
     * <p>this will cause create gson something like this: </p>
     * <code><pre>
     *      Gson gson = new GsonBuilder().setVersion(2.0).create();
     * </pre></code>
     * @return the current version of gson,
     */
    double version() default 1.0;

    /**
     * use {@literal @}JsonAdapter or not. that means is this is enabled .
     * it will auto generate TypeAdapter and generate {@literal @}JsonAdapter.
     * <p>Note: if you don't want any module generate of Json Adapter. please return false.
     * otherwise return true. if you want only some module generate json adapter.
     * just make other modules {@linkplain Fields#generateJsonAdapter()} return false.
     * of course this should return true(Global setting).  </p>
     * @return true if enable json adapter or not. you can see more in 'Google/Gson'.
     * @see Fields#generateJsonAdapter()
     */
    boolean generateJsonAdapter() default true;

    /**
     * force disable generate gson annotation. this is a global config for gson to enable or not.
     * @return true if you don't want gson annotation.
     */
    boolean forceDisable() default false;
}
