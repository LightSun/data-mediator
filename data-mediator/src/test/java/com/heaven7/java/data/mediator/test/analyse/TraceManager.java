package com.heaven7.java.data.mediator.test.analyse;

public interface TraceManager {

    int TRACE_FLAG_PROPERTY_SETTING =  1;
    int TRACE_FLAG_PROPERTY_CHANGE  =  2;
    int TRACE_FLAG_MODULE           =  4;

    void register(Class<?> module, TraceAnalyzer moduleProcessor);
    void register(String tag, TraceAnalyzer moduleProcessor);
    void register(String[] tags, TraceAnalyzer moduleProcessor);
    TraceAnalyzer unregister(Class<?> module);

    void beginTrace(String your_tag);

    void endTrace(String your_tag);

    void unregister(String your_tag);
}
