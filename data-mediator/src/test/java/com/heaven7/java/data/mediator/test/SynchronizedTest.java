package com.heaven7.java.data.mediator.test;


/**
 * test synchronized.
 */
//'javap -sysinfo -v -c com.heaven7.java.data.mediator.test.SynchronizedTest
public class SynchronizedTest {

    private static int sIndex = 0;
    private int id;

    public static synchronized void setIndex(int index){
        sIndex = index;
    }

    public synchronized void setId(int id){
        this.id = id;
    }

    public void setId2(int id){
        synchronized (this) {
            this.id = id;
        }
    }

}
