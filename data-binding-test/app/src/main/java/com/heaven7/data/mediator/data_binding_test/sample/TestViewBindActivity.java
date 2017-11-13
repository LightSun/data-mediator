package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.ViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ResHelper;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindBackground;
import com.heaven7.java.data.mediator.bind.BindBackgroundColor;
import com.heaven7.java.data.mediator.bind.BindBackgroundRes;
import com.heaven7.java.data.mediator.bind.BindEnable;
import com.heaven7.java.data.mediator.bind.BindVisibility;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * test data-binding of view: setBackground, setBackgroundColor,setBackgroundResource.setEnable . etc.
 * Created by heaven7 on 2017/9/24.
 */
public class TestViewBindActivity extends BaseActivity {

    @BindView(R.id.v_enable)@BindEnable("enable")
    View mV_enable;

    @BindView(R.id.v_bg) @BindBackground("background")
    View mV_bg;

    @BindView(R.id.v_bg_color)@BindBackgroundColor("backgroundColor")
    View mV_bg_color;

    @BindView(R.id.v_bg_res)@BindBackgroundRes("backgroundRes")
    View mV_bg_res;

    @BindView(R.id.v_visibility)@BindVisibility("visible")
    View mV_visibility;

    private ResHelper mHelper = new ResHelper();
    private Binder<ViewBind> binder;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_view_bind;
    }
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mHelper.init(context);

        final ViewBind data = DataMediatorFactory.createData(ViewBind.class);
        //bind data.
        binder = DataMediatorFactory.bind(this, data);
    }

    @OnClick(R.id.bt_change_bg)
    public void onClickChangeBg(View v){
        //改变背景（drawable）
        binder.getDataProxy().setBackground(mHelper.toggleDrawable());
    }

    @OnClick(R.id.bt_change_bg_color)
    public void onClickChangeBgColor(View v){
        //改变背景（color）
        binder.getDataProxy().setBackgroundColor(mHelper.toggleColor());
    }

    @OnClick(R.id.bt_change_bg_res)
    public void onClickChangeBgRes(View v){
        //改变背景（resource id）
        binder.getDataProxy().setBackgroundRes(mHelper.toggleDrawableRes());
    }

    @OnClick(R.id.bt_change_enable)
    public void onClickChangeEnable(View v){
        //改变enable 状态
        binder.getDataProxy().setEnable(!binder.getData().isEnable());
    }
    @OnClick(R.id.bt_change_visibility)
    public void onClickChangeVisibility(View v){
        binder.getDataProxy().setVisible(!binder.getDataProxy().isVisible());
    }

    @Override
    protected void onDestroy() {
        binder.unbindAll();
        super.onDestroy();
    }

}
