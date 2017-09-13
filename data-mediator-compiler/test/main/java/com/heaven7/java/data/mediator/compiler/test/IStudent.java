package com.heaven7.java.data.mediator.compiler.test;

import com.heaven7.java.data.mediator.ICopyable;
import com.heaven7.java.data.mediator.IResetable;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public interface IStudent extends ICopyable, IResetable{
    int getAge();

    void setAge(int age);

    String getName();

    void setName(String name);

    String getId();

    void setId(String id);
}
