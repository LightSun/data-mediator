package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.test.data.StudentModule;
import com.heaven7.java.data.mediator.test.data.StudentModule_Impl;
import junit.framework.TestCase;

/**
 * Created by Administrator on 2017/9/27 0027.
 */
public class TestPool extends TestCase {

    @Override
    protected void setUp() throws Exception {
        DataPools.preparePool(StudentModule_Impl.class.getName(), 10);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        //DataPools.sMap.clear();
    }

   /* public void testObtain(){
        try {
            DataPools.obtain(StudentModule_Impl.class);
        }catch (Exception e){
            //must exception
        }

        StudentModule result = DataPools.obtain(StudentModule.class);
        assertNotNull(result);
        assertEquals(DataPools.size(StudentModule.class), 0);
        assertEquals(DataPools.sMap.size(), 1);

        result.recycle();
        assertEquals(DataPools.size(StudentModule.class), 1);

        result.recycle();
        assertEquals(DataPools.size(StudentModule.class), 1);

        DataPools.obtain(StudentModule.class).recycle();
        assertEquals(DataPools.size(StudentModule.class), 1);

        DataMediatorFactory.createData(StudentModule.class).recycle();
        assertEquals(DataPools.size(StudentModule.class), 2);
    }*/
}
