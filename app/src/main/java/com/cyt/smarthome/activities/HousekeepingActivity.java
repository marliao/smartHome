package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.HouseKeepHistory;
import com.cyt.smarthome.bean.HouseKeeps;
import com.cyt.smarthome.bean.Propertypay;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.frags.MyPayFragemnt;
import com.cyt.smarthome.frags.MyPayLogFragemnt;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HousekeepingActivity extends BaseActivity {
    private CustomTitleBar tvPropertyPayment;
    private TextView tv1;
    private View v2;
    private TextView tv2;
    private View v1;
    private boolean processing = false;
    private User user;
    private List<HouseKeepHistory> propertypayArrayList;
    private List<HouseKeeps> propertypayArrayList1;
    private FrameLayout myFl;

    @Override
    protected int getLayout() {
        return R.layout.activity_housekeeping;
    }

    @Override
    protected void initLayout() {
        tvPropertyPayment = (CustomTitleBar) findViewById(R.id.tv_property_payment);
        tv1 = (TextView) findViewById(R.id.tv_1);
        v2 = (View) findViewById(R.id.v2);
        tv2 = (TextView) findViewById(R.id.tv_2);
        v1 = (View) findViewById(R.id.v1);
//        rvHousekeeping = (RecyclerView)  findViewById(R.id.rv_housekeeping);
        myFl = (FrameLayout) findViewById(R.id.my_fl);
    }


    @Override
    protected void init() {
        initlistenger();
        initData();
    }

    private void initData() {
        if (user == null) {
            user = BmobUser.getCurrentUser(User.class);
        }
        Getproess();
        GetHistoricalOrder();
    }

    private void GetHistoricalOrder() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "6");
        NetUtils.getApi().housekeeaping(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println("GetHistoricalOrder:" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            if (propertypayArrayList == null) {
                                propertypayArrayList = new ArrayList<>();
                            } else {
                                propertypayArrayList.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HouseKeepHistory Propertypay = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), HouseKeepHistory.class);
                                if (Propertypay.getAhousekeeping_name().equals(user.getuName())) {
                                    propertypayArrayList.add(Propertypay);
                                }
                            }
                            AppClient.list1 = propertypayArrayList;
                            initAdapter();
                            Log.i(TAG, "onNext: ----------------" + result);
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

    private void Getproess() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
