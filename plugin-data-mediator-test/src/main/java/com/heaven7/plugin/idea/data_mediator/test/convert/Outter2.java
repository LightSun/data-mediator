package com.heaven7.plugin.idea.data_mediator.test.convert;



import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.ArrayList;

public class Outter2 {

    public static class Inner2_1 {
        //just test
        public static class Inner2_2 extends ArrayList{
            private String text;
        }

        @Fields(value = {@Field(propName = "text")
        }, generateJsonAdapter = false)
        public interface IInner2_2 extends DataPools.Poolable {
            Property PROP_text = SharedProperties.get(String.class.getName(), "text", 0);

            IInner2_2 setText(String text1);

            String getText();
        }

    }
}
