package com.heaven7.java.data.mediator.compiler.test;

/**
 * Created by heaven7 on 2017/9/13 0013.
 */
public class StudentModuleImpl implements IStudent {

    private int age;
    private String name;
    private String id;

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object copy() {
        return null;
    }

    @Override
    public void copyTo(Object out) {

    }

    @Override
    public void reset() {

    }
}
