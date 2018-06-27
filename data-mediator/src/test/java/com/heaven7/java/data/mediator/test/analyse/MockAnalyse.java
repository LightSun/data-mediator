package com.heaven7.java.data.mediator.test.analyse;

import com.heaven7.java.data.mediator.test.SimpleTraceAnalyzer;
import org.junit.Test;

public class MockAnalyse {

    TraceManager tm;
    TestTarget target;

    // A, B, C,  D   D,  C , B, A
    //@Test
    public void test1(){
        //target.register
        tm.register(TestTarget.class, new SimpleTraceAnalyzer());
        tm.register("Student",  new SimpleTraceAnalyzer());
        tm.unregister(TestTarget.class);
        tm.unregister("Student");

        tm.beginTrace("your tag");
        tm.endTrace("your tag");
        //TraceModuleDelegate  beginTrace .endTrace.
        //- tag = enter -> click dialog evaluate -> tag = EvaluateStart -> click audio -> click option A of subjectXXX -> Evaluate1...
        // Async
    }

    public static class TestTarget{

    }
}
