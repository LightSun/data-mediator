package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heaven7.android.data.mediator.adapter.AdapterItemManager;
import com.heaven7.android.data.mediator.adapter.DataBindingRecyclerAdapter;
import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.TestGroupProperty;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * test group property.
 * Created by heaven7 on 2018/6/28 0028.
 */
public class TestGroupPropertyActivity extends BaseActivity {

    private static final String TAG = "TD adapter";
    //private static final Random sRan = new Random();

    @BindView(R.id.rv)
    RecyclerView mRv;

    private InternalAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_databinding_adapter;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(context));
        mRv.setAdapter(mAdapter = new InternalAdapter(createItems()));
    }

    //random remove item
    @OnClick(R.id.bt_remove)
    public void onClickRemove(View v){
        AdapterItemManager<TestGroupProperty> manager = mAdapter.getItemManager();
        int itemCount = manager.getItemSize();
        final int index = new Random().nextInt(itemCount - 1);
        Logger.i(TAG, "onClickRemove","index = " + index);
        manager.removeItemAt(index);
    }
    @OnClick(R.id.bt_add)
    public void onClickAdd(View v){
        AdapterItemManager<TestGroupProperty> manager = mAdapter.getItemManager();
        int itemCount = manager.getItemSize();
        final int index = new Random().nextInt(itemCount - 1);
        Logger.i(TAG, "onClickAdd","index = " + index);
        manager.addItem(index, createItem());
    }

    public static List<TestGroupProperty> createItems() {
        List<TestGroupProperty> list = new ArrayList<>();
        //just mock data
        final int count = 20;
        for (int i =0 ; i< count ; i++){
            list.add(createItem());
        }
        return list;
    }
    public static TestGroupProperty createItem(){
        TestGroupProperty data = DataMediatorFactory.createData(TestGroupProperty.class);
        data.setState(0);
        return data;
    }

    public static class InternalAdapter extends DataBindingRecyclerAdapter<TestGroupProperty> {

        public InternalAdapter(List<TestGroupProperty> mDatas) {
            super(mDatas, true);
        }
        @Override
        public DataBindingViewHolder<TestGroupProperty> onCreateViewHolderImpl(ViewGroup parent, int layoutId) {
            return new InnerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(layoutId, parent, false));
        }
        @Override
        protected int getItemLayoutId(int position, TestGroupProperty student) {
            return R.layout.item_test_recycler_list;
        }
    }
    public static class InnerViewHolder extends DataBindingRecyclerAdapter.DataBindingViewHolder<TestGroupProperty>{

        @BindView(R.id.tv_name) @BindText("state2")
        TextView mTv_name;

        @BindView(R.id.tv_age) @BindText("state")
        TextView mTv_age;

        public InnerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onPreCreateDataBinding(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tv_name)
        public void onClickName(View v){
            //here just toast
            Toast.makeText(v.getContext(), "onClickName is called, pos = "
                    + getAdapterPosition2(), Toast.LENGTH_SHORT).show();
            getDataProxy().setState2(true);
        }
        @OnClick(R.id.tv_age)
        public void onClickAge(View v){
            //here just toast
            Toast.makeText(v.getContext(), "pos setState to 1, pos = "
                    + getAdapterPosition2(), Toast.LENGTH_SHORT).show();
            getDataProxy().setState(1);
        }
    }

}
