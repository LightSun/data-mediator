package com.heaven7.data.mediator.data_binding_test;

import com.heaven7.data.mediator.data_binding_test.sample.TestViewBindActivity;

import java.util.List;

/**
 * Created by heaven7 on 2017/11/6.
 */

public class EntryActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(TestViewBindActivity.class));
    }
}
