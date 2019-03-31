package com.byt.eem.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.souja.lib.widget.PopZoomGallery;
import com.souja.lib.widget.ZoomImageModel;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Souja on 2018/7/23 0023.
 */

public class GlideUtil {


    public static void load(Context context, int imgRes, ImageView target) {
        Glide.with(context).load(imgRes).into(target);
    }

    public static void load(Context context, String path, ImageView target) {
        Glide.with(context)
                .load(path)
                .into(target);
    }

    public static void load(Context context, String path, int placeHolder, ImageView target) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeHolder);
        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .into(target);
    }


    public static void load(Context context, String url, int radius, int placeholder, ImageView target) {
        RoundedCorners roundedCorners = new RoundedCorners(radius);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                .override(222, 222)
                .placeholder(placeholder);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(target);
    }


    public static void showPop(Context context, View v, List<String> urls, String imgPath) {
        int index = 0;
        ArrayList<ZoomImageModel> zoomImageArrayList = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {

            String url = urls.get(i);
            if (imgPath.equals(url)) {
                index = i;
            }
            LogUtil.d("pop url " + url);
            ZoomImageModel imageScale = new ZoomImageModel();
            int[] xy = new int[2];
            v.getLocationInWindow(xy);
            imageScale.rect = new Rect(xy[0], xy[1], xy[0] + v.getWidth(), xy[1] + v.getHeight());
            imageScale.smallImagePath = url;
            imageScale.bigImagePath = url;
            zoomImageArrayList.add(imageScale);
        }
        PopZoomGallery popZoomGallery = new PopZoomGallery(context, zoomImageArrayList,
                (container, position, view, model) -> {
                    String url = model.bigImagePath;
                    Glide.with(context).load(url).into(view);
                });
        popZoomGallery.showPop(v, index);
    }

    public static void showPop(Context context, View v, List<String> urls, int index) {
        ArrayList<ZoomImageModel> zoomImageArrayList = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            LogUtil.d("pop url " + url);
            ZoomImageModel imageScale = new ZoomImageModel();
            int[] xy = new int[2];
            v.getLocationInWindow(xy);
            imageScale.rect = new Rect(xy[0], xy[1], xy[0] + v.getWidth(), xy[1] + v.getHeight());
            imageScale.smallImagePath = url;
            imageScale.bigImagePath = url;
            zoomImageArrayList.add(imageScale);
        }
        PopZoomGallery popZoomGallery = new PopZoomGallery(context, zoomImageArrayList,
                (container, position, view, model) -> {
                    String url = model.bigImagePath;
                    Glide.with(context).load(url).into(view);
                });
        popZoomGallery.showPop(v, index);
    }

    public static RequestOptions DefaultPlaceHolderOption(int placeHolder) {
        return new RequestOptions().placeholder(placeHolder);
    }

//    public static GlideUrl getValidUrl(String url) {
//        if (url == null || url.isEmpty()) return null;
//        if (EApp.getUserInfo() == null) return null;
//        if (!url.contains("http")) url = MConstants.HTTP + url;
//        LogUtil.d("load img:" + url);
//        GlideUrl validUrl = new GlideUrl(url, new LazyHeaders.Builder()
//                .addHeader("userHospitalId", HosApp.getUserInfo().getId() + "")
//                .addHeader("accessToken", HosApp.getUserInfo().getAccessToken())
//                .build());
//        return validUrl;
//    }


//    public static void loadHeadIcon(Context context, String url, ImageView target) {
//        if (MTool.isEmpty(url)) target.setImageResource(R.drawable.ic_error);
//        else loadValidImg(context, url, R.drawable.ic_error, target);
//    }
/*    public static void loadValidImg(Context context, String url, int placeholder, ImageView target) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(target);
    }*/



}
