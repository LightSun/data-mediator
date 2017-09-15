package com.heaven7.java.data.mediator.test.copy;

import com.heaven7.java.data.mediator.ICopyable;

/**
 * Created by heaven7 on 2017/9/12 0012.
 */

public class Student implements IStudent, ICopyable<IStudent> {

    private String name ;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public IStudent copy() {
        Student sdu = new Student();
        copyTo(sdu);
        return sdu;
    }

    @Override
    public void copyTo(Object out) {
        if(out instanceof IStudent){
            ((IStudent)out).setName(this.getName());
        }
    }
}
