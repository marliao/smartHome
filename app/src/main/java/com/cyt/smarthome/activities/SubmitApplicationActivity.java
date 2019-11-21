package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubmitApplicationActivity extends BaseActivity {

    private CustomTitleBar mCtbSubmitApplication;
    private EditText mEtHouseholdName;
    private EditText mEtRepairContent;
    private EditText mEtRepairAddress;
    private TextView mTvRepairTime;
    private Button mBtnSubmit;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_submit_application;
    }

    @Override
    protected void initLayout() {
        mCtbSubmitApplication = findViewById(R.id.ctb_submit_application);
        mEtHouseholdName = findViewById(R.id.et_household_name);
        mEtRepairContent = findViewById(R.id.et_repair_content);
        mEtRepairAddress = findViewById(R.id.et_repair_address);
        mTvRepairTime = findViewById(R.id.tv_repair_time);
        mBtnSubmit = findViewById(R.id.btn_submit);
    }

    @Override
    protected void init() {
        initUI();
        initlistener();
    }

    private void initUI() {
        String format = new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date(System.currentTimeMillis()));
        mTvRepairTime.setText(format);
        User currentUser = BmobUser.getCurrentUser(User.class);
        mEtHouseholdName.setText(currentUser.getuName());
        mEtRepairAddress.setText(currentUser.getFloor_id() + "号楼" + currentUser.getUnit_num() + "单元" + currentUser.getFloor() + "层" + currentUser.getDoor_num() + "室");
    }

    private void initlistener() {
        mCtbSubmitApplication.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mTvRepairTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalenerDialog();
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String household_name = mEtHouseholdName.getText().toString().trim();
                String repair_address = mEtRepairAddress.getText().toString().trim();
                String repair_content = mEtRepairContent.getText().toString().trim();
                String repair_time = mTvRepairTime.getText().toString().trim();
                if (TextUtils.isEmpty(household_name)) {
                    T.showShort("请输入住户姓名");
                    return;
                }
                if (TextUtils.isEmpty(repair_address)) {
                    T.showShort("请输入报修地址");
                    return;
                }
                if (TextUtils.isEmpty(repair_content)) {
                    T.showShort("请输入报修内容");
                    return;
                }
                submitApplication(household_name, repair_address, repair_content, repair_time);
            }
        });
    }

    private void submitApplication(String household_name, String repair_address, String repair_content, String repair_time) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "0");
        params.put("household_name", household_name);
        params.put("repair_content", repair_content);
        params.put("repair_addr", repair_address);
        params.put("repair_time", repair_time);
        NetUtils.getApi().insertMalfunctionRepair(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        Log.i(TAG, "onNext: -------------------------" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            String info = jsonArray.getJSONObject(0).getString("info");
                            if (info.equals("success")) {
                                T.showShort("提交成功");
                                finish();
                            } else {
                                T.showShort("提交失败，请稍后重试");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void showCalenerDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.datepicker_dialog, null);
        final DatePicker picker = view.findViewById(R.id.date_picker);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = picker.getYear();
                int month = picker.getMonth() + 1;
                int day = picker.getDayOfMonth();
                String sDay, sMonth;

                if (day < 10) {
                    sDay = "0" + day;
                } else {
                    sDay = day + "";
                }

                if (month < 10) {
                    sMonth = "0" + month;
                } else {
                    sMonth = month + "";
                }

                String format = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

                String time = year + "-" + sMonth + "-" + sDay + "-" + format;
                mTvRepairTime.setText(time);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
