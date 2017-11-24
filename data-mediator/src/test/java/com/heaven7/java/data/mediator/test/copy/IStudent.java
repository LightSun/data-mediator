package com.heaven7.java.data.mediator.test.copy;

import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/9/12 0012.
 */
public interface IStudent {

    Property PROP_name = SharedProperties.get(String.class.getName(), "name", 0);

    String getName();

    void setName(String name);

}
