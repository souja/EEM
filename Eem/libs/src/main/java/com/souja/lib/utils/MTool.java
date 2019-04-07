package com.souja.lib.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.souja.lib.R;
import com.souja.lib.inter.CompressImgCallBack;
import com.souja.lib.inter.PopEditListener;
import com.souja.lib.models.MediaBean;
import com.souja.lib.tools.DensityUtil;
import com.souja.lib.widget.MImgSpan;
import com.souja.lib.widget.PopZoomGallery;
import com.souja.lib.widget.ZoomImageModel;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

/**
 * Created by Souja on 2018/6/29 0029.
 */

public class MTool {

    public static void Toast(Context context, String msg) {
        if (msg == null || msg.contains("onNext") || context == null) return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void Toast(Context context, String msg, int duration) {
        if (msg == null || msg.contains("onNext") || context == null) return;
        Toast.makeText(context, msg, duration).show();
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("((^(13|14|15|17||18)[0-9]{9}$)|(^0[1,2]{1}d{1}-?d{8}$)|"
                + "(^0[3-9] {1}d{2}-?d{7,8}$)|"
                + "(^0[1,2]{1}d{1}-?d{8}-(d{1,4})$)|"
                + "(^0[3-9]{1}d{2}-? d{7,8}-(d{1,4})$))");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmpty(String s) {
        if (null == s || s.length() == 0 || s.trim().length() == 0 || s.equals("null"))
            return true;
        return false;
    }

    public static void showPopImage(Context context, View v, String url) {
        ArrayList<ZoomImageModel> zoomImageArrayList = new ArrayList<>();
        ZoomImageModel imageScale = new ZoomImageModel();
        int[] xy = new int[2];
        v.getLocationInWindow(xy);
        imageScale.rect = new Rect(xy[0], xy[1], xy[0] + v.getWidth(), xy[1] + v.getHeight());
        imageScale.smallImagePath = url;
        imageScale.bigImagePath = url;
        zoomImageArrayList.add(imageScale);
        PopZoomGallery popZoomGallery = new PopZoomGallery(context, zoomImageArrayList,
                (container, position, view, model) -> {
//                    GlideUtil.load(context, url, view, R.drawable.ic_default_s)
                    Glide.with(context).load(model.bigImagePath).into(view);
                });
        popZoomGallery.showPop(v, 0);
    }

    public static void showPopImage(Context context, View v, GlideUrl url) {
        ArrayList<ZoomImageModel> zoomImageArrayList = new ArrayList<>();
        ZoomImageModel imageScale = new ZoomImageModel();
        int[] xy = new int[2];
        v.getLocationInWindow(xy);
        imageScale.rect = new Rect(xy[0], xy[1], xy[0] + v.getWidth(), xy[1] + v.getHeight());
        imageScale.mGlideUrl = url;
        zoomImageArrayList.add(imageScale);
        PopZoomGallery popZoomGallery = new PopZoomGallery(context, zoomImageArrayList,
                (container, position, view, model) ->
                        Glide.with(context).load(model.mGlideUrl).into(view));
        popZoomGallery.showPop(v, 0);
    }


    public static int getCurrentStartPosition(int prePos, int newSize) {
        if (prePos == 0) return newSize * 100;
        LogUtil.e("pre cur pos=" + prePos + ",new size=" + newSize);
        int curPos;
        while ((prePos + newSize * 100) % newSize != 0) {
            LogUtil.e("计算新起点  " + (prePos + newSize * 100) % newSize);
            prePos++;
            LogUtil.e("起点++ " + prePos);
        }
        curPos = prePos + newSize * 100;
        LogUtil.e("新起点 " + curPos);
        return curPos;
    }


    public static void reflex(final TabLayout tabLayout) {
        reflex(tabLayout, 10, 10);
    }

    public static void reflex(final TabLayout tabLayout, int left, int right) {
        //线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                int leftPadding = DensityUtil.dip2px(tabLayout.getContext(), left);
                int rightPadding = DensityUtil.dip2px(tabLayout.getContext(), right);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);

                    TextView mTextView = (TextView) mTextViewField.get(tabView);

                    tabView.setPadding(0, 0, 0, 0);

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = leftPadding;
                    params.rightMargin = rightPadding;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }

    public static void GO(Context context, Class dClass) {
        context.startActivity(new Intent(context, dClass));
    }

    public static int getCurrentVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            LogUtil.e("当前版本信息：name=" + info.versionName + ",code=" + info.versionCode);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("获取当前版本信息出错");
            return 0;
        }
    }

    public static String getCurrentVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            LogUtil.e("当前版本信息：name=" + info.versionName + ",code=" + info.versionCode);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("获取当前版本信息出错");
            return "";
        }
    }

    public static int getNavigationBarHeight(Context context) {
//        Resources resources = context.getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        int height = resources.getDimensionPixelSize(resourceId);
//        return height;
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("keybord height:" + vh);
        return vh;
    }

    public static void showPopEditView(Context context, PopEditListener listener, View anchor) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_comment, null);
        ScreenUtil.initScale(contentView);
        PopupWindow mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.SHOW_FORCED);//这里给它设置了弹出的时间

        //设置各个控件的点击响应
        EditText edComment = contentView.findViewById(R.id.ed_comment);
        contentView.findViewById(R.id.btn_send).setOnClickListener(v ->
                listener.onResult(mPopWindow, edComment.getText().toString()));
        //是否具有获取焦点的能力
        mPopWindow.setFocusable(true);

        mPopWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
    }

    public static void setSpannableStr(Context context, TextView textView, int strRes, int imgRes) {
        Resources res = context.getResources();
        String tips = res.getString(strRes);
        SpannableString spannable = new SpannableString(" " + tips);
        MImgSpan imgSpan = new MImgSpan(context, imgRes);
        spannable.setSpan(imgSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    public static void setSpannableStr(Context context, TextView textView, String tips, int imgRes) {
        SpannableString spannable = new SpannableString(" " + tips);
        MImgSpan imgSpan = new MImgSpan(context, imgRes);
        spannable.setSpan(imgSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }

    public static String formatStr(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

    public static String formatStr(Locale locale, String format, Object... args) {
        return String.format(locale, format, args);
    }

    public static String getMoney(double money) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(money);
    }

    public static String getMoney(int money) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(money);
    }


    public static void floatAnim(View view) {
        List<Animator> animators = new ArrayList<>();
        /*ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(view, "translationX", -6.0f, 6.0f, -6.0f);
        translationXAnim.setDuration(3000);
        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
//        translationXAnim.setRepeatMode(ValueAnimator.INFINITE);//
        translationXAnim.start();
        animators.add(translationXAnim);*/

        float moveY = 6.0f * ScreenUtil.mScale;
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", -moveY, moveY, -moveY);
        translationYAnim.setDuration(3000);
        translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
//        translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
        translationYAnim.start();
        animators.add(translationYAnim);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.start();
    }


    public static void compressImage(Context context, String oriFilePath, OnRenameListener renameListener,
                                     CompressImgCallBack callBack) {
        compressImage(context, oriFilePath, renameListener, 100, callBack);
    }

    public static void compressImage(Context context, String oriFilePath, OnRenameListener renameListener,
                                     int maxSize, CompressImgCallBack callBack) {
        File oriFile = new File(oriFilePath);
        String saveFilePath = FilePath.getCompressedPath();

        double fileLen = (double) oriFile.length() / 1024d;
        LogUtil.e(oriFilePath + "\nlength:" + fileLen + "kb");

        if (fileLen <= 100) {
            LogUtil.e("无需压缩：" + oriFilePath);
            File tempFile = new File(saveFilePath, oriFile.getName());
            FileUtil.copyFile(oriFile, tempFile);
            callBack.onSkip(tempFile.getAbsolutePath());
        } else {
            Luban.Builder builder = Luban.with(context);
            builder.load(oriFilePath)
                    .ignoreBy(maxSize)
                    .setTargetDir(saveFilePath)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            callBack.onSuccess(file);
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = "压缩失败";
                            if (e != null) msg = e.getMessage();
                            LogUtil.e(msg);
                            callBack.onFail(oriFilePath, msg);
                        }
                    });
            if (renameListener != null)
                builder.setRenameListener(renameListener);
            builder.launch();
        }
    }


    public static boolean isEmptyList(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNetUrlPath(String urlPath) {
        if (TextUtils.isEmpty(urlPath)) return false;
        return urlPath.contains("http:") || urlPath.contains("https:");
    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     */
    public static List<MediaBean> subMediaGroup(HashMap<String, List<String>> groupMap, boolean isVideo) {
        if (groupMap.size() == 0) {
            return null;
        }
        List<MediaBean> list = new ArrayList<>();
        Iterator<Map.Entry<String, List<String>>> it = groupMap.entrySet().iterator();
        String flag = isVideo ? "所有视频" : "所有图片";
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            MediaBean mMediaBean = new MediaBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.size() > 0) {
                mMediaBean.setFolderName(key);
                mMediaBean.setMediaCount(value.size());
                mMediaBean.setTopMediaPath(value.get(0));//获取该组的第1个media的路径
                if (key.equals(flag))
                    list.add(0, mMediaBean);
                else
                    list.add(mMediaBean);
            }
        }
        return list;

    }

    public static void CallPhone(Context mContext, String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        mContext.startActivity(intent);
    }


}

