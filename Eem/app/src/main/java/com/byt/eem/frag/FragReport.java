package com.byt.eem.frag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.byt.eem.R;
import com.byt.eem.base.BaseHolder;
import com.byt.eem.base.MBaseLazyFragmentB;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.souja.lib.base.MBaseAdapter;
import com.souja.lib.inter.CommonItemClickListener;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.MDateUtils;
import com.souja.lib.widget.LoadingDialog;
import com.weigan.loopview.LoopView;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragReport extends MBaseLazyFragmentB {

    @BindView(R.id.rv_msgList)
    RecyclerView rvMsgList;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.yearWheel)
    LoopView yearWheel;
    @BindView(R.id.weekWheel)
    LoopView weekWheel;
    @BindView(R.id.ll_wheels)
    View wheels;
    @BindView(R.id.tv_empty)
    View emptyView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    Unbinder unbinder;

    private String startTime, endTime;
    private List<Report> mList;
    private AdapterReport mAdapter;
    List<String> years = new ArrayList<>();
    List<String> weeks = new ArrayList<>();

    private int reqYear, reqWeek;
    private boolean bChooseYear;

    @Override
    public void onFirstUserVisible() {
        _contentView = LayoutInflater.from(mBaseActivity).inflate(R.layout.frag_report, null, false);
        setContentView(_contentView);
        unbinder = ButterKnife.bind(this, _contentView);
        smartRefresh.setEnableLoadMore(false);
        smartRefresh.setOnRefreshListener(refreshLayout -> getReports());
        mList = new ArrayList<>();
        mAdapter = new AdapterReport(mBaseActivity, mList, position -> {

        });
        rvMsgList.setAdapter(mAdapter);

        Animation fadeIn = AnimationUtils.loadAnimation(mBaseActivity, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(mBaseActivity, R.anim.fade_out);

        _contentView.findViewById(R.id.ll_chooseYear).setOnClickListener(view -> {
            tvTitle.setText("选择年份");
            bChooseYear = true;
            weekWheel.setVisibility(View.GONE);
            yearWheel.setVisibility(View.VISIBLE);
            wheels.setVisibility(View.VISIBLE);
            wheels.startAnimation(fadeIn);
        });

        _contentView.findViewById(R.id.ll_chooseWeek).setOnClickListener(view -> {
            tvTitle.setText("选择周次");
            bChooseYear = false;
            yearWheel.setVisibility(View.GONE);
            weekWheel.setVisibility(View.VISIBLE);
            wheels.setVisibility(View.VISIBLE);
            wheels.startAnimation(fadeIn);
        });

        wheels.setOnClickListener(view -> wheels.startAnimation(fadeOut));
        _contentView.findViewById(R.id.tv_cancel).setOnClickListener(view -> wheels.startAnimation(fadeOut));
        _contentView.findViewById(R.id.tv_confirm).setOnClickListener(view -> {
            if (bChooseYear) {
                String year = years.get(yearWheel.getSelectedItem());
                tvYear.setText(year);
                initWeeks(Integer.parseInt(year));
                int week = Integer.parseInt(tvWeek.getText().toString());
                initStartEnd(Integer.parseInt(year), week);
            } else {
                String week = weeks.get(weekWheel.getSelectedItem());
                tvWeek.setText(week);
                int year = Integer.parseInt(tvYear.getText().toString());
                initStartEnd(year, Integer.parseInt(week));
            }
            wheels.startAnimation(fadeOut);
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wheels.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        reqYear = Integer.parseInt(MDateUtils.getCurrentYear());//年
        for (int i = reqYear; i >= (reqYear - 10); i--) {
            years.add(String.valueOf(i));
        }
        yearWheel.setItems(years);

        initWeeks(reqYear);
        reqWeek = MDateUtils.getWeekOfYear(new Date());//当前周次
        tvWeek.setText(String.valueOf(reqWeek));//周次

        initStartEnd(reqYear, reqWeek);

        _contentView.findViewById(R.id.btn_confirm).setOnClickListener(view -> {
            reqYear = Integer.parseInt(tvYear.getText().toString());
            reqWeek = Integer.parseInt(tvWeek.getText().toString());
            initStartEnd(reqYear, reqWeek);
            getReports();
        });

        getReports();
    }

    private void initWeeks(int year) {
        int totalWeeks = MDateUtils.getMaxWeekNumOfYear(year);
        LogUtil.e(year + "年共" + totalWeeks + "周");
        weeks.clear();
        for (int i = totalWeeks; i >= 1; i--) {
            weeks.add(String.valueOf(i));
//            LogUtil.e(i);
        }
        weekWheel.setItems(weeks);
    }

    private void initStartEnd(int year, int week) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date today = new Date();
//        Calendar c = new GregorianCalendar();
//        c.setTime(today);
        startTime = sdf.format(MDateUtils.getFirstDayOfWeek(year, week));
        endTime = sdf.format(MDateUtils.getLastDayOfWeek(year, week));
//        startTime = sdf.format(MDateUtils.getFirstDayOfWeek(today));
//        endTime = sdf.format(MDateUtils.getLastDayOfWeek(today));
        LogUtil.e("Init StartTime:" + startTime + ",EndTime:" + endTime);
        tvStart.setText(String.valueOf(startTime + "~"));
        tvEnd.setText(endTime);
    }

    private void getReports() {
        LogUtil.e("获取" + reqYear + "年第" + reqWeek + "周数据");
        Post(getDialog(), MConstants.URL.GET_PROJECT_STATISTICS,
                HttpUtil.formatParams(new Param(startTime, endTime).toString()),
                Report.class, new IHttpCallBack<Report>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<Report> data) {
                        smartRefresh.finishRefresh();
                        mList.clear();
                        if (data.size() > 0) {
                            mList.addAll(data);
                        }
                        mAdapter.notifyDataSetChanged();
                        if (mList.size() == 0) emptyView.setVisibility(View.VISIBLE);
                        else emptyView.setVisibility(View.GONE);
                    }

                    @Override
                    public void OnFailure(String msg) {
                        smartRefresh.finishRefresh();
                        showToast(msg);
                    }
                });
    }


    class AdapterReport extends MBaseAdapter<Report> {

        public AdapterReport(Context context, List<Report> list, CommonItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
            return new HolderReport(mInflater.inflate(R.layout.item_report, parent, false));
        }

        @Override
        public void onBindView(RecyclerView.ViewHolder holder, int position) {
            HolderReport mHolder = (HolderReport) holder;
            Report model = getItem(position);
            mHolder.tvName.setText(model.getProjectName());
            mHolder.tvCount.setText(String.valueOf("报警次数：" + model.getCounts() + "次"));
            if (isLastItem(position)) {
                mHolder.vBot.setVisibility(View.GONE);
                mHolder.vTemp.setVisibility(View.VISIBLE);
            } else {
                mHolder.vBot.setVisibility(View.VISIBLE);
                mHolder.vTemp.setVisibility(View.GONE);
            }
        }
    }

    static class HolderReport extends BaseHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.v_bot)
        View vBot;
        @BindView(R.id.v_temp)
        View vTemp;

        public HolderReport(View itemView) {
            super(itemView);
        }
    }

    class Report extends BaseModel {

        /**
         * ProjectId : 1
         * ProjectName : 军区总医院
         * Counts : 448
         */

        private int ProjectId;
        private String ProjectName;
        private int Counts;

        public int getProjectId() {
            return ProjectId;
        }

        public void setProjectId(int ProjectId) {
            this.ProjectId = ProjectId;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String ProjectName) {
            this.ProjectName = ProjectName;
        }

        public int getCounts() {
            return Counts;
        }

        public void setCounts(int Counts) {
            this.Counts = Counts;
        }
    }

    class Param extends BaseModel {
        public String beginTime, endTime;

        public Param(String beginTime, String endTime) {
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}
