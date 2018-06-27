package com.heaven7.java.data.mediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the group property description. see {@linkplain com.heaven7.java.data.mediator.GroupDataManager.GroupProperty}.
 * @author heaven7
 * @since 1.2.3
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GroupDesc {
    //@GroupProp(type = MUTEX, prop ="state", value=XXX, mutex=Oppo_val,flags = true)

    /**
     * define the property which will regard as the group property.
     * @return the group property name
     */
    String prop();

    /**
     * define the type of how to use group property which is define by {@linkplain #prop()}
     * @return the type
     */
    byte type() default GroupDataManager.TYPE_MUTEX;

    /**
     * define focus value which will be listen.
     * @return the focus value. if is 1 or 0. the raw field can be 'boolean' type.
     */
    long focusVal();
    /**
     * define opposite value which will be set when {@linkplain #focusVal()} is listened.
     * @return the opposite value. if is 1 or 0. the raw field can be 'boolean' type.
     */
    long oppositeVal();

    /**
     * decide the {@linkplain #focusVal()} will be used as flag or not. if true. the {@linkplain #oppositeVal()} is useless.
     * @return true if as flag. default is false.
     */
    boolean asFlag() default false;
}
