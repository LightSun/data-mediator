package com.heaven7.data.mediator.data_binding_test.sample.propertychain;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.core.util.MainWorker;
import com.heaven7.core.util.Toaster;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * sometimes the root module isn't prepared , should wait something done(like http done).
 * bind property chain. with later inflate. <br>
 * 翻译：<br>
 * 有时源数据还没有完全准备好。属性链(property chain)依赖的数据需要 等待其他工作完成。
 * 这时候可以先bind data, 等待数据完成后重新inflate.<br>
 * Created by heaven7 on 2017/11/25.
 */
public class PendingPropertyChainActivity extends BaseActivity {

    //'viewBind' is a property name of RootModule.
    @BindView(R.id.v_enable)@BindEnable("viewBind.enable")
    View mV_enable;

    @BindView(R.id.v_bg) @BindBackground("viewBind.background")
    View mV_bg;

    @BindView(R.id.v_bg_color)@BindBackgroundColor("viewBind.backgroundColor")
    View mV_bg_color;

    @BindView(R.id.v_bg_res)@BindBackgroundRes("viewBind.backgroundRes")
    View mV_bg_res;

    @BindView(R.id.v_visibility)@BindVisibility("viewBind.visible")
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

        //bind data.
        rootBinder = DataMediatorFactory.bind(this, rootModule);
        //mock get data. 翻译： 模拟获取数据
        MainWorker.postDelay(3000, new Runnable() {
            @Override
            public void run() {
                //模拟数据获取
                final ViewBind data = DataMediatorFactory.createData(ViewBind.class);
                rootBinder.getData().setViewBind(data);
                //reinflate (重新inflate property chain.)
                if(rootBinder.getDataMediator().reinflatePropertyChains()){
                    Toaster.show(getApplicationContext(), "reflate property chain success.");
                }
                //attach the inflate callbacks to 'dm_viewBind'. 翻译： 将inflate的callbacks 挂在到 dm_viewBind
                dm_viewBind = DataMediatorFactory.createDataMediator(
                        rootBinder.getDataMediator(), data);
                //now all data prepared.
            }
        });
    }
    @OnClick(R.id.bt_change_bg)
    public void onClickChangeBg(View v){
        //改变背景（drawable）
        if(checkPrepaired()) {
            dm_viewBind.getDataProxy().setBackground(mHelper.toggleDrawable());
        }
    }

    @OnClick(R.id.bt_change_bg_color)
    public void onClickChangeBgColor(View v){
        //改变背景（color）
        if(checkPrepaired()) {
            dm_viewBind.getDataProxy().setBackgroundColor(mHelper.toggleColor());
        }
    }

    @OnClick(R.id.bt_change_bg_res)
    public void onClickChangeBgRes(View v){
        //改变背景（resource id）
        if(checkPrepaired()) {
            dm_viewBind.getDataProxy().setBackgroundRes(mHelper.toggleDrawableRes());
        }
    }

    @OnClick(R.id.bt_change_enable)
    public void onClickChangeEnable(View v){
        //改变enable 状态
        if(checkPrepaired()) {
            dm_viewBind.getDataProxy().setEnable(!dm_viewBind.getData().isEnable());
        }
    }
    @OnClick(R.id.bt_change_visibility)
    public void onClickChangeVisibility(View v){
        if(checkPrepaired()) {
            dm_viewBind.getDataProxy().setVisible(!dm_viewBind.getDataProxy().isVisible());
        }
    }

    private boolean checkPrepaired(){
        if(dm_viewBind == null){
            Toaster.show(getApplicationContext(), "property chain has not inflate. please wait....");
            return false;
        }
        return true;
    }

}
