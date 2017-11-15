package com.heaven7.plugin.idea.data_mediator.test.convert;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

@Fields(value = {
        @Field(propName = "name"),
        @Field(propName = "id", seriaName = "_id"),
        @Field(propName = "age", type = int.class, flags = FieldFlags.FLAGS_MAIN_SCOPES_2 | FieldFlags.FLAG_EXPOSE_DEFAULT),
        @Field(propName = "grade", type = int.class, since = 1.3),
        @Field(propName = "nickName", since = 1.2, until = 2.6)
}, generateJsonAdapter = false)
//this class is convert by convertor plugin
public interface IStudent extends DataPools.Poolable {
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
    Property PROP_id = SharedProperties.get("java.lang.String", "id", 0);
    Property PROP_age = SharedProperties.get("int", "age", 0);
    Property PROP_grade = SharedProperties.get("int", "grade", 0);
    Property PROP_nickName = SharedProperties.get("java.lang.String", "nickName", 0);

    IStudent setName(String name1);

    String getName();

    IStudent setId(String id1);

    String getId();

    IStudent setAge(int age1);

    int getAge();

    IStudent setGrade(int grade1);

    int getGrade();

    IStudent setNickName(String nickName1);

    String getNickName();
}
