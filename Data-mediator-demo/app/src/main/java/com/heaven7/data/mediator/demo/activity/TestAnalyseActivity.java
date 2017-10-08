package com.heaven7.data.mediator.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.heaven7.adapter.BaseFragmentPagerAdapter;
import com.heaven7.core.util.BundleHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.data.mediator.demo.R;
import com.heaven7.data.mediator.demo.analysis.AnalysisDataModule;
import com.heaven7.data.mediator.demo.analysis.AnalysisManager;
import com.heaven7.data.mediator.demo.fragment.TestListFragment;
import com.heaven7.data.mediator.demo.module.FlowItemModule;
import com.heaven7.data.mediator.demo.util.InternalViewUtil;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorFactory;

import org.heaven7.core.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 一个相对贴近实际应用统计的demo.
 * Created by heaven7 on 2017/10/8.
 */

public class TestAnalyseActivity extends BaseActivity {

    private static final String TAG = "TestAnalyseActivity";
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;

    @BindView(R.id.vg)
    ViewPager mVg;

    private DataMediator<AnalysisDataModule> mDm;
    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_analyse;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        mDm = AnalysisManager.getInstance().getDataMediator();
        mDm.getData().setEnterTime(System.currentTimeMillis());

        InternalViewUtil.initSlidingTablayout(mSlidingTabLayout,
                mVg, true, new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageSelected(int position) {
                        mDm.getData()
                                .setTabIndex(position);
                    }
                });
        initViewPager();
    }
    @Override
    protected void onDestroy() {
        mDm.getData().setExitTime(System.currentTimeMillis());
        AnalysisManager.getInstance().handleAnalyseData();
        super.onDestroy();
    }

    public void initViewPager(){
        final BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(
                getSupportFragmentManager(), createTabDatas());
        adapter.setPageChangeListener(new PageChangeListenerImpl());
        mVg.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mVg);
    }

    private List< BaseFragmentPagerAdapter.FragmentData> createTabDatas(){
        List< BaseFragmentPagerAdapter.FragmentData> list = new ArrayList<>();
        for(int i = 0 ; i < 4; i++){
            BaseFragmentPagerAdapter.FragmentData fd = new BaseFragmentPagerAdapter.FragmentData(
                    "tab_" + i, TestListFragment.class, createItems(i));
            list.add(fd);
        }
        return list;
    }

    private Bundle createItems(int tabIndex) {
        ArrayList<FlowItemModule> list = new ArrayList<>();
        for(int i = 0 ; i< 20 ; i++){
            list.add(DataMediatorFactory.createData(FlowItemModule.class)
              .setId(i).setName("name__" + i).setDesc("desc_________" + i));
        }
        return new BundleHelper()
                .putParcelableArrayList(TestListFragment.KEY_LIST, list)
                .getBundle();
    }

    private class PageChangeListenerImpl extends BaseFragmentPagerAdapter.SimplePageChangeListener {
        @Override
        public void onPositionChanged(BaseFragmentPagerAdapter adapter, BaseFragmentPagerAdapter.FragmentData data, int oldPos, int newPos) {
            Logger.i(TAG, "onPositionChanged" + data);
            final List<TextView> textViews = mSlidingTabLayout.getTitleTextViews();
            textViews.get(newPos).setText(data.title);
        }
    }
}
