package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.ViewBind;
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

    //com.heaven7.android.data.mediator.
    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {

        initResource(context);
        mUserDrawable1 = true;
        mUserRes1 = true;
        mUserColor1 = true;

         binder = DataMediatorFactory.createBinder(ViewBind.class);
        //初始化属性。
         binder.getDataProxy()
                 .setBackground(mDrawable1)
                 .setBackgroundColor(mColor1)
                 .setBackgroundRes(mResId1)
                 .setEnable(true);


        // 设置属性拦截器，用于应用绑定的时候过滤一些不需要的属性值。
        binder.setPropertyInterceptor(PropertyInterceptor.NULL_AND_ZERO);
        //绑定并 首次应用属性(绑定只需要1次)
         binder
                 .bindBackground(ViewBind.PROP_background, mV_bg)
                         //使用生成的property对象。有助于模型变化后用的地方知晓改变。
                 .bindBackgroundRes(ViewBind.PROP_backgroundRes, mV_bg_res)
                 .bindBackgroundColor(ViewBind.PROP_backgroundColor, mV_bg_color)
                 .bindEnable("enable", mV_enable)
                 .applyProperties(
                         // 创建一个只接收固定属性的 拦截器。(1.1.2支持的). 只是用于本次apply.
                         PropertyInterceptor.createFilter(ViewBind.PROP_background,
                                 ViewBind.PROP_backgroundRes,
                                 ViewBind.PROP_backgroundColor)
                 );
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
