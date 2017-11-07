package com.heaven7.data.mediator.data_binding_test;

import android.widget.TextView;

import com.heaven7.java.data.mediator.bind.BindText;

/**
 * just test gap extend class.
 * Created by heaven7 on 2017/11/6.
 */
public class MockActivity4 extends MockActivity3 {

    @BindText("tv_class")
    TextView mtv_class;

    public static class Internal extends MockActivity2.TestActivityN{
        @BindText("tv_internal")
        TextView mtv_internal;
    }
}
