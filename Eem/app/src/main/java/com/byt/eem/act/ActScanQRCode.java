package com.byt.eem.act;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.byt.eem.R;
import com.byt.eem.base.BaseAct;
import com.byt.eem.util.MConstants;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.souja.lib.models.BaseModel;
import com.souja.lib.utils.ScreenUtil;

//扫描二维码
public class ActScanQRCode extends BaseAct
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    private ViewGroup mainLayout;

    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected int setupViewRes() {
        return R.layout.act_scan_qrcode;
    }

    @Override
    protected void initMain() {
        mainLayout = findViewById(R.id.main_layout);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }
    }

    private void initQRCodeReaderView() {
        View decoderView = getLayoutInflater().inflate(R.layout.content_decoder, null);
        ScreenUtil.initScale(decoderView);
        mainLayout.addView(decoderView);

        qrCodeReaderView = decoderView.findViewById(R.id.qrDecoderView);
        CheckBox mFlashlightCheckBox = decoderView.findViewById(R.id.flashlight_checkbox);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        mFlashlightCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) ->
                qrCodeReaderView.setTorchEnabled(isChecked));
        qrCodeReaderView.startCamera();

//        CheckBox mEnableDecodingCheckBox = decoderView.findViewById(R.id.enable_decoding_checkbox);
//        mEnableDecodingCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) ->
//                qrCodeReaderView.setQRDecodingEnabled(isChecked));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
//        resultTextView.setText(text);
//        pointsOverlayView.setPoints(points);
        addSubscription(new RxScanResult(text,_this),getAction(MConstants.RX_SCAN_QR_CODE));
    }

    public static class RxScanResult extends BaseModel {
        public String result;
        public Activity act;

        public RxScanResult(String result, Activity act) {
            this.result = result;
            this.act = act;
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", view -> ActivityCompat.requestPermissions(ActScanQRCode.this, new String[]{
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA)).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }
}