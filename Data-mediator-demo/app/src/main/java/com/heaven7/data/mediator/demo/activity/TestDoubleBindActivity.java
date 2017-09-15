package com.heaven7.data.mediator.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.testpackage.StudentModule;
import com.heaven7.data.mediator.demo.util.DoubleBindUtil;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 双向绑定示例程序.
 */
public class TestDoubleBindActivity extends AppCompatActivity {

    @BindView(R.id.tv_desc)
    TextView mTv_desc;

    DataMediator<StudentModule> mMediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //为数据模型创建  中介者。
        mMediator = DataMediatorFactory.createDataMediator(StudentModule.class);
        //双向绑定
        DoubleBindUtil.bindDouble(mMediator, mTv_desc, "name");

        mMediator.getDataProxy().setName("heaven7");
    }

    //从TextView 设置文本, 同事改变数据的属性.
    @OnClick(R.id.bt_set_text_on_TextView)
    public void onClickSetTextOnTextView(View v){
        mTv_desc.setText("set by set_text_on_TextView");
    }

    //从数据代理去设置 数据属性，同时更改绑定的TextView属性
    @OnClick(R.id.bt_set_text_on_mediator)
    public void onClickSetTextOnMediator(View v){
        mMediator.getDataProxy().setName("set_text_on_mediator");
    }

}
