package com.heaven7.java.data.mediator.util;

/**
 * the equals comparator
 * Created by heaven7 on 2017/9/13 0013.
 * @since 1.0.2
 */
public interface EqualsComparator {

    /**
     * called when want to compare the values.
     * @param v1 the value one
     * @param v2 the value two
     * @return true if is equals.
     */
    boolean isEquals(Object v1, Object v2);

}
