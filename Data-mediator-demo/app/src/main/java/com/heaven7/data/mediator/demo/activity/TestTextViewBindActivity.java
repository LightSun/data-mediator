package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.TextViewBind;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试 textView属性的绑定： text， textColor. textSize .
 * Created by heaven7 on 2017/9/25 0025.
 */

public class TestTextViewBindActivity extends BaseActivity {

    @BindView(R.id.tv)
    TextView mTv;

    private final Random mRan = new Random();
    private Binder<TextViewBind> mBinder;
    private TextViewBind mProxy;

    private int[] mTextRess;
    private int[] mColorRess;
    private int[] mTextSizeRess;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_text_view_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initResource(context);
        mBinder = DataMediatorFactory.createBinder(TextViewBind.class);
        //绑定一组属性到TextView
        mBinder.beginBatchTextViewBinder(mTv)
                .bindText(TextViewBind.PROP_text)
                .bindTextRes(TextViewBind.PROP_textRes)
                .bindTextColor(TextViewBind.PROP_textColor)
                .bindTextColorRes("textColorRes")
                .bindTextSize("textSize")
                .bindTextSizeRes("textSizeRes")
                .end();
        mProxy = mBinder.getDataProxy();
    }

    @Override
    protected void onDestroy() {
        mBinder.unbindAll();
        super.onDestroy();
    }

    @OnClick(R.id.bt_text)
    public void onClickChangeText(View v){
        //改变文本
        mProxy.setText(getString(mTextRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_res)
    public void onClickChangeTextRes(View v){
        //改变文本---通过资源id
        mProxy.setTextRes(mTextRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_color)
    public void onClickChangeTextColor(View v){
        //改变文本颜色
        mProxy.setTextColor(getResources().getColor(mColorRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_color_res)
    public void onClickChangeTextColorRes(View v){
        //改变文本颜色---通过资源id
        mProxy.setTextColorRes(mColorRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_size)
    public void onClickChangeTextSize(View v){
        //改变文本大小
        mProxy.setTextSize(getResources().getDimensionPixelSize(mTextSizeRess[mRan.nextInt(5)]));
    }

    @OnClick(R.id.bt_text_size_res)
    public void onClickChangeTextSizeRes(View v){
        //改变文本大小---通过资源id
        mProxy.setTextSizeRes(mTextSizeRess[mRan.nextInt(5)]);
    }

    private void initResource(Context context) {
        mTextRess = new int[]{
                R.string.text_1,
                R.string.text_2,
                R.string.text_3,
                R.string.text_4,
                R.string.text_5,
        };
        mColorRess = new int []{
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                android.R.color.black,
                android.R.color.holo_red_light,
        };
        mTextSizeRess = new int []{
                R.dimen.size_15,
                R.dimen.size_20,
                R.dimen.size_25,
                R.dimen.size_30,
                R.dimen.size_35,
        };
    }


}
