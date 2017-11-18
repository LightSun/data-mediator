package com.heaven7.data.mediator.data_binding_test.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.data_binding_test.R;
import com.heaven7.data.mediator.data_binding_test.module.TextViewBind;
import com.heaven7.data.mediator.data_binding_test.util.ResHelper;
import com.heaven7.java.data.mediator.BindMethodSupplier;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.PropertyInterceptor;
import com.heaven7.java.data.mediator.bind.BindAny;
import com.heaven7.java.data.mediator.bind.BindMethodSupplierClass;
import com.heaven7.java.data.mediator.bind.BindsAny;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 第二种方式： 绑定一组属性到view
 * Created by heaven7 on 2017/11/13 0013.
 */
@BindMethodSupplierClass(BindMethodSupplier.DefaultBindMethodSupplier2.class)
public class TestBindArrayPropertyToOneView2 extends BaseActivity {

    //bind array properties to a TextView.
    //relative to @BindsTextView , @BindsAny has greater freedom(翻译: BindsAny注解有更大的自由度).
    // but it must use with @BindMethodSupplierClass.(翻译： 但是他必须搭配注解BindMethodSupplierClass.)
    @BindView(R.id.tv)
    @BindsAny(value = {"textSizeRes", "textRes"},    //any count you want
            methods = {"bindTextSizeRes", "bindTextRes"})
    @BindAny(value = "textColorRes", method = "bindTextColorRes") //only one property
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
                .bind(data, 0, PropertyInterceptor.NULL);
    }

    @OnClick(R.id.bt_text_color)
    public void onClickChangeTextColorRes(View v) {
        mBinder.getDataProxy().setTextColorRes(mHelper.toggleTextColorRes());
    }

    @OnClick(R.id.bt_text_size)
    public void onClickChangeTextSizeRes(View v) {
        mBinder.getDataProxy().setTextSizeRes(mHelper.toggleTextSizeRes());
    }

    @OnClick(R.id.bt_text)
    public void onClickChangeTextRes(View v) {
        mBinder.getDataProxy().setTextRes(mHelper.toggleTextRes());
    }

}
