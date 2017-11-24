package com.heaven7.test;

import com.heaven7.java.base.util.Objects;
import com.heaven7.java.data.mediator.*;
import com.heaven7.plugin.idea.data_mediator.test.TestItem;
import com.heaven7.plugin.idea.data_mediator.test.TestParcelableData;
import junit.framework.TestCase;

public class TestDataMediatorTree extends TestCase {

    private static final String TAG = "TestDataMediatorTree";

    public void test1(){
        DataMediator<TestItem> root = DataMediatorFactory.createDataMediator(TestItem.class);
        root.getData().setTestItem_6(DataMediatorFactory.createData(TestParcelableData.class));
        root.inflatePropertyChain("testItem_6.test_int");
        final String pName = "test_int";
        DataMediatorCallback<Object> callback = new DataMediatorCallback<Object>() {
            @Override
            public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
                if(!prop.getName().equals(pName)){
                    return;
                }
                LogUtils.log(TAG, "onPropertyValueChanged", data);
                LogUtils.log(TAG, "onPropertyValueChanged", prop);
                LogUtils.log(TAG, "onPropertyValueChanged", oldValue);
                LogUtils.log(TAG, "onPropertyValueChanged", newValue);
                if(getParams() != null) {
                    LogUtils.log(TAG, "onPropertyValueChanged", getParams().mDepth);
                    LogUtils.log(TAG, "onPropertyValueChanged", getParams().mOriginalSource);
                }
            }
            @Override
            public void onPropertyApplied(Object data, Property prop, Object value) {
                if(!prop.getName().equals(pName)){
                    return;
                }
                LogUtils.log(TAG, "onPropertyValueChanged", data);
                LogUtils.log(TAG, "onPropertyValueChanged", prop);
                LogUtils.log(TAG, "onPropertyValueChanged", value);
                if(getParams() != null) {
                    LogUtils.log(TAG, "onPropertyValueChanged", getParams().mDepth);
                    LogUtils.log(TAG, "onPropertyValueChanged", getParams().mOriginalSource);
                }
            }
        };
       // root.addDataMediatorCallback(callback);

        DataMediator<TestParcelableData> dm_child = DataMediatorFactory.createDataMediator(
                root, root.getData().getTestItem_6());
        dm_child.addDataMediatorCallback(callback);
        dm_child.getDataProxy().setTest_int(100);
    }
}
