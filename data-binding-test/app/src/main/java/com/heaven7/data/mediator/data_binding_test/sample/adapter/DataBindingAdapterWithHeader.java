package com.heaven7.data.mediator.data_binding_test.sample.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.RecyclerViewUtils;
import com.heaven7.data.mediator.data_binding_test.R;

import butterknife.BindView;

/**
 * data-binding adapter with header.
 * Created by heaven7 on 2017/11/11.
 */

public class DataBindingAdapterWithHeader extends TestDatabindingAdapter {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_databinding_adapter_with_header;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        TestDatabindingAdapter.InternalAdapter adapter =
                new TestDatabindingAdapter.InternalAdapter(createItems());
        GridLayoutManager glp = RecyclerViewUtils.createGridLayoutManager(adapter, context, 2);
        mRv.setLayoutManager(glp);
        adapter.addHeaderView(getLayoutInflater().inflate(R.layout.header_data_binding,
                mRv, false));

        mRv.setAdapter(mAdapter = adapter);
    }
}
