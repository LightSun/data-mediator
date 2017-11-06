package com.heaven7.data.mediator.data_binding_test;

import android.widget.TextView;

import com.heaven7.java.data.mediator.bind.BindText;

/**
 * just test gap extend from other module
 * Created by heaven7 on 2017/11/6.
 */

public class TestMockActivity3 extends TestMockActivity2 {

    @BindText("tv_test_mock3")
    TextView mTv_test_mock3;
}
