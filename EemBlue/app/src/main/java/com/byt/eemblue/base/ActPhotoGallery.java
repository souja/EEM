package com.byt.eemblue.base;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;

import com.souja.lib.base.DirAdapter;
import com.souja.lib.base.DirLayout;
import com.souja.lib.models.MediaBean;
import com.souja.lib.models.RxCropInfo;
import com.souja.lib.models.RxImgPath;
import com.souja.lib.models.SelectImgOptions;
import com.souja.lib.utils.FileUtil;
import com.souja.lib.utils.LibConstants;
import com.souja.lib.utils.MGlobal;
import com.souja.lib.utils.MTool;
import com.souja.lib.utils.SPHelper;
import com.souja.lib.widget.TitleBar;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * ClassName:ActPhotoGallery <br/>
 * Date:     2014-9-19 下午3:20:59 <br/>
 *
 * @author WangYue
 * @see
 * @since JDK 1.6
 */
public class ActPhotoGallery extends BaseAct {

    public static final int REQUEST_CODE1 = 10100;
    public static final int REQUEST_CODE2 = 10101;

    private TitleBar mTitleBar;
    private Button mBtnSelectDir;
    private Button mBtnPreview;
    private RecyclerView mRecyclerView;
    private DirLayout mDirLayout;

    public List<String> allImageList;   //所有图片的路径
    public ArrayList<String> selectedPathList;  //已选择的图片路径
    public HashMap<String, List<String>> mPathMap; //key-文件夹名称，value-图片路径List
    public int maxCount;  //可选图片数量
    public String tempPath;

    public PhotoAdapter mAdapter;
    private List<MediaBean> mDirList;   //相册目录List
    private Handler handler = new Handler();   //处理器
    private File cameraFile;
    private DirAdapter mDirAdapter;
    private String strComplete;

    private static ActPhotoGallery instance;
    private boolean bCrop, skipEnable;
    private String defaultDir;

