package com.heaven7.plugin.idea.data_mediator.test;

import com.heaven7.java.data.mediator.support.gson.BaseTypeAdapter;
import com.heaven7.java.data.mediator.support.gson.GsonProperty;

public class $FlowItem$TypeAdapter extends BaseTypeAdapter<FlowItem> {
  public $FlowItem$TypeAdapter() {
     super();
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_xxx1, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_desc, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_xxx4, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_id, "", 1.2, 2.0));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_xxx5, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_xxx2, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_selected, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_name, "", 1.0, 2.147483647E9));
     this.addGsonProperty(GsonProperty.of(FlowItem.PROP_xxx3, "", 1.0, 2.147483647E9));
  }

  protected FlowItem create() {
    return new FlowItem_$Impl();
  }
}
