package com.cyt.smarthome.activities;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.HouseKeepHistory;
import com.cyt.smarthome.bean.Propertypay;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ServiceRecordDetailsActivity extends BaseActivity {
    private CustomTitleBar ctbMalfunctionRepair;
    private TextView tvMaintainStaff;
    private TextView tvServiceContent;
    private TextView tvMaintainTime;
    private TextView tv_serviceDuration;
    private TextView tvMaintainMoney;
    private TextView tvHouseholdName;
    private TextView tvServiceStatus;
    private TextView tvRepairStatus;
    private EditText sp1;
    private Button btSave;
    private HouseKeepHistory houseKeepHistory;

    @Override
    protected int getLayout() {
        return R.layout.activity_servicerecord_details;
    }

    @Override
    protected void initLayout() {
        ctbMalfunctionRepair = (CustomTitleBar) findViewById(R.id.ctb_malfunction_repair);
        tvMaintainStaff = (TextView) findViewById(R.id.tv_maintain_staff);
        tvServiceContent = (TextView) findViewById(R.id.tv_serviceContent);
        tvMaintainTime = (TextView) findViewById(R.id.tv_maintain_time);
        tv_serviceDuration = (TextView) findViewById(R.id.tv_serviceDuration);
        tvMaintainMoney = (TextView) findViewById(R.id.tv_maintain_money);
        tvHouseholdName = (TextView) findViewById(R.id.tv_household_name);
        tvServiceStatus = (TextView) findViewById(R.id.tv_service_status);
        tvRepairStatus = (TextView) findViewById(R.id.tv_repair_status);
        sp1 = (EditText) findViewById(R.id.sp_1);
        btSave = (Button) findViewById(R.id.bt_save);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String ahousekeeping_id = intent.getStringExtra("Ahousekeeping_id");
        List<HouseKeepHistory> list1 = AppClient.list1;
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getAhousekeeping_id().equals(ahousekeeping_id)) {
                houseKeepHistory = list1.get(i);
            }
        }
        setView();
        InitListener();
    }

    private void InitListener() {
        ctbMalfunctionRepair.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String s = sp1.getText().toString();
                                          if (TextUtils.isEmpty(s)) {
                                              T.showShort("请先填人评价");
                                          } else {
                                              SaveRating(s);
                                          }
                                      }
                                  }
        );
    }

    private void SaveRating(String s) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "2");
        params.put("housekeeping_assess", s);
        params.put("housekeeping_id", houseKeepHistory.getHousekeeping_id());
        NetUtils.getApi().housekeeaping(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
//                        System.out.println(":" + result);
                        if (result.contains("success")) {
                            T.show("保存成功(ˇ∀ˇ)", 1000);
                            finish();
                        } else {
                            T.show("保存失败(⊙﹏⊙)", 1000);
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

    private void setView() {
        tvMaintainStaff.setText(houseKeepHistory.getAhousekeeping_name());
        tvServiceContent.setText(houseKeepHistory.getHousekeeping_content());
        tvMaintainTime.setText(houseKeepHistory.getHousekeeping_time());
        tv_serviceDuration.setText(houseKeepHistory.getHousekeeping_duration() + "小时");
        tvMaintainMoney.setText(houseKeepHistory.getHousekeeping_fees() + "元");
        tvHouseholdName.setText(houseKeepHistory.getHousekeeping_staff_name());
        if (houseKeepHistory.getHousekeeping_status().equals("1")) {
            tvServiceStatus.setText("服务中...");
        } else {
            tvServiceStatus.setText("服务已完成");
        }
        if (houseKeepHistory.getPayment_status().equals("1")) {
            tvRepairStatus.setText("已缴费");
        } else {
            tvRepairStatus.setText("未缴费");
        }
        if (houseKeepHistory.getHousekeeping_assess() != null) {
            sp1.setText(houseKeepHistory.getHousekeeping_assess().toString());
        }
    }
}
