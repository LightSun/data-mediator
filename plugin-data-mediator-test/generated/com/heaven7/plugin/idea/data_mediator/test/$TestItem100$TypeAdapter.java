package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.support.gson.BaseTypeAdapter;
import com.heaven7.java.data.mediator.support.gson.GsonProperty;

public class $TestItem100$TypeAdapter extends BaseTypeAdapter<TestItem100> {
  public $TestItem100$TypeAdapter() {
     super();
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_4, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_5, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_6, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_selected, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_name, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_xxx1, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_desc, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_xxx4, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_1, "", 1.2, 2.0));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_id, "", 1.2, 2.0));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_2, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem_3, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_xxx5, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_testItem100, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_xxx2, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(TestItem100.PROP_xxx3, "", 1.0, 2.147483647E9));
  }

  protected TestItem100 create() {
    return new TestItem100_$Impl();
  }
}
