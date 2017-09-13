package com.heaven7.java.data.mediator.util;

/**
 * the default equals comparator
 * Created by heaven7 on 2017/9/13 0013.
 * @since 1.0.2
 */
public class DefaultEqualsComparator implements EqualsComparator {

    private static DefaultEqualsComparator sComparator;

    private DefaultEqualsComparator(){}

    /**
     * get an instance of DefaultEqualsComparator.
     * @return  an instance of DefaultEqualsComparator.
     */
    public static DefaultEqualsComparator getInstance(){
        return sComparator != null ? sComparator : (sComparator = new DefaultEqualsComparator());
    }

    @Override
    public boolean isEquals(Object v1, Object v2) {
        if(v1 == null){
            return v2 == null;
        }else{
            return v1.equals(v2);
        }
    }
}
