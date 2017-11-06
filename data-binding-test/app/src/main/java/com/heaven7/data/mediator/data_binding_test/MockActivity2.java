package com.heaven7.data.mediator.data_binding_test;

import android.widget.TextView;

import com.heaven7.java.data.mediator.bind.BindText;

/**
 * Created by heaven7 on 2017/11/6.
 */
public class MockActivity2 extends MockActivity {

    @BindText("tv_desc")
    TextView mtv_desc;
}
