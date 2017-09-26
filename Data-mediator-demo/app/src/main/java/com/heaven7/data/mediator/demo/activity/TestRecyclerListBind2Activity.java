package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.RecyclerListBindModule;
import com.heaven7.data.mediator.demo.testpackage.StudentModule;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.Property;

import java.util.List;

/**
 * 测试 绑定RecyclerList2.(RecyclerView)
 * Created by heaven7 on 2017/9/26 0026.
 */

public class TestRecyclerListBind2Activity extends TestRecyclerListBindActivity {

    private InternalAdapater<StudentModule> mAdapter;

    @Override
    protected void onBindListItems(Binder<RecyclerListBindModule> mBinder) {
        //默认方式 (需要现在RecyclerView 上设置adapter)
        mBinder.bindRecyclerList(RecyclerListBindModule.PROP_students.getName(), mRv);
    }

    @Override
    protected void initAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter = new InternalAdapater<StudentModule>(
                R.layout.item_test_recycler_list, null) {
            @Override
            protected void onBindData(Context context, int position,
                                      StudentModule item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_age, ""+item.getAge());
            }
        });
    }

    private static abstract  class InternalAdapater<T extends ISelectable>
            extends QuickRecycleViewAdapter<T> implements Binder.BinderCallback<RecyclerListBindModule>{

        public InternalAdapater(int layoutId, List<T> mDatas) {
            super(layoutId, mDatas);
        }
        @Override
        public Object getTag() {
            return this;
        }
        @Override
        public void onAddPropertyValues(RecyclerListBindModule data, Property prop,
                                        Object newValue, Object addedValue) {
            List<T> items = (List<T>) addedValue;
            getAdapterManager().addItems(items);
        }

        @Override
        public void onAddPropertyValuesWithIndex(RecyclerListBindModule data, Property prop,
                                                 Object newValue, Object addedValue, int index) {
            List<T> items = (List<T>) addedValue;
            getAdapterManager().addItems(index, items);
        }

        @Override
        public void onRemovePropertyValues(RecyclerListBindModule data, Property prop,
                                           Object newValue, Object removeValue) {
            List<T> items = (List<T>) removeValue;
            getAdapterManager().removeItems(items);
        }

        @Override
        public void onPropertyValueChanged(RecyclerListBindModule data, Property prop,
                                           Object oldValue, Object newValue) {
            List<T> items = (List<T>) newValue;
            getAdapterManager().replaceAllItems(items);
        }

        @Override
        public void onPropertyApplied(RecyclerListBindModule data, Property prop, Object value) {
            onPropertyValueChanged(data, prop , null, value);
        }
    }
}
