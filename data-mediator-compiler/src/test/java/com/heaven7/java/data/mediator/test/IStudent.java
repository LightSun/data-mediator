package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.ICopyable;
import com.heaven7.java.data.mediator.IResetable;
import com.heaven7.java.data.mediator.IShareable;
import com.heaven7.java.data.mediator.ISnapable;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public interface IStudent extends ICopyable, IResetable, IShareable, ISnapable{
    int getAge();

    void setAge(int age);

    String getName();

    void setName(String name);

    String getId();

    void setId(String id);
}
