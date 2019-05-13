package com.byt.eem.act;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.frag.FragHome;
import com.byt.eem.frag.FragMine;
import com.byt.eem.frag.FragMsg;
import com.byt.eem.frag.FragReport;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.MCheckBox;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActMain extends BaseAct {

    @BindView(R.id.layout_navigation)
    LinearLayout layoutNavigation;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private FragHome mFragHome;
    private FragMsg mFragMsg;
    private FragReport mFragReport;
    private FragMine mFragMine;

    private long exitTime;
    private int preNaviIndex = 0;  //上一个获得焦点的导航
    private boolean bClick;

    @Override
    protected int setupViewRes() {
        return R.layout.act_main;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        mFragHome = new FragHome();
        mFragMsg = new FragMsg();
        mFragReport = new FragReport();
        mFragMine = new FragMine();
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mFragHome;
                    case 1:
                        return mFragMsg;
                    case 2:
                        return mFragReport;
                    default:
                        return mFragMine;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (bClick) bClick = false;
                else hanldeOnNaviClick(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < layoutNavigation.getChildCount(); i++) {
            View child = layoutNavigation.getChildAt(i);
            child.setTag(i);
            int finalI = i;
            child.setOnClickListener(v -> hanldeOnNaviClick(finalI));
            if (i == 0) {
                ((MCheckBox) child).doFocus();
            }
        }

        new Thread(()-> checkUpdate()).start();

    }

    class VersionInfo extends BaseModel{

    }

    private void checkUpdate(){
        Post(null, MConstants.URL.CHECK_UPDATE, VersionInfo.class, new IHttpCallBack<VersionInfo>() {
            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList<VersionInfo> data) {

            }

            @Override
            public void OnFailure(String msg) {
                LogUtil.e("检查更新失败:"+msg);
            }
        });
    }

    private void hanldeOnNaviClick(int naviIndex) {
        if (preNaviIndex == naviIndex) return;
        bClick = true;
        if (preNaviIndex == 0) {
            mFragHome.pauseFlipper();
        }
        if (naviIndex == 0) {
            mFragHome.resumeFlipper();
        }
        viewpager.setCurrentItem(naviIndex);

        ((MCheckBox) layoutNavigation.getChildAt(preNaviIndex)).doBlur();
        ((MCheckBox) layoutNavigation.getChildAt(naviIndex)).doFocus();
        preNaviIndex = naviIndex;
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
