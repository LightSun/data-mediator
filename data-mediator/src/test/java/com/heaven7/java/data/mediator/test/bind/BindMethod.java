package com.heaven7.java.data.mediator.test.bind;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface BindMethod {

    String value();

    Class<?>[] paramTypes();
}
