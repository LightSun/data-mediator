package com.heaven7.data.mediator.demo;


import com.heaven7.data.mediator.demo.activity.*;

import java.util.List;

/**
 * Created by heaven7 on 2017/7/12 0012.
 */

public class MainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(TestPropertyChangeActivity.class, "TestPropertyChangeActivity"));
        list.add(new ActivityInfo(TestDoubleBindActivity.class, "ToastTestActivity"));
        list.add(new ActivityInfo(TestChainCallActivity.class, "TestChainCallActivity"));
        list.add(new ActivityInfo(TestParcelableDataActivity.class, "TestParcelableDataActivity"));

        list.add(new ActivityInfo(TestViewBindActivity.class, "Test binder View property"));
        list.add(new ActivityInfo(TestTextViewBindActivity.class, "Test binder TextView property"));
        list.add(new ActivityInfo(TestRecyclerListBindActivity.class, "Test binder recycler list property"));
        list.add(new ActivityInfo(TestRecyclerListBind2Activity.class, "Test binder recycler list2"));

    }
}
