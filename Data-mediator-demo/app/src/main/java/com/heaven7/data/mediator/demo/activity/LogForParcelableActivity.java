package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.testpackage.ClassBind;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用于显示传递过来的parcelable数据
 * Created by heaven7 on 2017/9/19 0019.
 */

public class LogForParcelableActivity extends BaseActivity {

    @BindView(R.id.bt_set_text_on_TextView)
    Button mBt_changeProperty;
    @BindView(R.id.bt_set_text_on_mediator)
    Button mBt_temp;

    @BindView(R.id.tv_desc)
    TextView mTv_desc;

    private ClassBind mModule;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_double_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mBt_temp.setVisibility(View.GONE);
        mBt_changeProperty.setText("click to back");

        mModule = getIntent().getParcelableExtra(TestParcelableDataActivity.KEY_DATA);
        mTv_desc.setText(mModule.toString());
    }

    @OnClick(R.id.bt_set_text_on_TextView)
    public void onClickBack(View v){
        finish();
    }

    @Override
    protected void onDestroy() {
        mModule.recycle();
        super.onDestroy();
    }
}
