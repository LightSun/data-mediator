package com.heaven7.test;

import com.heaven7.java.data.mediator.support.gson.BaseTypeAdapter;
import com.heaven7.java.data.mediator.support.gson.GsonProperty;

public class $TestItem3$TypeAdapter extends BaseTypeAdapter<TestItem3> {
  public $TestItem3$TypeAdapter() {
     super();
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_2, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_3, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_6, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_4, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem3, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_selected, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_name, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_xxx1, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_desc, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_xxx4, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_id, "", 1.2, 2.0));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_xxx5, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_5, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_xxx2, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_testItem2_1, "", 1.2, 2.0));
     this.addGsonProperty(GsonProperty.of(TestItem3.PROP_xxx3, "", 1.0, 2.147483647E9));
  }

  protected TestItem3 create() {
    return new TestItem3_$Impl();
  }
}
