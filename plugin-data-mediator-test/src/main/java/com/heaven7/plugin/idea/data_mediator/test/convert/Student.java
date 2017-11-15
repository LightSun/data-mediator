package com.heaven7.plugin.idea.data_mediator.test.convert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

//auto generate the module of 'data-mediator'(自动生成DataMediator 框架 需要的数据模型 )
public class Student {

    private String name;

    @SerializedName("_id")
    private String id;

    @Expose
    private int age;
    @Since(1.3)
    private int grade;

    @Since(1.2)
    @Until(2.6)
    private String nickName;

}
