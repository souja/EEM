package com.byt.eemblue.act;

import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;

import com.byt.eemblue.EApp;
import com.byt.eemblue.R;
import com.byt.eemblue.base.BaseActEd;
import com.byt.eemblue.model.UserInfo;
import com.byt.eemblue.util.HttpUtil;
import com.byt.eemblue.util.MConstants;
import com.souja.lib.inter.IHttpCallBack;
import com.souja.lib.models.BaseModel;
import com.souja.lib.models.ODataPage;
import com.souja.lib.widget.LoadingDialog;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

//test(user 1)
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
        Post(getDialog(), MConstants.URL.LOGIN,
                HttpUtil.formatParams(new LoginParam(phone, pwd).toString()),
                UserInfo.class, new IHttpCallBack<UserInfo>() {

                    @Override
                    public void OnSuccess(String msg, ODataPage page, ArrayList<UserInfo> data) {
                        UserInfo userInfo = data.get(0);
                        LogUtil.e(userInfo.getUserName() + " " + userInfo.getRoleName());
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

    class LoginParam extends BaseModel {
        String UserId;
        String PassWord;

        public LoginParam(String userId, String passWord) {
            UserId = userId;
            PassWord = passWord;
        }
    }

}
