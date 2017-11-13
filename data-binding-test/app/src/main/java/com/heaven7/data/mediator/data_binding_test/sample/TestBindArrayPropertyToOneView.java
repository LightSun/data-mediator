package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.TextViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ResHelper;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;
import com.heaven7.java.data.mediator.bind.BindsTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定一组属性到view
 * Created by heaven7 on 2017/11/13 0013.
 */
public class TestBindArrayPropertyToOneView extends BaseActivity {


    //BindsTextView bind array properties to a TextView.
    @BindView(R.id.tv) @BindsTextView( {"textColorRes", "textSizeRes", "textRes"})
    TextView mTv;

    private ResHelper mHelper = new ResHelper();
    private Binder<TextViewBind> mBinder;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bind_array_prop_to_view;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mHelper.init(context);

        TextViewBind data = DataMediatorFactory.createData(TextViewBind.class);
        mBinder = DataMediatorFactory.createDataBinding(this)
                .bind( data, 0, PropertyInterceptor.NULL);
    }

    @OnClick(R.id.bt_text_color)
    public void onClickChangeTextColorRes(View v){
        mBinder.getDataProxy().setTextColorRes(mHelper.toggleTextColorRes());
    }

    @OnClick(R.id.bt_text_size)
    public void onClickChangeTextSizeRes(View v){
        mBinder.getDataProxy().setTextSizeRes(mHelper.toggleTextSizeRes());
    }

    @OnClick(R.id.bt_text)
    public void onClickChangeTextRes(View v){
        mBinder.getDataProxy().setTextRes(mHelper.toggleTextRes());
    }

}
