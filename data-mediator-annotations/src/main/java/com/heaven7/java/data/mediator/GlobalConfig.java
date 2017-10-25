package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the global gson config for data-mediator.
 * @since 1.0.5
 * @author heaven7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GlobalConfig {

    /**
     * the gson configof 'Google/Gson'
     * @return the gson config.
     */
    GsonConfig gsonConfig() default @GsonConfig();
}
