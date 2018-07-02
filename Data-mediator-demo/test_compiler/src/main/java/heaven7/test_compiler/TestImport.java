package heaven7.test_compiler;

import android.content.Context;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.FamilyDesc;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImportDesc;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

/**
 * Created by heaven7 on 2018/6/30 0030.
 */
@Fields(
        value = {
                @Field(propName = "state", type = int.class),
                @Field(propName = "enable", type = boolean.class),
                @Field(propName = "text")
        }
        , families = {
        @FamilyDesc(master = {"state"}, slave = {"enable", "text"}, connect = ConnectorImpl.class)
})
@ImportDesc(names = {"context"}, classes = {ConnectorImpl.class})
public interface TestImport extends DataPools.Poolable {

        Property PROP_state = SharedProperties.get(int.class.getName(), "state", 0);
        Property PROP_enable = SharedProperties.get(boolean.class.getName(), "enable", 0);
        Property PROP_text = SharedProperties.get(String.class.getName(), "text", 0);

        TestImport setState(int state1);

        int getState();

        TestImport setEnable(boolean enable1);

        boolean isEnable();

        TestImport setText(String text1);

        String getText();
}
