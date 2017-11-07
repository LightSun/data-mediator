package com.heaven7.data.mediator.data_binding_test;

import android.widget.TextView;

import com.heaven7.java.data.mediator.bind.BindText;

import butterknife.BindView;

/**
 * Created by heaven7 on 2017/11/6.
 */
public class MockActivity2 extends MockActivity {

    @BindText("tv_desc")
    TextView mtv_desc;

    public static class TestActivityN extends MockActivity3{

        @BindText("tv_test_n")
        @BindView(R.id.tv)
        TextView mTv_test_N;
    }
}
