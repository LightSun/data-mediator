package com.heaven7.data.mediator.demo.util;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.heaven7.data.mediator.demo.R;

import org.heaven7.core.view.SlidingTabLayout;

/**
 * Created by heaven7 on 2016/9/5.
 */
public class InternalViewUtil {

    public static void initSlidingTablayout(final SlidingTabLayout mSlidingTabLayout,
                                            final ViewPager vp, boolean splitEqual) {
        initSlidingTablayout(mSlidingTabLayout, vp , splitEqual, null);
    }
    public static void initSlidingTablayout(final SlidingTabLayout mSlidingTabLayout,
                                            final ViewPager vp, boolean splitEqual,
                                            final ViewPager.OnPageChangeListener opl) {
        final Context context = mSlidingTabLayout.getContext();
        mSlidingTabLayout.setDrawBottomUnderLine(true);
        mSlidingTabLayout.setBottomIndicatorHeight(1);
        mSlidingTabLayout.setSelectIndicatorHeight(4);
        if(splitEqual) {
            mSlidingTabLayout.setCustomTabView(R.layout.item_equal_tab, R.id.tv_title);
        }else{
            mSlidingTabLayout.setCustomTabView(R.layout.item_home_tab_title, R.id.tv_title);
        }
        mSlidingTabLayout.setDrawVerticalIndicator(false);
        mSlidingTabLayout.setDrawHorizontalIndicator(true);
        mSlidingTabLayout.setSelectRelativeTextColorsRes(R.color.c_0064ff, R.color.c_8c8c93);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.AbsTabColorizer(context) {
            @Override
            protected int getIndicatorColorRes(int position) {
                return R.color.c_0064ff;
            }
            @Override
            protected int getDividerColorRes(int position) {
                return R.color.c_0064ff;
            }
        });
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSlidingTabLayout.toogleSelect(vp.getCurrentItem());
                if(opl != null){
                    opl.onPageSelected(position);
                }
            }
        });
    }
}
