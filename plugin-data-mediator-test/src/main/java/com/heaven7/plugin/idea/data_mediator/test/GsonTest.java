package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2017/10/29.
 */
@Fields({
        @Field(propName = "id", seriaName = "_id", since = 1.1, until = 1.5),
        @Field(propName = "test", flags = FieldFlags.FLAGS_MAIN_SCOPES_2 &~ FieldFlags.FLAG_GSON_PERSISTENCE)
})
public interface GsonTest extends DataPools.Poolable {

    Property PROP_id = SharedProperties.get(String.class.getName(), "id", 0);
    Property PROP_test = SharedProperties.get(String.class.getName(), "test", 0);

    GsonTest setId(String id1);

    String getId();

    GsonTest setTest(String test1);

    String getTest();
}
