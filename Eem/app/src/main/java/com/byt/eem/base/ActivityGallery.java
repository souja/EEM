package com.byt.eem.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.byt.eem.util.GlideUtil;
import com.souja.lib.models.SelectImgOptions;
import com.souja.lib.utils.FilePath;
import com.souja.lib.utils.MGlobal;
import com.souja.lib.widget.HackyViewPager;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.kareluo.imaging.IMGEditActivity;
import uk.co.senab.photoview.PhotoView;

/**
 * ClassName:ActivityGallery (类似微信的图片左右选择器) <br/>
 * Date: 2014年9月9日 下午3:19:45 <br/>
 *
 * @author WangYue
 * @author Ydz
 * @since JDK 1.6
 */
public class ActivityGallery extends BaseAct {

    private HackyViewPager viewPager;
    private TextView textView_num;
    private Button btn_complete;
    private AppCompatCheckBox checkBox;
    private AppCompatButton btnEditImg;

    private final int REQ_IMAGE_EDIT = 2018;//编辑图片
    private File editFile;
    private MyPagerAdapter adapter;

    private List<String> mListAll = new ArrayList<>();
    private List<String> mListSelected = new ArrayList<>();
    private int index = 0;
    private int count, maxCount;
    private Point mPoint = new Point();
    //    private List<Integer> errorList = new ArrayList<>();
    private boolean needFinish;
    private String strComplete;
    private String curSelectedPath;//编辑照片前的图片路径


    @Override
    protected int setupViewRes() {
        return com.souja.lib.R.layout.activity_gallery;
    }

    @Override
    protected void initMain() {
        //Init Views
        checkBox = findViewById(com.souja.lib.R.id.gallery_checkbox);
        btn_complete = findViewById(com.souja.lib.R.id.gallery_complate);
        textView_num = findViewById(com.souja.lib.R.id.gallery_nums);
        viewPager = findViewById(com.souja.lib.R.id.gallery_vp);
        btnEditImg = findViewById(com.souja.lib.R.id.btn_edit);
        Resources res = getResources();
        strComplete = res.getString(com.souja.lib.R.string.complete);
        //Init Intent
        Intent it = getIntent();
        boolean bOnlySelected = it.getBooleanExtra("selected", false);
        mListSelected.addAll(ActPhotoGallery.getInstance().selectedPathList);
        if (bOnlySelected) {
            mListAll.addAll(ActPhotoGallery.getInstance().selectedPathList);
        } else {
            mListAll.addAll(ActPhotoGallery.getInstance().mAdapter.getPathList());
        }
        maxCount = it.getIntExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, 1);
        index = it.getIntExtra(SelectImgOptions.IMAGES_INDEX, 0);
        if (mListSelected != null && !mListSelected.isEmpty()) {
            btn_complete.setText(String.valueOf(strComplete + "(" + mListSelected.size() + "/" + maxCount + ")"));
        }

        initVp();
        initListeners();
    }

    private void initVp() {
        int mVp_h = 0;
        int mVp_w = MGlobal.get().getDeviceWidth();
        updateImgCount();
        mPoint.set(mVp_w, mVp_h);
        adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int posi) {
                index = posi;
                textView_num.setText(String.valueOf((posi + 1) + "/" + count));
                isCheck(posi);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int posi) {
            }
        });
        viewPager.setCurrentItem(index);
        isCheck(index);
    }

    private void initListeners() {
        //返回
        findViewById(com.souja.lib.R.id.backbutton).setOnClickListener(v -> {
            needFinish = false;
            SetResult();
        });
        //完成
        btn_complete.setOnClickListener(v -> {
//            needFinish = true;
            needFinish = false;
            SetResult();
        });
        //编辑
        btnEditImg.setOnClickListener(v -> {
            curSelectedPath = mListAll.get(viewPager.getCurrentItem());
            LogUtil.e("current selected path:" + curSelectedPath);
            File selectedFile = new File(curSelectedPath);
            LogUtil.e("file exist:" + selectedFile.exists());
            if (selectedFile.exists()) {
                LogUtil.e("ab path:" + selectedFile.getAbsolutePath());
                Uri uri = Uri.fromFile(selectedFile);
                editFile = new File(FilePath.getTempPicturePath() + "/editedimg" + System.currentTimeMillis() + ".jpg.bk");
                startActivityForResult(
                        new Intent(this, IMGEditActivity.class)
                                .putExtra("IMAGE_URI", uri)
                                .putExtra("IMAGE_SAVE_PATH", editFile.getAbsolutePath()),
                        REQ_IMAGE_EDIT);
            } else {
                showToast("图片加载失败");
            }
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (errorList.contains(index)) {
//                if (buttonView.getVisibility() == View.VISIBLE) {
//                    showToast("图片加载失败，不可选择");
//                    buttonView.setChecked(false);
//                }
//            } else {
            if (!mListSelected.contains(mListAll.get(index))
                    && mListSelected.size() < maxCount) {
                if (isChecked) {
                    mListSelected.add(mListAll.get(index));
                }
            } else if (mListSelected.contains(mListAll.get(index))) {
                if (!isChecked) {
                    mListSelected.remove(mListAll.get(index));
                }
            } else {
                // 超过4个 toast
                buttonView.setChecked(false);
            }
            if (mListSelected.size() > 0)
                btn_complete.setText(String.valueOf(strComplete + "(" + mListSelected.size() + "/" + maxCount + ")"));
            else
                btn_complete.setText(strComplete);
//            }
        });
    }

    private void updateImgCount() {
        count = mListAll.size();
        textView_num.setText(String.valueOf((index + 1) + "/" + count));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE_EDIT) {
            if (resultCode == RESULT_OK) {
                if (mListSelected.contains(curSelectedPath)) {
                    mListSelected.remove(curSelectedPath);
                }
                String editPath = editFile.getAbsolutePath();
                if (mListSelected.size() < maxCount) {
                    mListSelected.add(editPath);
//                    checkBox.setChecked(true);
                }
                ActPhotoGallery.getInstance().onEditImg(curSelectedPath, editPath);
                mListAll.add(viewPager.getCurrentItem(), editPath);
                adapter.notifyDataSetChanged();
                checkBox.setChecked(true);
                updateImgCount();

            }
        }
    }

    private void SetResult() {
        Intent intent = new Intent(_this, ActPhotoGallery.class);
        Bundle b = new Bundle();
        b.putStringArrayList(SelectImgOptions.IMAGE_PATH_LIST_SELECTED, (ArrayList<String>) mListSelected);
        b.putBoolean("needFinish", needFinish);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void isCheck(int posi) {
        if (mListSelected.contains(mListAll.get(posi)))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        needFinish = false;
        SetResult();
    }

    public class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private RequestManager mRequestManager;

        public MyPagerAdapter(Context context) {
            mContext = context;
            mRequestManager = Glide.with(mContext);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int arg1, Object arg2) {
            container.removeView((View) arg2);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListAll.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int posi) {
            String path = mListAll.get(posi);
            PhotoView imageView = new PhotoView(_this);
            mRequestManager.load(path).into(imageView);
            container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
}
