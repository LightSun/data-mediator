package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * define the data fields.
 * used for interface as mock class define.
 * @author heaven7
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Fields {

	Field[] value();
	//int interfaceFlags();
}
