package com.heaven7.data.mediator.data_binding_test.sample.propertychain;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.RootModule;
import com.heaven7.data.mediator.data_binding_test.module.ViewBind;
import com.heaven7.data.mediator.data_binding_test.sample.BaseActivity;
import com.heaven7.data.mediator.data_binding_test.util.ResHelper;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindBackground;
import com.heaven7.java.data.mediator.bind.BindBackgroundColor;
import com.heaven7.java.data.mediator.bind.BindBackgroundRes;
import com.heaven7.java.data.mediator.bind.BindEnable;
import com.heaven7.java.data.mediator.bind.BindVisibility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * test data-binding of view: setBackground, setBackgroundColor,setBackgroundResource.setEnable . etc.
 * Created by heaven7 on 2017/9/24.
 */
public class ListPropertyChainBindActivity extends BaseActivity {

    //'viewBindList' is a property name of RootModule.
    @BindView(R.id.v_enable)@BindEnable("viewBindList[0].enable")
    View mV_enable;

    @BindView(R.id.v_bg) @BindBackground("viewBindList[0].background")
    View mV_bg;

    @BindView(R.id.v_bg_color)@BindBackgroundColor("viewBindList[0].backgroundColor")
    View mV_bg_color;

    @BindView(R.id.v_bg_res)@BindBackgroundRes("viewBindList[0].backgroundRes")
    View mV_bg_res;

    @BindView(R.id.v_visibility)@BindVisibility("viewBindList[0].visible")
    View mV_visibility;

    private ResHelper mHelper = new ResHelper();
    private Binder<RootModule> rootBinder;
    private DataMediator<ViewBind> dm_viewBind;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_view_bind;
    }
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mHelper.init(context);

        RootModule rootModule = DataMediatorFactory.createData(RootModule.class);
        rootModule.setViewBindList(new ArrayList<ViewBind>());
        rootModule.getViewBindList().add(DataMediatorFactory.createData(ViewBind.class));

        //bind data.
        rootBinder = DataMediatorFactory.bind(this, rootModule);
        dm_viewBind = DataMediatorFactory.createDataMediator(
                rootBinder.getDataMediator(), rootModule.getViewBindList().get(0));
    }

    @OnClick(R.id.bt_change_bg)
    public void onClickChangeBg(View v){
        //改变背景（drawable）
        dm_viewBind.getDataProxy().setBackground(mHelper.toggleDrawable());
    }

    @OnClick(R.id.bt_change_bg_color)
    public void onClickChangeBgColor(View v){
        //改变背景（color）
        dm_viewBind.getDataProxy().setBackgroundColor(mHelper.toggleColor());
    }

    @OnClick(R.id.bt_change_bg_res)
    public void onClickChangeBgRes(View v){
        //改变背景（resource id）
        dm_viewBind.getDataProxy().setBackgroundRes(mHelper.toggleDrawableRes());
    }

    @OnClick(R.id.bt_change_enable)
    public void onClickChangeEnable(View v){
        //改变enable 状态
        dm_viewBind.getDataProxy().setEnable(!dm_viewBind.getData().isEnable());
    }
    @OnClick(R.id.bt_change_visibility)
    public void onClickChangeVisibility(View v){
        dm_viewBind.getDataProxy().setVisible(!dm_viewBind.getDataProxy().isVisible());
    }

}
