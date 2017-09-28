package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.ViewBindModule;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试 绑定view控件的基本属性: setBackground, setBackgroundColor,setBackgroundResource.setEnable等
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

    //com.heaven7.android.data.mediator.
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {

        initResource(context);

         binder = DataMediatorFactory.createBinder(ViewBindModule.class);
        //初始化属性。
         binder.getDataProxy()
                 .setBackground(mDrawable1)
                 .setBackgroundColor(mColor1)
                 .setBackgroundRes(mResId1)
                 .setEnable(true);
        mUserDrawable1 = true;
        mUserRes1 = true;
        mUserColor1 = true;

        //绑定并 首次应用属性(绑定只需要1次)
         binder.bindBackground("background", mV_bg)
                 .bindBackgroundRes("backgroundRes", mV_bg_res)
                 .bindBackgroundColor("backgroundColor", mV_bg_color)
                 .bindEnable("enable", mV_enable)
                 .applyProperties(PropertyInterceptor.NULL);//应用
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
    }

    @Override
    protected void onDestroy() {
        binder.unbindAll();
        super.onDestroy();
    }

}
