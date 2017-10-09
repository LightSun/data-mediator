package com.heaven7.data.mediator.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.analysis.AnalysisDataModule;
import com.heaven7.data.mediator.demo.analysis.AnalysisManager;
import com.heaven7.data.mediator.demo.module.FlowItemModule;
import com.heaven7.java.data.mediator.ActionMode;
import com.heaven7.java.data.mediator.DataConsumer;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by heaven7 on 2017/10/8.
 */

public class TestListFragment extends BaseFragment {

    public static final String KEY_LIST = "TabItemModule";

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_list1;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(context));

        final ArrayList<FlowItemModule>  datas = getArguments().getParcelableArrayList(KEY_LIST);
        mRv.setAdapter(new QuickRecycleViewAdapter<FlowItemModule>(
          R.layout.item_simple1,   datas ) {
            @Override
            protected void onBindData(Context context, final int position, final FlowItemModule item,
                                      int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.getName())
                        .setText(R.id.tv_desc, item.getDesc())
                        .setRootOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AnalysisManager.getInstance()
                                        .getDataMediator().startActionMode(new ActionMode.Callback<AnalysisDataModule>() {
                                    @Override
                                    public void onPrepareActionMode(ActionMode<AnalysisDataModule> mode) {
                                         mode.getData().setOccurTime(System.currentTimeMillis())
                                                 .setEventType("click")
                                                 .setItem(item)
                                                 .setItemIndex(position);
                                    }
                                }).applyTo();
                            }
                        });
            }
        });
    }
}
