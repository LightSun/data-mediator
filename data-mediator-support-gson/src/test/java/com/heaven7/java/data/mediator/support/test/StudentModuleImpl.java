package com.heaven7.java.data.mediator.support.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by heaven7 on 2017/9/13 0013.
 */
@JsonAdapter(StudentTypeAdapter.class) //使用这个后. 字段上的json注解不起作用了
public class StudentModuleImpl implements IStudent {

    private int age;
    @Expose(
            serialize = false,
            deserialize = false
    )
    private String name;
    @Expose()
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
                "name='" + name + '\'' +
                ", age=" + age +
                ", id='" + id + '\'' +
                '}';
    }
}