    public static void open(Context context, int max, ArrayList<String> selected) {
        Intent it = new Intent(context, ActPhotoGallery.class);
        it.putExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, max);
        if (selected != null && selected.size() > 0)
            it.putExtra(SelectImgOptions.IMAGE_PATH_LIST_SELECTED, selected);
        context.startActivity(it);
    }

    public static void open(Context context, SelectImgOptions options) {
        Intent it = new Intent(context, ActPhotoGallery.class);
        it.putExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, options.max);
        if (options.selectedImgListPath != null && options.selectedImgListPath.size() > 0)
            it.putExtra(SelectImgOptions.IMAGE_PATH_LIST_SELECTED, options.selectedImgListPath);
        if (options.crop) {
            it.putExtra(SelectImgOptions.IMAGES_CROP, true);
            it.putExtra(SelectImgOptions.IMAGES_SKIP_CROP, options.skip);
            if (options.x != 0 && options.y != 0) {
                it.putExtra(SelectImgOptions.IMAGES_CROP_X, options.x);
                it.putExtra(SelectImgOptions.IMAGES_CROP_Y, options.y);
            }
        }
        context.startActivity(it);
    }

    @Override
    protected int setupViewRes() {
        return com.souja.lib.R.layout.act_select_media;
    }

    @Override
    protected void initMain() {
        instance = this;
        strComplete = getResources().getString(com.souja.lib.R.string.complete);
        defaultDir = SPHelper.getString(SelectImgOptions.GALLERY_LAST);
        initViews();
        initIntent();
        initListeners();
        initListView();
        getImages();
    }

    private void initViews() {
        mTitleBar = findViewById(com.souja.lib.R.id.m_title);
        mBtnSelectDir = findViewById(com.souja.lib.R.id.btn_selectDir);
        mBtnPreview = findViewById(com.souja.lib.R.id.btn_preview);
        mRecyclerView = findViewById(com.souja.lib.R.id.media_list);
        mDirLayout = findViewById(com.souja.lib.R.id.dir_layout);
    }

    private int cropX, cropY;

    private void initIntent() {
        Intent intent = getIntent();
        maxCount = intent.getIntExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, 4);
        bCrop = intent.getBooleanExtra(SelectImgOptions.IMAGES_CROP, false);
        if (bCrop) {
            skipEnable = intent.getBooleanExtra(SelectImgOptions.IMAGES_SKIP_CROP, true);

            cropX = intent.getIntExtra("x", -1);
            if (cropX > 0) {
                cropY = intent.getIntExtra("y", -1);
            }
            Consumer cropHeadIcon = (Consumer<RxCropInfo>) cropInfo -> {
                String path = cropInfo.cropFilePath;
                selectedPathList.clear();
                selectedPathList.add(path);
                cropInfo.mActivity.finish();
                goBack();
            };
            MGlobal.get().addAction(LibConstants.CROP_IMG, cropHeadIcon);
        }
        if (intent.getStringArrayListExtra(SelectImgOptions.IMAGE_PATH_LIST_SELECTED) != null) {
            //存放调用此类的类传递过来的图片路径
            selectedPathList = intent.getStringArrayListExtra(SelectImgOptions.IMAGE_PATH_LIST_SELECTED);
            refreshNum();
        } else
            selectedPathList = new ArrayList<>();
    }

    private void initListeners() {
        mTitleBar.setLeftClick(v -> onBackPressed());//返回
        mTitleBar.setRightClick(v -> {//完成
            setResultBack();
        });
        mBtnSelectDir.setOnClickListener(view -> {//选择图片目录
            if (!mDirLayout.isShowing()) {
                mDirLayout.show();
            } else {
                mDirLayout.dismiss();
            }
        });
        mBtnPreview.setOnClickListener(v -> {//预览
            if (selectedPathList.size() > 0) {
                Intent it = new Intent(this, ActivityGallery.class);
                it.putExtra("selected", true);
//                it.putExtra(LibConstants.IMAGE_PATH_LIST_SELECTED, (ArrayList<String>) selectedPathList);
//                it.putExtra(LibConstants.IMAGES_PATH_LIST_ALL, (ArrayList<String>) selectedPathList);
                it.putExtra(SelectImgOptions.IMAGES_INDEX, 0);
                it.putExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, maxCount);
                startActivityForResult(it, REQUEST_CODE1);
            }
        });
    }

    private void initListView() {
        mPathMap = new HashMap<>();
        allImageList = new ArrayList<>();
        mAdapter = new PhotoAdapter(this, new ArrayList<>(), position -> {
            if (position == 0) {//拍照
                if (selectedPathList.size() >= maxCount) {
                    showToast("已达最大可选择张数");
                    return;
                }

                if (PermissionChecker.checkSelfPermission(_this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(_this, new String[]{Manifest.permission.CAMERA},
                            1122);
                } else {
//                selectedPathListdd.clear();
//                mAdapter.notifyDataSetChanged();
//                refreshNum();
                    turnCamera();
                }
            } else {
                Intent it = new Intent(this, ActivityGallery.class);
//                it.putExtra(LibConstants.IMAGE_PATH_LIST_SELECTED, (ArrayList<String>) selectedPathList);
//                it.putExtra(LibConstants.IMAGES_PATH_LIST_ALL, (ArrayList<String>) mAdapter.getPathList());
                it.putExtra(SelectImgOptions.IMAGES_INDEX, position - 1);
                it.putExtra(SelectImgOptions.IMAGES_MAX_SELECT_COUNT, maxCount);
                startActivityForResult(it, REQUEST_CODE1);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    //利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            showToast(com.souja.lib.R.string.sdcard_nosize);
            return;
        }

        // 显示进度条
        new Thread(() -> {
            scanData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //通知Handler扫描图片完成
            mPathMap.put("所有图片", allImageList);
            handler.post(() -> {
                if (!TextUtils.isEmpty(defaultDir)) {
                    changeDir(defaultDir);
                } else
                    changeDir("所有图片");
                mDirList = MTool.subMediaGroup(mPathMap, false);
                initDirList();
            });
        }).start();
    }

    private void turnCamera() {
        try {
            cameraFile = FileUtil.setUpPhotoFile();

            LogUtil.e("cameraFile path=" + cameraFile.getAbsolutePath()
                    + ",exist=" + cameraFile.exists());

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            startActivityForResult(intent, REQUEST_CODE2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            showToast("相机设备异常");
        }
    }

    public void refreshNum() {
        if (selectedPathList.size() == 0) {
            mTitleBar.setRightMenuText(strComplete);
            mBtnPreview.setTextColor(Color.parseColor("#666666"));
            mBtnPreview.setText("预览");
        } else {
            mTitleBar.setRightMenuText(strComplete + "(" + selectedPathList.size() + "/" + maxCount + ")");
            mBtnPreview.setTextColor(Color.parseColor("#CFCFCF"));
            mBtnPreview.setText(String.valueOf("预览(" + selectedPathList.size() + ")"));
        }
    }

    private void scanData(Uri uri) {
        ContentResolver mContentResolver = getContentResolver();
        Cursor mCursor = mContentResolver.query(uri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String tempPath = path.substring(path.lastIndexOf(".")).toLowerCase();
            if (tempPath.equals(".bk") ||
                    (!tempPath.equals(".jpg") && !tempPath.equals(".png") && !tempPath.equals(".jpeg"))) {
                LogUtil.e("图片路径无效 " + path);
            } else if (path.contains("counselorfiles") || path.contains("000files")
                    || path.contains("ymbfiles") || path.contains(FileUtil.APP_NAME.toLowerCase())
                    || new File(path).length() == 0) {
                LogUtil.e("跳过应用文件 " + path);
            } else {
                allImageList.add(path);
                // 获取该图片的父路径名
                String parentName = new File(path).getParentFile()
                        .getName();
                // 根据父路径名将图片放入到mGruopMap中
                if (!mPathMap.containsKey(parentName)) {
                    List<String> childList = new ArrayList<>();
                    childList.add(path);
                    mPathMap.put(parentName, childList);
                } else {
                    mPathMap.get(parentName).add(path);
                }
            }
        }
        mCursor.close();
    }

    private void goCrop() {
//        ActCrop.open(_this, skipEnable, cropX, cropY <= 0 ? cropX : cropY, selectedPathList.get(0));
    }

    private void initDirList() {
        mDirAdapter = new DirAdapter(_this, mDirList, defaultDir, false, mediaBean -> {
            String key = mediaBean.getFolderName();
            changeDir(key);
            mDirLayout.dismiss();
        });
        mDirLayout.setAdapter(mDirAdapter);
    }

    private void changeDir(String key) {
        mTitleBar.setTitle(key);
        mAdapter.setPhotoPathList(mPathMap.get(key));
        mRecyclerView.scrollTo(0, 0);
    }

    //编辑（涂鸦等）产生了新的图片
    public void onEditImg(String oriPath, String editPath) {
        //添加到图库
        AddPicToScan(editPath);

        if (selectedPathList.contains(oriPath)) {
            selectedPathList.remove(oriPath);
        }
        if (selectedPathList.size() < maxCount) {
            selectedPathList.add(editPath);
        }

        String curFolder = mDirList.get(mDirAdapter.getDirIndex()).getFolderName();
        LogUtil.e("curFolder : " + curFolder);
        mPathMap.get(curFolder).add(editPath);//添加到数据源
        //添加到当前图片目录
        mAdapter.addPath(editPath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE2 == requestCode) { //相机
                if (cameraFile == null) return;
                tempPath = cameraFile.getAbsolutePath();
                AddPicToScan(tempPath);
                LogUtil.e("tempPath=" + tempPath + ",cameraFile exist=" + cameraFile.exists());
                selectedPathList.add(tempPath);
                refreshNum();
                allImageList.add(0, tempPath);
                String curFolder = mDirList.get(mDirAdapter.getDirIndex()).getFolderName();
                if (curFolder.equals("所有图片")) {
                    mAdapter.notifyDataSetChanged();
                }
//                setResultBack();
            } else if (REQUEST_CODE1 == requestCode) { //画廊
                Bundle b = data.getExtras();
                if (b == null) return;
                ArrayList<String> newList = b.getStringArrayList(SelectImgOptions.IMAGE_PATH_LIST_SELECTED);
                if (newList == null) return;
                this.selectedPathList = newList;
                refreshNum();
                if (selectedPathList.size() > 0 && bCrop) {
                    goCrop();
                } else {
                    boolean needfinish = b.getBoolean("needFinish", false);
                    if (needfinish) {
                        setResultBack();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else {
            if (requestCode == REQUEST_CODE2) {//相机拍照
                LogUtil.e("相机拍照取消");
                if (cameraFile != null && cameraFile.exists())
                    cameraFile.delete();
            }
        }
    }

    private void setResultBack() {
        if (selectedPathList.size() > 0 && bCrop) {
            goCrop();
        } else {
            goBack();
        }
    }

    private void goBack() {
        if (MGlobal.get().containsKey(LibConstants.RX_CHOOSE_PHOTO)) {
            addSubscription(new RxImgPath(this, selectedPathList),
                    MGlobal.get().getAction(LibConstants.RX_CHOOSE_PHOTO));
        } else {
            LogUtil.e("未设置回调");
            finish();
        }
    }

    private void AddPicToScan(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public static ActPhotoGallery getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        instance = null;
        MGlobal.get().delAction(LibConstants.CROP_IMG);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDirLayout.isShowing()) {
            mDirLayout.dismiss();
        } else
            super.onBackPressed();
    }
}