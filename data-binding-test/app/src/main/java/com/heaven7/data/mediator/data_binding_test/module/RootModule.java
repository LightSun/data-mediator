package com.heaven7.data.mediator.data_binding_test.module;

import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.FieldFlags;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ListPropertyEditor;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

import java.util.List;

/**
 * Created by heaven7 on 2017/11/24 0024.
 */

@Fields({
        @Field(propName = "viewBind" , type = ViewBind.class),
        @Field(propName = "viewBindList" , type = ViewBind.class, complexType = FieldFlags.COMPLEX_LIST),
})
public interface RootModule extends DataPools.Poolable {

    Property PROP_viewBind = SharedProperties.get(ViewBind.class.getName(), "viewBind", 0);
    Property PROP_viewBindList = SharedProperties.get(ViewBind.class.getName(), "viewBindList", 2);

    RootModule setViewBind(ViewBind viewBind1);

    ViewBind getViewBind();

    RootModule setViewBindList(List<ViewBind> viewBindList1);

    List<ViewBind> getViewBindList();

    ListPropertyEditor<? extends RootModule, ViewBind> beginViewBindListEditor();/*
================== start methods from super properties ===============
======================================================================= */
}
