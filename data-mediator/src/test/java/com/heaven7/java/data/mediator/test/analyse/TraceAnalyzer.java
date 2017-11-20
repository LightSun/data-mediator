package com.heaven7.java.data.mediator.test.analyse;


import java.util.List;

public abstract class TraceAnalyzer {

     public abstract void onProcessProperty(Object module, List<TraceEvent> event);

}


