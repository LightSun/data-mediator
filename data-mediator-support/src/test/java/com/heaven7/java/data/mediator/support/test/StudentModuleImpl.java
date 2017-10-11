package com.heaven7.java.data.mediator.support.test;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by heaven7 on 2017/9/13 0013.
 */
@JsonAdapter(StudentTypeAdapter.class)
public class StudentModuleImpl implements IStudent {

    private int age;
    private String name;
    private String id;
    private List<String> mTags;

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
    public void setTags(List<String> tags) {
        this.mTags = tags;
    }

    @Override
    public List<String> getTags() {
        return mTags;
    }

    @Override
    public String toString() {
        return "StudentModuleImpl{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
