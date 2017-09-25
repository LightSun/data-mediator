package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.ViewBindModule;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2017/9/24.
 */
public class TestViewBindActivity extends BaseActivity {

    @BindView(R.id.v_enable)
    View mV_enable;
    @BindView(R.id.v_bg)
    View mV_bg;
    @BindView(R.id.v_bg_color)
    View mV_bg_color;
    @BindView(R.id.v_bg_res)
    View mV_bg_res;

    private Binder<ViewBindModule> binder;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_view_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        //TODO handle exist property/data?
         binder = DataMediatorFactory.createBinder(ViewBindModule.class);
         binder.bindBackground("background", mV_bg)
                 .bindBackgroundRes("backgroundRes", mV_bg_res)
                 .bindBackgroundColor("backgroundColor", mV_bg_color)
                 .bindEnable("enable", mV_enable);
    }
    @Override
    protected void onDestroy() {
        binder.unbindAll();
        super.onDestroy();
    }

    @OnClick(R.id.bt_change_bg)
    public void onClickChanageBg(View v){
        //改变背景（drawable）

    }
    @OnClick(R.id.bt_change_bg_color)
    public void onClickChanageBgColor(View v){
        //改变背景（color）
    }
    @OnClick(R.id.bt_change_bg_res)
    public void onClickChanageBgRes(View v){
        //改变背景（resource id）

    }

}
