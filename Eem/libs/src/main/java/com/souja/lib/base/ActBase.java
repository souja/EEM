package com.souja.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.souja.lib.R;
import com.souja.lib.utils.MGlobal;
import com.souja.lib.utils.ScreenUtil;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Souja on 2018/3/12 0012.
 */

public abstract class ActBase extends AppCompatActivity {

    protected AlertDialog _mDialog;
    protected TextView _tvProgressTip;

    public AlertDialog getDialog() {
        createDialog(null);
        return _mDialog;
    }

    public AlertDialog getDialog(String msg) {
        createDialog(msg);
        return _mDialog;
    }

    private void createDialog(@Nullable String msg) {
        if (_mDialog == null) {
            _mDialog = new AlertDialog.Builder(this, R.style.CustomProgressDialog).create();
            View loadView = LayoutInflater.from(this).inflate(R.layout.m_dialog_new, null);
            _mDialog.setView(loadView, 0, 0, 0, 0);
            _mDialog.setCanceledOnTouchOutside(false);
            _tvProgressTip = loadView.findViewById(R.id.tvTip);
        }
        _tvProgressTip.setText(TextUtils.isEmpty(msg) ? "加载中..." : msg);
    }

    protected abstract int setupViewRes();

    protected abstract void initMain();

    public void showToast(String msg) {
        if (msg == null || msg.contains("onNext")) return;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg, int duration) {
        if (msg == null || msg.contains("onNext")) return;
        Toast.makeText(this, msg, duration).show();
    }

    public void NEXT(Intent it) {
        startActivity(it);
    }

    public void GO(Class dclass) {
        startActivity(new Intent(getApplicationContext(), dclass));
    }

    public void addRequest(Callback.Cancelable req) {
        if (mCancelables == null) mCancelables = new ArrayList<>();
        mCancelables.add(req);
    }

    public void addAction(int actionCode, Consumer<Object> action) {
        if (!MGlobal.get().containsKey(actionCode)) {
            MGlobal.get().addAction(actionCode, action);
            if (actions == null) actions = new ArrayList<>();
            actions.add(actionCode);
        }
    }

    public Consumer<Object> getAction(int key) {
        return MGlobal.get().getAction(key);
    }

    public void delAction(int actionCode) {
        MGlobal.get().delAction(actionCode);
    }

    public void addSubscription(Object obj, Consumer<Object> consumer) {
        if (consumer != null)
            mDisposable = Flowable.just(obj).subscribe(consumer);
    }

    public boolean containsKey(int actionCode) {
        return MGlobal.get().containsKey(actionCode);
    }

    public void addSubscription(int key, Object o) {
        if (containsKey(key))
            addSubscription(o, getAction(key));
    }

    protected InputMethodManager inputMethodManager;
    private List<Callback.Cancelable> mCancelables;
    private List<Integer> actions;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleOnCreate(savedInstanceState);

        ScreenUtil.setScale(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (setupViewRes() != 0)
            setContentView(setupViewRes());
        initMain();
    }

    protected void handleOnCreate(Bundle savedInstanceState) {
    }

    @Override
    public void setContentView(int layoutResID) {
        View v = getLayoutInflater().inflate(layoutResID, null);
        ScreenUtil.initScale(v);
        super.setContentView(v);
    }

    @Override
    protected void onDestroy() {
        if (mCancelables != null) {
            for (Callback.Cancelable req : mCancelables) {
                if (req != null && !req.isCancelled()) req.cancel();
            }
        }
        if (actions != null) {
            for (int actionCode : actions) {
                MGlobal.get().delAction(actionCode);
            }
        }
        if (mDisposable != null) {
            if (!mDisposable.isDisposed())
                mDisposable.dispose();
        }
        mDisposable = null;
        super.onDestroy();
    }

    public boolean isHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void requestInput() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
