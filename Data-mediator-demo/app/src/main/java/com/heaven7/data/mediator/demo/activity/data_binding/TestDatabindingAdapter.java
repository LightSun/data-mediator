package com.heaven7.data.mediator.demo.activity.data_binding;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heaven7.android.data.mediator.adapter.DataBindingRecyclerAdapter;
import com.heaven7.android.data.mediator.adapter.ItemManager;
import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.activity.BaseActivity;
import com.heaven7.data.mediator.demo.testpackage.Student;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * test data-binding in adapter.
 * Created by heaven7 on 2017/11/9 0009.
 */

public class TestDatabindingAdapter extends BaseActivity {

    private static final String TAG = "TD adapter";
    private static final Random sRan = new Random();

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
        ItemManager<Student> manager = mAdapter.getItemManager();
        int itemCount = manager.getItemSize();
        final int index = new Random().nextInt(itemCount - 1);
        Logger.i(TAG, "onClickRemove","index = " + index);
        manager.removeItemAt(index);
    }
    @OnClick(R.id.bt_add)
    public void onClickAdd(View v){
        ItemManager<Student> manager = mAdapter.getItemManager();
        int itemCount = manager.getItemSize();
      /*  final int index = new Random().nextInt(itemCount - 1);*/
        Logger.i(TAG, "onClickAdd","index = " + 1);
        manager.addItem(1, createItem().setAge(1));
    }

    private static List<Student> createItems() {
        sIndex = 0;
        List<Student> list = new ArrayList<>();
        //just mock data
        final int count = 20;
        for (int i =0 ; i< count ; i++){
            list.add(createItem());
        }
        return list;
    }
    private static Student createItem(){
        Student data = DataMediatorFactory.createData(Student.class);
        data.setAge(sIndex ++ );
        data.setName("google__" + sRan.nextInt(100));
        return data;
    }

    private static int sIndex = 0;

    private static class InternalAdapter extends DataBindingRecyclerAdapter<Student>{

        public InternalAdapter(List<Student> mDatas) {
            super(mDatas, true);
        }
        @Override
        public DataBindingViewHolder<Student> onCreateViewHolderImpl(ViewGroup parent, int layoutId) {
            return new InnerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(layoutId, parent, false));
        }
        @Override
        protected int getItemLayoutId(int position, Student student) {
            return R.layout.item_test_recycler_list;
        }
    }
    public static class InnerViewHolder extends DataBindingRecyclerAdapter.DataBindingViewHolder<Student>{

        @BindView(R.id.tv_name) @BindText("name")
        TextView mTv_name;

        @BindView(R.id.tv_age) @BindText("age")
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
                    + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
        @OnClick(R.id.tv_age)
        public void onClickAge(View v){
            //here just toast
            Toast.makeText(v.getContext(), "onClickAge is called, pos = "
                    + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
        @OnClick(R.id.bt_change_item)
        public void onClickChangeItem(View v){
            getDataProxy()
                    .setAge((int) (System.currentTimeMillis() % 99))
                    .setId(getAdapterPosition())
                    .setName("google+__" + getAdapterPosition());
            //Note: no need notifyItemChanged here.
        }
    }
}
