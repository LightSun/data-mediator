package com.heaven7.test;

import com.heaven7.java.data.mediator.*;
import com.heaven7.plugin.idea.data_mediator.test.FlowItem;
import org.junit.Test;

/**
 * Created by heaven7 on 2017/10/29.
 */
public class TestListPropertyChange {

    private DataMediator<FlowItem> mDm_flowitem;

    public TestListPropertyChange(){
        mDm_flowitem = DataMediatorFactory.createDataMediator(FlowItem.class);
        mDm_flowitem.addDataMediatorCallback(DataMediatorCallback.create(
                FlowItem.PROP_desc.getName(), new ListPropertyCallbackImpl()));
    }

    @Test
    public void testListPropertyChange() {
        mDm_flowitem.getDataProxy().beginDescEditor()
                .add("heaven7")
                .add(1, "google")
                .set(0, "facebook")
                .remove(1)
                .end();
    }
    private static class ListPropertyCallbackImpl implements ListPropertyCallback<FlowItem>{

        @Override
        public void onAddPropertyValues(FlowItem data, Property prop, Object newValue, Object addedValue) {
             //called on add
            //all value is List type
        }
        @Override
        public void onAddPropertyValuesWithIndex(FlowItem data, Property prop,
                                                 Object newValue, Object addedValue, int index) {
             //called on add with index
            //all value is List type
        }

        @Override
        public void onRemovePropertyValues(FlowItem data, Property prop,
                                           Object newValue, Object removeValue) {
             //called on remove
            //all value is List type
        }

        @Override
        public void onPropertyItemChanged(FlowItem data, Property prop,
                                          Object oldItem, Object newItem, int index) {
            //called on remove
            //oldItem and new item is a  element of list
        }

        @Override
        public void onPropertyValueChanged(FlowItem data, Property prop,
                                           Object oldValue, Object newValue) {
            //all value is List type, may be null
        }

        @Override
        public void onPropertyApplied(FlowItem data, Property prop, Object value) {
            //all value is List type, may be null
        }
    }

}
