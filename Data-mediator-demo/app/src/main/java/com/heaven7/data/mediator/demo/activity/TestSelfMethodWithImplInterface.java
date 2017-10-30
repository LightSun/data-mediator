package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.module.TestSelfMethod;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * test self method or impl self interface
 * Created by heaven7 on 2017/10/30 0030.
 */

public class TestSelfMethodWithImplInterface extends BaseActivity {

    @BindView(R.id.textView)
    TextView mTv;

    private TestSelfMethod mProxy;
    @Override
    protected int getLayoutId() {
        return R.layout.ac_self_methods;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        Binder<TestSelfMethod> binder = DataMediatorFactory.createBinder(TestSelfMethod.class);
        //bind property to textView
        binder.bindText(TestSelfMethod.PROP_text, mTv);
        //get proxy
        mProxy = binder.getDataProxy();
    }

    @OnClick(R.id.button)
    public void onClickCallSelf(View view){
        //call self method
        mProxy.changeText("text changed: " + System.currentTimeMillis());
    }

    public interface TextDelegate{
        void changeText(String text);
    }



}
