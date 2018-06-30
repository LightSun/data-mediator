package heaven7.test_compiler;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.FamilyDesc;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2018/6/30 0030.
 */
@Fields(
        value = {
                @Field(propName = "state", type = int.class),
                @Field(propName = "enable", type = boolean.class)
        }
        , families = {
        @FamilyDesc(master = {"state"}, /*slave = {"enable"},*/ connect = ConnectorImpl.class)
})
public interface TestImport extends DataPools.Poolable {

        Property PROP_state = SharedProperties.get(int.class.getName(), "state", 0);
        Property PROP_enable = SharedProperties.get(boolean.class.getName(), "enable", 0);

        TestImport setState(int state1);

        int getState();

        TestImport setEnable(boolean enable1);

        boolean isEnable();
}
