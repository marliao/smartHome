package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.DateUtils;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.RegexUtils;
import com.cyt.smarthome.utils.SpUtils;
import com.cyt.smarthome.utils.StaticClass;
import com.cyt.smarthome.utils.T;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户登录页面
 */
public class LoginActivity extends BaseActivity {
    private EditText mEtPhone;
    private EditText mEtPwd;
    private CheckBox mCbRemember;
    private CheckBox mCbAutoLogin;
    private Button mBtnRegister;
    private Button mBtnLogin;
    private AlertDialog alertDialog;
    private Disposable disposable;

    /**
     * 初始化布局
     */
    @Override
    protected void initLayout() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mCbRemember = (CheckBox) findViewById(R.id.cb_remember);
        mCbAutoLogin = (CheckBox) findViewById(R.id.cb_auto_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    /**
     * 获取子aitivity的布局ID
     *
     * @return
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    /**
     * 子类activity的初始化方法
     */
    @Override
    protected void init() {
        initData();
        initEvent();
    }


    /**
     * 初始化事件
     */
    private void initEvent() {
        //用户登录
        mBtnLogin.setOnClickListener(v -> {
            login();
        });

        //注册
        mBtnRegister.setOnClickListener(v -> {
            showRegisterDialog();
        });

        mCbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mCbRemember.setChecked(true);
            }
        });


        //记住密码
        mCbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    mCbAutoLogin.setChecked(false);
            }
        });


    }

    /**
     * 用户登录
     */
    private void login() {
        String phoneStr = mEtPhone.getText().toString().trim();
        //验证手机号码
        if (!RegexUtils.checkPhone(phoneStr)) {
            T.showShort("手机号码有误");
            return;
        }

        String pwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            T.showShort("密码不能为空");
            return;
        }


        //进行登录
        BmobUser.loginByAccount(phoneStr, pwd, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //保存密码
                    savePassword(phoneStr, pwd);
                    //跳转到下个界面
                    gotoMainActivity();
                } else {
                    //登录失败
                    T.showShort("登录失败,请重试");
                    L.e("--------------登录失败错误信息:" + e.getErrorCode() + "----" + e.getMessage());
                }
            }

        });
    }

    private void gotoMainActivity() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        int level = currentUser.getLevel();
        switch (level) {
            case 1://业主
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            case 2://住户
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }

    }

    /**
     * 保存密码的业务逻辑
     *
     * @param phoneStr 用户名
     * @param pwd      密码
     */
    private void savePassword(String phoneStr, String pwd) {
        boolean isAutoLogin = mCbAutoLogin.isChecked();
        boolean isRememberPassword = mCbRemember.isChecked();
        SpUtils.put(AppClient.mContext, StaticClass.isAutoLogin, isAutoLogin);
        SpUtils.put(AppClient.mContext, StaticClass.isRememberPassword, isRememberPassword);
        if (isAutoLogin || isRememberPassword) {
            SpUtils.put(AppClient.mContext, StaticClass.USERNAME, phoneStr);
            SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, pwd);
        } else {
            SpUtils.put(AppClient.mContext, StaticClass.USERNAME, "");
            SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, "");
        }
    }


    /**
     * 显示注册的对话框
     */
    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.layout_register_dialog, null);
        builder.setView(view);
        EditText mEtPhoneNumber;
        EditText mEtVerificationCode;
        Button mBtnVerificationCode;
        EditText mEtPassword;
        Button mBtnDialogCancel;
        Button mBtnDialogRegister;
        mEtPhoneNumber = (EditText) view.findViewById(R.id.et_phone_number);
        mEtVerificationCode = (EditText) view.findViewById(R.id.et_verification_code);
        mBtnVerificationCode = (Button) view.findViewById(R.id.btn_verification_code);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mBtnDialogCancel = (Button) view.findViewById(R.id.btn_dialog_cancel);
        mBtnDialogRegister = (Button) view.findViewById(R.id.btn_dialog_register);
        EditText mEtIdcard;
        RadioButton mIbOwner;
        RadioButton mIbHousehold;
        Spinner mSpFloorId;
        Spinner mSpUnitNum;
        Spinner mSpFloor;
        Spinner mSpDoorNum;
        mEtIdcard = (EditText) view.findViewById(R.id.et_idcard);
        mIbOwner = (RadioButton) view.findViewById(R.id.ib_owner);
        mIbHousehold = (RadioButton) view.findViewById(R.id.ib_household);
        mSpFloorId = (Spinner) view.findViewById(R.id.sp_floor_id);
        mSpUnitNum = (Spinner) view.findViewById(R.id.sp_unit_num);
        mSpFloor = (Spinner) view.findViewById(R.id.sp_floor);
        mSpDoorNum = (Spinner) view.findViewById(R.id.sp_door_num);
        EditText mEtUname;
        mEtUname = (EditText) view.findViewById(R.id.et_uname);
        LinearLayout mLlOwner;
        EditText mEtOwnerName;
        mLlOwner = (LinearLayout) view.findViewById(R.id.ll_owner);
        mEtOwnerName = (EditText) view.findViewById(R.id.et_owner_name);

        mIbHousehold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLlOwner.setVisibility(View.VISIBLE);
                } else {
                    mLlOwner.setVisibility(View.GONE);
                }
            }
        });


        //楼层选择的监听,设置对象的501室等
        mSpFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpDoorNum.setAdapter(new ArrayAdapter<String>(LoginActivity.this,
                        R.layout.item_texiview, new String[]{(position + 1) + "01", (position + 1) + "02"}));
                mSpDoorNum.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //发送验证码
        mBtnVerificationCode.setOnClickListener(v -> sendSmsCode(mEtPhoneNumber.getText().toString(), mBtnVerificationCode));
        mBtnDialogCancel.setOnClickListener(v -> alertDialog.dismiss());
        //注册
        mBtnDialogRegister.setOnClickListener(v -> register(
                mEtUname.getText().toString().trim(),
                mEtVerificationCode.getText().toString().trim(), //验证码
                mEtPhoneNumber.getText().toString(),//手机号
                mEtPassword.getText().toString(),//密码
                mEtIdcard.getText().toString(),//身份证号码
                mSpFloorId.getSelectedItemPosition(),//楼盘
                mSpUnitNum.getSelectedItemPosition(),//单元
                mSpFloor.getSelectedItemPosition(),//楼层
                mSpDoorNum.getSelectedItemPosition(),//室
                mIbOwner.isChecked(),
                mEtOwnerName.getText().toString().trim()//业主名字
        ));
        alertDialog = builder.create();
        alertDialog.show();

        Long sendTime = (Long) SpUtils.get(AppClient.mContext, StaticClass.DATE_DIFFERENCE, 0L);
        //发送验证码按钮状态回显
        Date date = new Date();
        Long dateDifference = DateUtils.getDateDifference(date, new Date(sendTime));
        if (dateDifference > 0 && dateDifference < 60) {
            sendCodeStatus(mBtnVerificationCode, 60 - Integer.valueOf(dateDifference.toString()));
        }
    }

    /**
     * 发送验证码的业务
     *
     * @param phone 手机号码
     */
    private void sendSmsCode(String phone, Button mBtnVerificationCode) {
        L.e("----phone-----------" + phone);
        //验证手机号码
        if (!RegexUtils.checkMobile(phone)) {
            T.showShort("手机号码有误");
            return;
        }

        //请求发送验证码
        BmobSMS.requestSMSCode(phone, null, new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    T.showShort("发送验证码成功，短信ID：" + smsId + "\n");
                    //按钮倒计时
                    sendCodeStatus(mBtnVerificationCode, 60);
                    //记录发送验证码的时间
                    SpUtils.put(AppClient.mContext, StaticClass.DATE_DIFFERENCE, new Date().getTime());
                } else {
                    //更详细的以后再说
                   // T.showShort("发送验证码失败请稍后重试");
                    T.showShort(e.getErrorCode()+"--"+e.getMessage());
                }
            }
        });

    }

    /**
     * 发送短信按钮倒计时
     *
     * @param mBtnVerificationCode
     * @param seconds
     */
    private void sendCodeStatus(Button mBtnVerificationCode, int seconds) {
        mBtnVerificationCode.setBackgroundResource(R.drawable.bg_gray_button);
        mBtnVerificationCode.setEnabled(false);
        disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
                .take(seconds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mBtnVerificationCode.setText((seconds - aLong) + "秒后获取");
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


    /**
     * 注册的业务逻辑
     *
     * @param uname      用户名
     * @param code       验证码
     * @param phone      手机号
     * @param pwd        密码
     * @param idCard     身份证号码
     * @param floor_id   楼盘
     * @param unit       单元
     * @param floor      楼层
     * @param door_num   房间号码
     * @param checked    业主是否被选中
     * @param owner_name 业主姓名
     */
    private void register(String uname, String code, String phone, String pwd, String idCard,
                          int floor_id, int unit, int floor, int door_num, boolean checked, String owner_name) {

        if (checkParams(uname, phone, pwd, idCard, checked, owner_name)) return;

        //进行手机号注册
        BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                //验证完成后,关闭弹窗
                alertDialog.dismiss();
                if (e == null) {
                    saveUser(phone, uname, checked, owner_name, idCard, floor, floor_id, unit, door_num, pwd);
                } else {
                    T.showShort("验证码验证失败：" + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 参数判断,是否符合要求
     *
     * @param uname
     * @param phone
     * @param pwd
     * @param idCard
     * @param checked
     * @param owner_name
     * @return
     */
    private boolean checkParams(String uname, String phone, String pwd, String idCard, boolean checked, String owner_name) {
        if (TextUtils.isEmpty(uname)) {
            T.showShort("姓名不能为空");
            return true;
        }
        //验证手机号码
        if (!RegexUtils.checkMobile(phone)) {
            T.showShort("手机号码有误");
            return true;
        }

        if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
            T.showShort("密码长度最少为6位");
            return true;
        }
        //验证身份证号码
        if (!RegexUtils.checkIdCard(idCard)) {
            T.showShort("身份证号码格式不符合要求");
            return true;
        }

        if (!checked) {
            if (TextUtils.isEmpty(owner_name)) {
                T.showShort("业主姓名不能为空");
                return true;
            }
        }
        return false;
    }

    /**
     * 保存用户
     *
     * @param phone
     * @param uname
     * @param checked
     * @param owner_name
     * @param idCard
     * @param floor
     * @param floor_id
     * @param unit
     * @param door_num
     * @param pwd
     */
    private void saveUser(String phone, String uname, boolean checked, String owner_name, String idCard, int floor, int floor_id, int unit, int door_num, String pwd) {
        User currentUser = BmobUser.getCurrentUser(User.class);
        currentUser.setMobilePhoneNumber(phone);
        currentUser.setuName(uname);
        if (checked) {
            currentUser.setLevel(1);//业主
        } else {
            currentUser.setLevel(2);//住户
            currentUser.setOwner_name(owner_name);
        }
        currentUser.setIdentity_num(idCard);
        currentUser.setSex(Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1)) % 2 == 1 ? 0 : 1);
        currentUser.setFloor(floor + 1);
        currentUser.setFloor_id(floor_id + 1);
        currentUser.setUnit_num(unit + 1);
        currentUser.setDoor_num(Integer.parseInt(currentUser.getFloor() + "0" + (door_num + 1)));
        currentUser.setMobilePhoneNumberVerified(true);
        currentUser.setPassword(pwd);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    T.showShort("注册成功,请使用手机号+密码登录");
                    HashMap<String, String> params = new HashMap<>();
                    String housing_num = currentUser.getFloor_id() + "号楼"
                            + currentUser.getUnit_num() + "单元" +
                            currentUser.getFloor() + "层" +
                            currentUser.getDoor_num() + "室";
                    params.put("method_id", "0");
                    params.put("housing_num", housing_num);

                    //将该数据插入到服务器数据库中
                    if (currentUser.getLevel() == 1) {
                        //数据插入到业主表
                        addOwner(params, currentUser);
                    } else {
                        addHouser(params, currentUser);
                    }
                } else {
                    regexError(e);
                }
            }
        });
    }

    /**
     * 错误码识别
     *
     * @param e
     */
    private void regexError(BmobException e) {
        L.e(e.getErrorCode() + "------------------" + e.getMessage());
        switch (e.getErrorCode()) {
            case 202:
                T.showShort("用户名已经存在");
                break;
            case 207:
                T.showShort("验证码错误");
                break;
            case 209:
                T.showShort("该手机号码已经存在");
                break;
            default:
                T.showShort("验证失败，请稍后重试");
                break;
        }
    }

    /**
     * 向服务器中插入一条住户数据
     *
     * @param params
     * @param currentUser
     */
    private void addHouser(HashMap<String, String> params, User currentUser) {
        params.put("household_username", currentUser.getuName());
        params.put("household_name", currentUser.getuName());
        params.put("household_sex", currentUser.getSex() + "");
        params.put("household_phone", currentUser.getMobilePhoneNumber());
        params.put("identity_num", currentUser.getIdentity_num());
        params.put("owner_name", currentUser.getOwner_name());
        //数据插入到住户表
        NetUtils.getApi().addHouser(params).subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        L.e("----------------------" + result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("----------------------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 添加业主
     *
     * @param params
     * @param currentUser
     */
    private void addOwner(HashMap<String, String> params, User currentUser) {
        params.put("owner_username", currentUser.getuName());
        params.put("owner_name", currentUser.getuName());
        params.put("owner_sex", currentUser.getSex() + "");
        params.put("owner_phone", currentUser.getMobilePhoneNumber());
        params.put("identity_num", currentUser.getIdentity_num());
        NetUtils.getApi().addOwener(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        L.e("----------------------" + result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("----onError------------------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //用户名和密码回显
        Boolean isRememberPassword = (Boolean) SpUtils.get(AppClient.mContext, StaticClass.isRememberPassword, false);
        Boolean isAutoLogin = (Boolean) SpUtils.get(AppClient.mContext, StaticClass.isAutoLogin, false);
        mCbRemember.setChecked(isRememberPassword);
        mCbAutoLogin.setChecked(isAutoLogin);
        if (isRememberPassword) {
            mEtPwd.setText((String) SpUtils.get(AppClient.mContext, StaticClass.PASSWORD, ""));
            mEtPhone.setText((String) SpUtils.get(AppClient.mContext, StaticClass.USERNAME, ""));
        }

        if (isAutoLogin) {
            login();
        }

        //test();
    }

    /**
     * 添加一个业主,测试
     */
    private void test() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("owner_username", currentUser.getuName());
        params.put("owner_name", currentUser.getuName());
        params.put("owner_sex", currentUser.getSex() + "");
        params.put("owner_phone", currentUser.getMobilePhoneNumber());
        params.put("identity_num", currentUser.getIdentity_num());
        String housing_num = currentUser.getFloor_id() + "号楼"
                + currentUser.getUnit_num() + "单元" +
                currentUser.getFloor() + "层" +
                currentUser.getDoor_num() + "室";
        params.put("method_id", "0");
        params.put("housing_num", housing_num);


//        params.put("household_username", currentUser.getuName());
//        params.put("household_name", currentUser.getuName());
//        params.put("household_sex", currentUser.getSex() + "");
//        params.put("household_phone", currentUser.getMobilePhoneNumber());
//        params.put("identity_num", currentUser.getIdentity_num());
//        params.put("owner_name", "小柒1");
//        //数据插入到住户表
//        NetUtils.getApi().addHouser(params).subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
//                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String result) {
//                        L.e("----------------------" + result);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        L.e("---------addHouser-------------" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        NetUtils.getApi().addOwener(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        L.e("----------------------" + result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("----onError------------------" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
