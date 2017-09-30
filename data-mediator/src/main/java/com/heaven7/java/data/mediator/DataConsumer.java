package com.heaven7.java.data.mediator;

/**
 * the data consumer
 * @param <T> the data type
 * Created by heaven7 on 2017/9/30 0030.
 * @since 1.1.2
 */
public interface DataConsumer<T> {


    /**
     * called on process the target data.
     * @param data the data
     */
    void accept(T data);
}
