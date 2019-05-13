package com.byt.eem.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.byt.eem.EApp;
import com.byt.eem.act.ActMain;
import com.byt.eem.util.MConstants;
import com.souja.lib.utils.SPHelper;


/**
 * Created by Ydz on 2017/3/13 0013.
 */

public class DownLoadApkBroadCast extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        long myDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long reference = SPHelper.getLong(MConstants.DOWNLOAD_KEY);
        if (reference == myDownloadID) {
            if (ActMain.get() != null) {
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.fromFile(ActMain.get().getNewApkInstaller()),
                        "application/vnd.android.package-archive");
                EApp.getContext().startActivity(it);
            }
        }
    }
}