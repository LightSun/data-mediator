package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediatorFactory;
import com.heaven7.java.data.mediator.DataPools;
import com.heaven7.java.data.mediator.Field;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.ImplMethod;
import com.heaven7.java.data.mediator.Property;
import com.heaven7.java.data.mediator.internal.SharedProperties;

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

    /**
     * test self method /impl interface
     */
    @Fields({
            @Field(propName = "text")
    })
    public interface TestSelfMethod extends TestSelfMethodWithImplInterface.TextDelegate, DataPools.Poolable {

        Property PROP_text = SharedProperties.get("java.lang.String", "text", 0);

        @ImplMethod(from = HelpUtil.class)
        void changeText(String text);

        TestSelfMethod setText(String text1);

        String getText();/*
================== start methods from super properties ===============
======================================================================= */
        class HelpUtil {
            //compare to  ' void changeText(String text);' , just add a module param at the first.
            public static void changeText(TestSelfMethod module, String text) {
                //just mock text change.
                //module can be real data or data proxy, if is proxy it will auto dispatch text change event.
                module.setText(text);
            }

        }

    }

}
