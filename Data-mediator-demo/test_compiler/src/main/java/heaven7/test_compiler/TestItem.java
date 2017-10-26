package heaven7.test_compiler;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_MAIN_SCOPES_2;
import static com.heaven7.java.data.mediator.FieldFlags.FLAGS_NO_EXPOSE;

/**
 * the item of flow
 * Created by heaven7 on 2017/10/8.
 */

@Fields({
        @Field(propName = "id", type = int.class,  since = 1.2, until = 2.0),
        @Field(propName = "name" ,seriaName = "stu_name"),
        @Field(propName = "desc" , flags = FLAGS_MAIN_SCOPES_2 | FLAGS_NO_EXPOSE),
        @Field(propName = "testItem_test" , flags = FLAGS_MAIN_SCOPES_2 | FLAGS_NO_EXPOSE),
})
public interface TestItem extends DataPools.Poolable {
    Property PROP_id = SharedProperties.get("int", "id", 0);
    Property PROP_name = SharedProperties.get("java.lang.String", "name", 0);
    Property PROP_desc = SharedProperties.get("java.lang.String", "desc", 0);
    Property PROP_testItem_test = SharedProperties.get("java.lang.String", "testItem_test", 0);

    int getId();

    TestItem setId(int id1);

    String getName();

    TestItem setName(String name1);

    String getDesc();

    TestItem setDesc(String desc1);

    String getTestItem_test();

    TestItem setTestItem_test(String testItem_test1);
}
