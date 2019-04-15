package com.byt.eem.frag;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.byt.eem.R;
import com.byt.eem.act.ActProvinceProjects;
import com.byt.eem.adapter.AdapterHomeProj;
import com.byt.eem.base.MBaseFragment;
import com.byt.eem.model.ODeviceWarn;
import com.byt.eem.model.OHomeProj;
import com.byt.eem.model.PageModel;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragHome extends MBaseFragment {
    @BindView(R.id.tv_onlineEquipCount)
    TextView tvOnlineEquipCount;
    @BindView(R.id.tv_offlineEquipCount)
    TextView tvOfflineEquipCount;
    @BindView(R.id.tv_warningEquipCount)
    TextView tvWarningEquipCount;
    @BindView(R.id.flipper)
    ViewFlipper flipper;
    @BindView(R.id.tv_warningType1)
    TextView tvWarningType1;
    @BindView(R.id.tv_equipName1)
    TextView tvEquipName1;
    @BindView(R.id.tv_equipLoc1)
    TextView tvEquipLoc1;
    @BindView(R.id.tv_warningType2)
    TextView tvWarningType2;
    @BindView(R.id.tv_equipName2)
    TextView tvEquipName2;
    @BindView(R.id.tv_equipLoc2)
    TextView tvEquipLoc2;
    @BindView(R.id.rv_projects)
    RecyclerView rvProjects;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_empty)
    View tvEmpty;

    Unbinder unbinder;
    private List<OHomeProj> mList;
    private AdapterHomeProj mAdapter;


    private PageModel mParam;
    private int pageIndex = 1;
    private List<ODeviceWarn> mListDeviceWarn;
    private int flipperIndex;

    @Override
    public int setupLayoutRes() {
        return R.layout.frag_home;
    }

    @Override
    public void initMain() {
        unbinder = ButterKnife.bind(this, _rootView);
        mList = new ArrayList<>();
        mAdapter = new AdapterHomeProj(mBaseActivity, mList, position -> {
            OHomeProj model = mList.get(position);
            NEXT(new Intent(mBaseActivity, ActProvinceProjects.class)
                    .putExtra("str", model.getProvinceName())
                    .putExtra("id", model.getProvinceId()));
        });
        rvProjects.setAdapter(mAdapter);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> getData());
        flipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                flipperIndex++;
                initDeviceWarnParam();
//                如果没加判断，则任一通知的动画都会被监听

//                View currentView = flipper.getCurrentView();
//                final TextView textView = currentView.findViewById(R.id.tv_notice);
//                flipper.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                        intent.putExtra("message", textView.getText());
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getData();
    }

    private void getData() {
        getDeviceStateCount();
    }


    //在线、离线、报警设备数
    private void getDeviceStateCount() {
        Post(MConstants.URL.GET_DEVICE_STATE_COUNT, HomeData.class, new IHttpCallBack<HomeData>() {

            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList<HomeData> data) {
            if (data.size() > 0) {
                for (HomeData homeData : data) {
                    setupWarningParams(homeData);
                }
                getDeviceWarnByRealTime();
            }
            }

            @Override
            public void OnFailure(String msg) {
                showToast(msg);
            }
        });
    }

    private void setupWarningParams(HomeData homeData) {
        if (homeData.State.contains("在线")) {
            tvOnlineEquipCount.setText(homeData.Counts);
        } else if (homeData.State.contains("离线")) {
            tvOfflineEquipCount.setText(homeData.Counts);
        } else {
            tvWarningEquipCount.setText(homeData.Counts);
        }
    }

    //实时告警
    private void getDeviceWarnByRealTime() {
        if (mParam == null) mParam = new PageModel(pageIndex);
        Post(MConstants.URL.GET_DEVICE_WARN_BY_REAL_TIME, HttpUtil.formatParams(mParam.toString()),
                ODeviceWarn.class, new IHttpCallBack<ODeviceWarn>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<ODeviceWarn> data) {
                        if (mListDeviceWarn == null) mListDeviceWarn = new ArrayList<>();
                        else mListDeviceWarn.clear();
                        if (data.size() > 0) {
                            mListDeviceWarn.addAll(data);
                        }
                        flipperIndex = 0;
                        initDeviceWarn();
                        getProjectsGroupByProvince();
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

    private void initDeviceWarn() {
        if (mListDeviceWarn.size() == 0) {
            if (flipper.isFlipping()) flipper.stopFlipping();
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            initDeviceWarnParam();

            tvEmpty.setVisibility(View.GONE);
            if (!flipper.isFlipping()) flipper.startFlipping();
        }
    }

    private void initDeviceWarnParam() {
        if (flipperIndex >= mListDeviceWarn.size()) flipperIndex = 0;
        ODeviceWarn currentWarn = mListDeviceWarn.get(flipperIndex);
        int showIndex = flipperIndex % 2;
//        LogUtil.e("showIndex:" + (showIndex == 0 ? "第一个" : "第二个"));
        switch (showIndex) {
            case 0:
                tvEquipName1.setText(currentWarn.getDeviceName());
                tvEquipLoc1.setText(currentWarn.getAddress());
                tvWarningType1.setText(currentWarn.getWarnType());
                break;
            default:
                tvEquipName2.setText(currentWarn.getDeviceName());
                tvEquipLoc2.setText(currentWarn.getAddress());
                tvWarningType2.setText(currentWarn.getWarnType());
        }
    }

    //省项目
    private void getProjectsGroupByProvince() {
        Post(MConstants.URL.GET_PROJECTS_GROUP_BY_PROVINCE, OHomeProj.class, new IHttpCallBack<OHomeProj>() {

            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList<OHomeProj> data) {
                mList.clear();
                if (data.size() > 0) {
                    mList.addAll(data);
                }
                mAdapter.notifyDataSetChanged();
                //todo 只有省名、总设备数，缺少各省下面的各状态设备数
                //?参数是否需要再传入一个省相关的字段，只靠state如何区分各省
                //1:根据状态获取设备信息 参数:string state（正常/告警/离线）;接口地址: http://localhost:56721/api/Home/GetDevicesByState
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void OnFailure(String msg) {
                mRefreshLayout.finishRefresh();
                showToast(msg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class HomeData extends BaseModel {
        public String State;
        public String Counts;
    }


    public void pauseFlipper() {
        if (flipper != null && flipper.isFlipping()) {
//            LogUtil.e("flipper stop");
            flipper.stopFlipping();
        }
    }

    public void resumeFlipper() {
        if (flipper != null && mListDeviceWarn != null && mListDeviceWarn.size() > 0) {
//            LogUtil.e("flipper resume");
            flipper.startFlipping();
        }
    }

    @Override
    public void onPause() {
        pauseFlipper();
        super.onPause();
    }

    @Override
    public void onResume() {
        resumeFlipper();
        super.onResume();
    }
}
