package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.testpackage.StudentModule;
import com.heaven7.data.mediator.demo.testpackage.TestBindModule;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorCallback;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.SparseArrayPropertyCallback;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试 SparseArray属性。
 * Created by heaven7 on 2017/10/6.
 */

public class TestSparseArrayActivity extends BaseActivity {

    private static final String TAG = "TestSparseArray";
    @BindView(R.id.tv_sa)
    TextView mTv_sa;

    private DataMediator<TestBindModule> mDm;
    private Set<Integer> mIndexes = new HashSet<>();

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_sparse_array;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mDm = DataMediatorFactory.createDataMediator(TestBindModule.class);
        // 这里直接用属性回调。也可以用binder.bind(String property, SparseArrayPropertyCallback<? super T> callback)方法
        mDm.addDataMediatorCallback(DataMediatorCallback.createForSparse(
                TestBindModule.PROP_cityData2.getName(), new CallbackImpl()));

    }

    // put 操作
    @OnClick(R.id.bt_put)
    public void onClickPut(View v){
        final StudentModule stu = createStu(-1);
        mDm.getDataProxy().beginCityData2Editor()
                .put((int)stu.getId(), stu)
                .end();
    }

    // 移除操作(通过key)
    @OnClick(R.id.bt_remove_key)
    public void onClickRemoveByKey(View v){
        if(!mIndexes.isEmpty()){
            final Integer index = mIndexes.iterator().next();
            mDm.getDataProxy().beginCityData2Editor()
                    .remove(index);
            mIndexes.remove(index);
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickRemoveByKey", "already empty");
        }
    }

    // 移除操作(通过value)
    @OnClick(R.id.bt_remove_value)
    public void onClickRemoveByValue(View v){
        if(!mIndexes.isEmpty()){
            final Integer index = mIndexes.iterator().next();
            mDm.getDataProxy().beginCityData2Editor()
                    .removeByValue(createStu(index));
            mIndexes.remove(index);
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickRemoveByValue", "already empty");
        }
    }

    //清空操作
    @OnClick(R.id.bt_clear)
    public void onClickClear(View v){
        if(!mIndexes.isEmpty()){
            mDm.getDataProxy().beginCityData2Editor().clear();
            mIndexes.clear();
        }else{
            mTv_sa.setText("");
            Logger.w(TAG , "onClickClear", "already empty");
        }
    }
    private StudentModule createStu(int index) {
        if(index < 0){
            index = new Random().nextInt(5);
        }
        mIndexes.add(index);
        return DataMediatorFactory.createData(StudentModule.class)
                .setId(index).setName("google_" + index).setAge(index);
    }

    private void setLogText(String method, String msg){
        mTv_sa.setText(method + ": \n  " + msg + "\n\n now is: \n"
                + mDm.getData().getCityData2().toString());
    }

    private class CallbackImpl implements SparseArrayPropertyCallback<TestBindModule>{

        @Override
        public void onEntryValueChanged(TestBindModule data, Property prop, Integer key,
                                        Object oldValue, Object newValue) {
            final String msg = "oldValue = " + oldValue + " ,newValue = " + newValue;
            Logger.i(TAG , "onEntryValueChanged", msg);
            setLogText("onEntryValueChanged", msg);
        }
        @Override
        public void onAddEntry(TestBindModule data, Property prop, Integer key, Object value) {
            final String msg = "key = " + key + " ,value = " + value;
            Logger.i(TAG , "onAddEntry", msg);
            setLogText("onAddEntry", msg);
        }
        @Override
        public void onRemoveEntry(TestBindModule data, Property prop, Integer key, Object value) {
            final String msg = "key = " + key + " ,value = " + value;
            Logger.i(TAG , "onRemoveEntry", msg);
            setLogText("onRemoveEntry", msg);
        }
        @Override
        public void onClearEntries(TestBindModule data, Property prop, Object entries) {
            final String msg = entries.toString(); //here entries is SparseArray
            Logger.i(TAG , "onClearEntries", msg);
            setLogText("onClearEntries", msg);
        }
        @Override
        public void onPropertyValueChanged(TestBindModule data, Property prop,
                                           Object oldValue, Object newValue) {
            final String msg = "oldValue = " + oldValue + " ,newValue = " + newValue;
            Logger.i(TAG , "onPropertyValueChanged", msg);
            setLogText("onPropertyValueChanged", msg);
        }
        @Override
        public void onPropertyApplied(TestBindModule data, Property prop, Object value) {
            final String msg = "value = " + value;
            Logger.i(TAG , "onPropertyApplied", msg);
            setLogText("onPropertyApplied", msg);
        }
    }
}