//        params.put("ahousekeeping_staff_name", user.getuName());
        NetUtils.getApi().ahousekeeaping(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println("Getproess:" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            if (propertypayArrayList1 == null) {
                                propertypayArrayList1 = new ArrayList<>();
                            } else {
                                propertypayArrayList1.clear();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HouseKeeps Propertypay = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), HouseKeeps.class);
                                 if (Propertypay.getAhousekeeping_staff_name().equals(BmobUser.getCurrentUser(User.class).getuName())) {
                                propertypayArrayList1.add(Propertypay);
                                  }
                            }
                            AppClient.list = propertypayArrayList1;
                            initAdapter();
                            Log.i(TAG, "onNext: ----------------" + result);
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

    private void initAdapter() {
//        if(!processing){
//            rvHousekeeping.setHasFixedSize(true);
//            rvHousekeeping.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            MyAdapter myAdapter = new MyAdapter();
//            rvHousekeeping.setAdapter(myAdapter);
//            myAdapter.add(propertypayArrayList1);
//        }else {
//            rvHousekeeping.setHasFixedSize(true);
//            rvHousekeeping.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            MyAdapter myAdapter = new MyAdapter();
//            rvHousekeeping.setAdapter(myAdapter);
//            myAdapter.add(propertypayArrayList);
//        }
        if (!processing) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.my_fl, new MyPayFragemnt()).commit();
            L.e("processing1:" + processing);
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.my_fl, new MyPayLogFragemnt()).commit();
            L.e("processing2:" + processing);
        }
    }

    private void PayDialog(Propertypay propertypay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HousekeepingActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(getApplicationContext(), R.layout.pay_dialog, null);
        TextView tvPayContent;
        Button btnDialogCancel;
        Button btnDialogPay;
        tvPayContent = (TextView) inflate.findViewById(R.id.tv_pay_content);
        btnDialogCancel = (Button) inflate.findViewById(R.id.btn_dialog_cancel);
        btnDialogPay = (Button) inflate.findViewById(R.id.btn_dialog_pay);
        alertDialog.setView(inflate);
        alertDialog.show();
        tvPayContent.setText("确认支付" + propertypay.getPayment_name() + ":" + propertypay.getPending_fee() + "元。");
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnDialogPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayMoney(propertypay);
                alertDialog.dismiss();
            }
        });
    }

    private void PayMoney(Propertypay propertypay) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "2");
        params.put("payment_id", propertypay.getPayment_id());
        params.put("payment_time", new SimpleDateFormat("yyyy-M-d").format(System.currentTimeMillis()));
        params.put("payment_status", "1");
        NetUtils.getApi().selectAllPropertyPayment(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        L.e(result);
                        if (result.contains("success")) {
                            initData();
                            T.show("支付成功(ˇ∀ˇ)", 1000);
                        } else {
                            T.show("支付失败(⊙﹏⊙)", 1000);
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


    private void initlistenger() {
        tvPropertyPayment.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                ShowAddSuggestionAlter();
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.GONE);
                tv1.setTextColor(getResources().getColor(R.color.write));
                tv2.setTextColor(getResources().getColor(R.color.line_color_gray_80999999));
                processing = false;
                initAdapter();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.GONE);
                tv2.setTextColor(getResources().getColor(R.color.write));
                tv1.setTextColor(getResources().getColor(R.color.line_color_gray_80999999));
                processing = true;
                initAdapter();
            }
        });

    }

    private void ShowAddSuggestionAlter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HousekeepingActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(getApplicationContext(), R.layout.add_house, null);
        alertDialog.setView(inflate);
        alertDialog.show();
        CustomTitleBar ctbSubmitApplication;
        EditText etHouseholdName;
        EditText etRepairContent;
        EditText etRepairAddress;
        TextView tvRepairTime;
        Button btnCancel;
        Button btnSubmit;

        ctbSubmitApplication = (CustomTitleBar) inflate.findViewById(R.id.ctb_submit_application);
        etHouseholdName = (EditText) inflate.findViewById(R.id.et_household_name);
        etRepairContent = (EditText) inflate.findViewById(R.id.et_repair_content);
        etRepairAddress = (EditText) inflate.findViewById(R.id.et_repair_address);
        tvRepairTime = (TextView) inflate.findViewById(R.id.tv_repair_time);
        btnCancel = (Button) inflate.findViewById(R.id.btn_cancel);
        btnSubmit = (Button) inflate.findViewById(R.id.btn_submit);
        tvRepairTime.setText(new SimpleDateFormat("yyyyy-M-d-HH-mm").format(System.currentTimeMillis()));
        etHouseholdName.setText(user.getuName());
        etRepairAddress.setText(user.getFloor_id() + "号楼" + user.getUnit_num() + "单元" + user.getFloor() + "层" + user.getDoor_num() + "室");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etHouseholdName.getText().toString();
                String s1 = etRepairContent.getText().toString();
                String s2 = etRepairAddress.getText().toString();

                if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)) {
                    T.show("请先填写建议内容(´ｰ∀ｰ`)", 1000);
                } else {
                    AddSuggestionAlter(s, s1, s2);
                    alertDialog.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void AddSuggestionAlter(String s2, String s1, String s) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "0");
        params.put("ahousekeeping_staff_name", s2);
        params.put("ahousekeeping_content", s1);
        params.put("ahousekeeping_addr", s);
        params.put("ahousekeeping_time", new SimpleDateFormat("yyyy-M-d-HH-mm").format(System.currentTimeMillis()));
//        params.put("property_feedback", null);
        NetUtils.getApi().ahousekeeaping(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println("----" + result);
                        if (result.contains("success")) {
                            initData();
                            T.show("添加成功(ˇ∀ˇ)", 1000);
                        } else {
                            T.show("添加失败(⊙﹏⊙)", 1000);
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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
