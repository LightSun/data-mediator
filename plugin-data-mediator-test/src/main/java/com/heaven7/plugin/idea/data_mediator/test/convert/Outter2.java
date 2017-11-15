package com.heaven7.plugin.idea.data_mediator.test.convert;



import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;

import java.util.ArrayList;

public class Outter2 {

    public static class Inner2_1 {
        //just test
        public static class Inner2_2 extends ArrayList{
            private String text;
        }

        @Fields(value = {@Field(propName = "text")
        }, generateJsonAdapter = false)
        public interface IInner2_2 {
        }

    }
}
