package com.cyt.smarthome.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.utils.ErrorRec;
import com.cyt.smarthome.utils.RegexUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResetPasswordActivity extends BaseActivity {

    private CustomTitleBar mCtbCommunityNews;
    private EditText mEtNumber;
    private EditText mEtVerificationCode;
    private Button mBtnVerificationCode;
    private EditText mEtPassword;
    private Button mBtnReset;
    private Disposable disposable;
    private String mPhoneNumber;
    private String mPassowrd;
    private String mVerificationCode;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initLayout() {
        mCtbCommunityNews = findViewById(R.id.ctb_community_news);
        mEtNumber = findViewById(R.id.et_number);
        mEtVerificationCode = findViewById(R.id.et_verification_code);
        mBtnVerificationCode = findViewById(R.id.btn_verification_code);
        mEtPassword = findViewById(R.id.et_password);
        mBtnReset = findViewById(R.id.btn_reset);
    }

    @Override
    protected void init() {
        initlistener();
    }

    private void initlistener() {
        mCtbCommunityNews.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mBtnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificationCode();
            }
        });
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumber = mEtNumber.getText().toString().trim();
                mPassowrd = mEtPassword.getText().toString().trim();
                mVerificationCode = mEtVerificationCode.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    T.showShort("请输入您的手机号");
                    return;
                }
                if (TextUtils.isEmpty(mVerificationCode)) {
                    T.showShort("请输入您收到的短信验证码");
                    return;
                }
                if (TextUtils.isEmpty(mPassowrd)) {
                    T.showShort("请输入您的新密码");
                    return;
                }
                BmobUser.resetPasswordBySMSCode(mVerificationCode, mPassowrd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("修改成功！");
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            finish();
                        } else {
                            ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                        }
                    }
                });
            }
        });
    }


    private void Countdown() {
        disposable = Flowable.interval(1, 1, TimeUnit.SECONDS)
                .take(60)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mBtnVerificationCode.setText((60 - aLong) + "秒后获取");
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mBtnVerificationCode.setText("获取验证码");
                        mBtnVerificationCode.setEnabled(true);
                        mBtnVerificationCode.setBackgroundResource(R.drawable.bg_blue_button);
                    }
                }).subscribe();
    }

    private void VerificationCode() {
        mPhoneNumber = mEtNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(mPhoneNumber)) {
            if (RegexUtils.checkPhone(mPhoneNumber)) {
                //按钮失去焦点，开始60秒倒计时，并请求短信验证码
                mBtnVerificationCode.setEnabled(false);
                mBtnVerificationCode.setBackgroundResource(R.drawable.bg_gray_button);
                Countdown();
                GetVerificationCode(mPhoneNumber);
            } else {
                T.showShort("您输入的手机号码不合法");
            }
        } else {
            T.showShort("请输入手机号码");
        }
    }


    private void GetVerificationCode(String phoneNumber) {
        BmobSMS.requestSMSCode(phoneNumber, "smartHom", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (disposable != null) {
            if (disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }


}
