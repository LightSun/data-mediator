package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.android.data.mediator.ListBinderCallback;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.RecyclerListBindModule;
import com.heaven7.data.mediator.demo.testpackage.StudentModule;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试 绑定RecyclerList.(RecyclerView)
 * Created by heaven7 on 2017/9/26 0026.
 */

public class TestRecyclerListBindActivity extends BaseActivity {

    private static final Random sRan = new Random();

    @BindView(R.id.rv)
    RecyclerView mRv;

    private Binder<RecyclerListBindModule> mBinder;
    private TestRecyclerListAdapter<StudentModule> mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_recycler_list_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initAdapter();
        mBinder = DataMediatorFactory.createBinder(RecyclerListBindModule.class);

        onBindListItems(mBinder);
    }

    //添加一个item
    @OnClick(R.id.bt_add)
    public void onClickAddItem(View v){
        mBinder.getDataProxy().beginStudentsEditor()
                .add(0, createItem());
    }

    //添加一组items
    @OnClick(R.id.bt_add_all)
    public void onClickAddItems(View v){
        List<StudentModule> list = createItems();
        mBinder.getDataProxy().beginStudentsEditor()
                .addAll(list);
    }

    //删除一个 item
    @OnClick(R.id.bt_remove)
    public void onClickRemoveItem(View v){
        mBinder.getDataProxy().beginStudentsEditor()
                .remove(0);
    }

    //替换所有items
    @OnClick(R.id.bt_replace)
    public void onClickReplaceItem(View v){
        mBinder.getDataProxy().setStudents(createItems());
    }

    protected void onBindListItems(Binder<RecyclerListBindModule> mBinder) {
        //通用的绑定方法. 这里用于绑定列表
        mBinder.bind(RecyclerListBindModule.PROP_students.getName(),
                new ListBinderCallback<>(mAdapter));
    }

    protected void initAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mAdapter = new TestRecyclerListAdapter<StudentModule>(
                R.layout.item_test_recycler_list, null) {
            @Override
            protected void onBindData(Context context, int position,
                                      StudentModule item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_age, ""+item.getAge());
            }
        });
    }

    private static StudentModule createItem(){
        StudentModule data = DataMediatorFactory.createData(StudentModule.class);
        data.setAge(sRan.nextInt(10001));
        data.setName("google__" + sRan.nextInt(100));
        return data;
    }
    @NonNull
    private static List<StudentModule> createItems() {
        List<StudentModule> list = new ArrayList<>();
        //just mock data
        final int count = sRan.nextInt(10) + 1;
        for (int i =0 ; i< count ; i++){
            list.add(createItem());
        }
        return list;
    }

    private static abstract class TestRecyclerListAdapter<T extends ISelectable>
             extends QuickRecycleViewAdapter<T> implements
             ListBinderCallback.IItemManager<T>{

        public TestRecyclerListAdapter(int layoutId, List<T> mDatas) {
            super(layoutId, mDatas);
        }

         //===================== sub is IItemManager's methods ===============
         @Override
        public void addItems(List<T> items) {
            getAdapterManager().addItems(items);
        }

        @Override
        public void addItems(int index, List<T> items) {
            getAdapterManager().addItems(index, items);
        }

        @Override
        public void removeItems(List<T> items) {
            getAdapterManager().removeItems(items);
        }

        @Override
        public void replaceItems(List<T> items) {
            getAdapterManager().replaceAllItems(items);
        }

        @Override
        public void onItemChanged(int index, T oldItem, T newItem) {
            getAdapterManager().setItem(index, newItem);
        }
    }
}
