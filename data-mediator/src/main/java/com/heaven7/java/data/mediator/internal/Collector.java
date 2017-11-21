package com.heaven7.java.data.mediator.internal;

public class Collector {

    private final Object target;
    private PropertyCollector mSimpleCollector;
    private ListPropertyCollector mListCollector;
    private MapPropertyCollector<Integer> mSparseCollector;

    public Collector(Object target) {
        this.target = target;
    }
}
