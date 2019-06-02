package com.byt.eem.act;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.byt.eem.EApp;
import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.frag.FragHome;
import com.byt.eem.frag.FragMine;
import com.byt.eem.frag.FragMsg;
import com.byt.eem.frag.FragReport;
import com.byt.eem.util.MConstants;
import com.souja.lib.inter.DownloadTask;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.utils.FilePath;
import com.souja.lib.utils.MDateUtils;
import com.souja.lib.utils.MTool;
import com.souja.lib.utils.SPHelper;
import com.souja.lib.widget.MCheckBox;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActMain extends BaseAct {

    @BindView(R.id.layout_navigation)
    LinearLayout layoutNavigation;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_desc)
    TextView tvVersionInfo;
    @BindView(R.id.btn_skip)
    Button btnLater;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.ll_update)
    View upgradeView;
    @BindView(R.id.sk_progress)
    ProgressBar mProgressBar;

    private FragHome mFragHome;
    private FragMsg mFragMsg;
    private FragReport mFragReport;
    private FragMine mFragMine;

    private long exitTime;
    private int preNaviIndex = 0;  //上一个获得焦点的导航
    private boolean bClick;

    private File newApkInstaller;
    private CheckFileLengthListener mFileLengthListener;

    private static ActMain instance;

    public static ActMain get() {
        return instance;
    }

    @Override
    protected int setupViewRes() {
        return R.layout.act_main;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        instance = this;

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

        new Thread(() -> checkUpdate()).start();

    }

    class VersionInfo extends BaseModel {
        int Version;
        String DownLoadUrl, Description;
        boolean ForceUpdate;
    }

    private void checkUpdate() {
        Post(null, MConstants.URL.CHECK_UPDATE, VersionInfo.class, new IHttpCallBack<VersionInfo>() {
            @Override
            public void OnSuccess(String msg, ODataPage page, ArrayList<VersionInfo> data) {
                if (data != null && data.size() > 0)
                    runOnUiThread(() -> doCheck(data.get(0)));
            }

            @Override
            public void OnFailure(String msg) {
                LogUtil.e("检查更新失败:" + msg);
            }
        });
    }

    private void doCheck(VersionInfo versionInfo) {
        if (versionInfo != null) {
            int currentVersion = MTool.getCurrentVersionCode(_this);
            LogUtil.e("客户端版本号:" + currentVersion);
            int latestVersionCode = versionInfo.Version;
            LogUtil.e("服务器版本号:" + latestVersionCode);
            if (latestVersionCode <= currentVersion) {
                LogUtil.e("当前已是最新版本");
            } else {
                long preRemindTime = SPHelper.getLong("upgradeRemind");//升级提醒
                long curTime = Long.parseLong(MDateUtils.getCurrentDate1());

                if (preRemindTime == 0 || curTime > preRemindTime) {//没有提醒过||提醒时间已超过一天，重新提醒
                    runOnUiThread(() -> showUpgradeView(latestVersionCode, versionInfo.Description,
                            versionInfo.ForceUpdate,
                            versionInfo.DownLoadUrl));
                }
            }
        }

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

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    /**
     * 升级弹窗
     *
     * @param upgradeDesc  新版本更新提示
     * @param forceUpgrade 是否强制更新
     * @param downloadUrl  新版本下载地址
     */
    private void showUpgradeView(int latestVersionCode, String upgradeDesc, boolean forceUpgrade, String downloadUrl) {
        tvVersionInfo.setText(TextUtils.isEmpty(upgradeDesc) ? "快来下载体验新版本吧" : upgradeDesc);
        upgradeView.setVisibility(View.VISIBLE);

        LogUtil.e("强制更新:" + forceUpgrade);
        String apkLocalPath = FilePath.getDownloadPath() + "/YLY_Hospital_" + latestVersionCode + ".apk";
        LogUtil.e("InstallerFileName:" + apkLocalPath);
        newApkInstaller = new File(apkLocalPath);

        if (forceUpgrade) {//强制更新
            if (newApkInstaller.exists()) {//有安装包
                mFileLengthListener = b -> {
                    if (b) {//安装包完整
                        btnUpdate.setText("已下载，立即安装");
                        btnUpdate.setOnClickListener(v -> installApp());
                        btnLater.setOnClickListener(v -> finish());
                    } else {//安装包异常
                        newApkInstaller.delete();
                        newApkInstaller = new File(apkLocalPath);
                        btnUpdate.setOnClickListener(v -> downLoadApk(downloadUrl));
                        btnLater.setOnClickListener(v -> finish());
                    }
                };
                checkFileLength(downloadUrl);
            } else {
                btnUpdate.setOnClickListener(v -> downLoadApk(downloadUrl));
                btnLater.setOnClickListener(v -> finish());
            }
        } else {//非强制更新
            if (newApkInstaller.exists()) {//有安装包
                mFileLengthListener = b -> {
                    if (b) {//安装包完好
                        btnUpdate.setText("已下载，立即安装");
                        btnUpdate.setOnClickListener(v -> installApp());
                        btnLater.setOnClickListener(v -> skipUpgrade());
                    } else {//安装包异常
                        newApkInstaller.delete();
                        newApkInstaller = new File(apkLocalPath);
                        btnUpdate.setOnClickListener(v -> {
                            showToast("新版本下载中...");
                            EApp.downloadLatestVersionInBackground(downloadUrl, newApkInstaller);
                            skipUpgrade();
                        });
                        btnLater.setOnClickListener(v -> skipUpgrade());
                    }
                };
                checkFileLength(downloadUrl);
            } else {//没有安装包
                btnUpdate.setOnClickListener(v -> {
                    showToast("新版本下载中...");
                    EApp.downloadLatestVersionInBackground(downloadUrl, newApkInstaller);
                    skipUpgrade();
                });
                btnLater.setOnClickListener(v -> skipUpgrade());
            }
        }
    }

    //跳过升级
    private void skipUpgrade() {
        long curTime = Long.parseLong(MDateUtils.getCurrentDate1());
        SPHelper.putLong("upgradeRemind", curTime);

        Animation fadeout = AnimationUtils.loadAnimation(instance, R.anim.fade_out);
        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                upgradeView.setVisibility(View.GONE);
                newApkInstaller = null;
                mFileLengthListener = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        upgradeView.startAnimation(fadeout);
    }

    private void checkFileLength(String downloadUrl) {
        LogUtil.e("检查安装包");
        new Thread(() -> {
            URL url;
            HttpURLConnection connection;
            long fileLength = -1;
            try {
                url = new URL(downloadUrl);
                connection = (HttpURLConnection) url.openConnection();
                fileLength = connection.getContentLength();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(2);
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(2);
            }
            long length = newApkInstaller.length();
            System.out.println("newLength=" + fileLength + ",existLength=" + length);
            if (fileLength == length) {
                mHandler.sendEmptyMessage(1);
            } else {
                mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mFileLengthListener != null) {
                        LogUtil.e("安装包完整");
                        mFileLengthListener.onCheckOver(true);
                    }
                    break;
                case 2:
                    if (mFileLengthListener != null) {
                        LogUtil.e("安装包异常");
                        mFileLengthListener.onCheckOver(false);
                    }
                    break;
            }
            return false;
        }
    });

    //下载进度对话框-仅强制更新时使用
    private void downLoadApk(String downloadUrl) {
//        HosApp.get().registerDownload();
        DownloadTask task = new DownloadTask(downloadUrl, newApkInstaller, new DownloadTask.LoadCallBack() {
            @Override
            public void onPreExecute() {
                mProgressBar.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
            }

            @Override
            public void onProgressUpdate(Integer progress) {
                mProgressBar.setProgress(progress);
            }

            @Override
            public void onFail() {
                if (btnUpdate != null) {
                    btnUpdate.setVisibility(View.VISIBLE);
                    btnUpdate.setText("下载失败，点击重试");
                }
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mProgressBar.setProgress(0);
                }
            }

            @Override
            public void onInstall() {
                installApp();
            }
        });
        EApp.setTask(task);
        task.execute();
    }

    private void installApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(newApkInstaller),
                "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    public File getNewApkInstaller() {
        return newApkInstaller;
    }

    private interface CheckFileLengthListener {
        void onCheckOver(boolean b);
    }

}
