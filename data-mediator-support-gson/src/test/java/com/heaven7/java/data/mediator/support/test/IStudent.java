package com.heaven7.java.data.mediator.support.test;


import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public interface IStudent{
    int getAge();

    void setAge(int age);

    String getName();

    void setName(String name);

    String getId();

    void setId(String id);

    void setTags(List<String> tags);
    List<String> getTags();
}
