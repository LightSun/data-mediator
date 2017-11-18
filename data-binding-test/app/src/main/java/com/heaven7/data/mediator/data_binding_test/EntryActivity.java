package com.heaven7.data.mediator.data_binding_test;

import com.heaven7.data.mediator.data_binding_test.sample.TestBindArrayPropertyToOneView;
import com.heaven7.data.mediator.data_binding_test.sample.TestBindArrayPropertyToOneView2;
import com.heaven7.data.mediator.data_binding_test.sample.TestImageViewBindActivity;
import com.heaven7.data.mediator.data_binding_test.sample.TestSelfBinderActivity;
import com.heaven7.data.mediator.data_binding_test.sample.TestSelfBinderFactory;
import com.heaven7.data.mediator.data_binding_test.sample.TestViewBindActivity;
import com.heaven7.data.mediator.data_binding_test.sample.adapter.DataBindingAdapterWithHeader;
import com.heaven7.data.mediator.data_binding_test.sample.adapter.TestDatabindingAdapter;

import java.util.List;

/**
 * Created by heaven7 on 2017/11/6.
 */

public class EntryActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(TestViewBindActivity.class));
        list.add(new ActivityInfo(TestImageViewBindActivity.class));
        list.add(new ActivityInfo(TestSelfBinderActivity.class));
        list.add(new ActivityInfo(TestSelfBinderFactory.class));
        list.add(new ActivityInfo(TestBindArrayPropertyToOneView.class));
        list.add(new ActivityInfo(TestBindArrayPropertyToOneView2.class));

        list.add(new ActivityInfo(TestDatabindingAdapter.class));
        list.add(new ActivityInfo(DataBindingAdapterWithHeader.class));
    }
}
