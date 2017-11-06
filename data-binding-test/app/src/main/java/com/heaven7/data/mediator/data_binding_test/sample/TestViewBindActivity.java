package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.ViewBind;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.bind.BindBackground;
import com.heaven7.java.data.mediator.bind.BindBackgroundColor;
import com.heaven7.java.data.mediator.bind.BindBackgroundRes;
import com.heaven7.java.data.mediator.bind.BindEnable;

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

    private Binder<ViewBind> binder;

    private Drawable mDrawable1;
    private Drawable mDrawable2;
    private int mResId1;
    private int mResId2;
    private int mColor1;
    private int mColor2;

    private boolean mUserDrawable1;
    private boolean mUserRes1;
    private boolean mUserColor1;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_view_bind;
    }
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initResource(context);

        final ViewBind data = DataMediatorFactory.createData(ViewBind.class);
        //bind data.
        binder = DataMediatorFactory.bind(this, data);
    }

    @OnClick(R.id.bt_change_bg)
    public void onClickChangeBg(View v){
        //改变背景（drawable）
        binder.getDataProxy().setBackground(mUserDrawable1 ? mDrawable2 : mDrawable1);
        mUserDrawable1 = !mUserDrawable1;
    }

    @OnClick(R.id.bt_change_bg_color)
    public void onClickChangeBgColor(View v){
        //改变背景（color）
        binder.getDataProxy().setBackgroundColor(mUserColor1 ? mColor2 : mColor1);
        mUserColor1 = !mUserColor1;
    }

    @OnClick(R.id.bt_change_bg_res)
    public void onClickChangeBgRes(View v){
        //改变背景（resource id）
        binder.getDataProxy().setBackgroundRes(mUserRes1 ? mResId2 : mResId1);
        mUserRes1 = !mUserRes1;
    }

    @OnClick(R.id.bt_change_enable)
    public void onClickChangeEnable(View v){
        //改变enable 状态
        binder.getDataProxy().setEnable(!binder.getData().isEnable());
    }

    private void initResource(Context context) {
        Resources res = context.getResources();
        mDrawable1 = res.getDrawable(R.mipmap.ic_launcher);
        mDrawable2 = res.getDrawable(R.mipmap.ic_launcher_round);
        mResId1 = R.mipmap.ic_launcher;
        mResId2 = R.mipmap.ic_launcher_round;
        mColor1 = Color.RED;
        mColor2 = Color.GREEN;

        mUserDrawable1 = true;
        mUserRes1 = true;
        mUserColor1 = true;
    }

    @Override
    protected void onDestroy() {
        binder.unbindAll();
        super.onDestroy();
    }

}
