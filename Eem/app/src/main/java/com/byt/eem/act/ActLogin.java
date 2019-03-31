package com.byt.eem.act;

import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

import com.byt.eem.EApp;
import com.byt.eem.R;
import com.byt.eem.base.BaseActEd;
import com.byt.eem.model.UserInfo;
import com.byt.eem.util.HttpUtil;
import com.byt.eem.util.MConstants;
import com.google.gson.JsonObject;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActLogin extends BaseActEd {
    @BindView(R.id.ed_account)
    AppCompatEditText edAccount;
    @BindView(R.id.ed_pwd)
    AppCompatEditText edPwd;
    @BindView(R.id.ib_pwdVisible)
    ImageButton ibPwdVisible;
    @BindView(R.id.forgetPwd)
    TextView forgetPwd;
    @BindView(R.id.login)
    TextView login;

    private boolean bPwdVisible;

    @Override
    protected int setupViewRes() {
        return R.layout.act_login;
    }

    @Override
    protected void initMain() {
        ButterKnife.bind(this);
        edPwd.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        requestInput();
                        login.postDelayed(() -> sendLogin(), 100);
//                        login.postDelayed(() -> testGo(), 100);
                    }
                    return false;
                }
        );

        ibPwdVisible.setOnClickListener(v -> {
            if (!bPwdVisible) {
//                ibPwdVisible.setImageResource(eyeOpen);
                edPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edPwd.setSelection(edPwd.length());
            } else {
//                ibPwdVisible.setImageResource(eyeClose);
                edPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edPwd.setSelection(edPwd.length());
            }
            bPwdVisible = !bPwdVisible;
        });

        login.setOnClickListener(view -> {
            sendLogin();
//            testGo();
        });

    }

    private void testGo() {
        GO(ActMain.class);
        finish();
    }

    private void sendLogin() {
        String phone = edAccount.getText().toString().trim();
        if (phone.isEmpty()) {
            showToast("请输入登录账号");
            return;
        }
        String pwd = edPwd.getText().toString().trim();
        if (pwd.isEmpty()) {
            showToast("请输入密码");
            return;
        }
        JsonObject obj = new JsonObject();
        obj.addProperty("UserId", phone);
        obj.addProperty("PassWord", pwd);
//        String deviceInfo = SPHelper.getString("deviceInfo");
//        obj.addProperty("hardware", deviceInfo);

        Post(new LoadingDialog(_this), MConstants.URL.LOGIN,
                HttpUtil.formatParams(obj.toString()), UserInfo.class, new IHttpCallBack() {
                    @Override
                    public <T> void OnSuccess(String msg, ODataPage page, ArrayList<T> dataList) {
                        UserInfo userInfo = (UserInfo) dataList.get(0);
                        EApp.setUserInfo(userInfo);
                        EApp.updateUserInfoCache();
//                        SPHelper.putString("lastLogin", userInfo.getPhone());
//                        EApp.initPrivateDB();
                        login.postDelayed(() -> {
                            GO(ActMain.class);
                            finish();
                        }, 100);
                    }

                    @Override
                    public void OnFailure(String msg) {
                        showToast(msg);
                    }
                });
    }

}
