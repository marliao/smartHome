package com.cyt.smarthome.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.Maintain;
import com.cyt.smarthome.bean.MalfunctionRepair;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.cyt.smarthome.view.RecyclerAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedBackApplicationActivity extends BaseActivity {

    private CustomTitleBar mCtbMalfunctionRepair;
    private RecyclerView mRvFeedbackApplication;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_feedback_application;
    }

    @Override
    protected void initLayout() {
        mCtbMalfunctionRepair = findViewById(R.id.ctb_malfunction_repair);
        mRvFeedbackApplication = findViewById(R.id.rv_feedback_application);
    }

    @Override
    protected void init() {
        initListener();
        getSearchApplication();
    }


    private void getSearchApplication() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().searchMalfunctionRepair(params)
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
                            List<MalfunctionRepair> malfunctionRepairList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MalfunctionRepair malfunctionRepair = new MalfunctionRepair();
                                malfunctionRepair.setRepair_id(jsonArray.getJSONObject(i).getString("repair_id"));
                                malfunctionRepair.setHousehold_name(jsonArray.getJSONObject(i).getString("household_name"));;
                                malfunctionRepair.setRepair_content(jsonArray.getJSONObject(i).getString("repair_content"));;
                                malfunctionRepairList.add(malfunctionRepair);
                            }
                            List<MalfunctionRepair> currentMalfuncionRepairlist = new ArrayList<>();
                            for (MalfunctionRepair malfunctionRepair : malfunctionRepairList) {
                                if (BmobUser.getCurrentUser(User.class).getuName().equals(malfunctionRepair.getHousehold_name())) {
                                    currentMalfuncionRepairlist.add(malfunctionRepair);
                                }
                            }
                            getMalfunctionRepairStatus(currentMalfuncionRepairlist);
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

    private void getMalfunctionRepairStatus(List<MalfunctionRepair> currentMalfuncionRepairlist) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "6");
        NetUtils.getApi().searchFeedBack(params)
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
                            List<Maintain> maintainList = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Maintain maintain = new Maintain();
                                maintain.setMaintain_id(jsonArray.getJSONObject(i).getString("maintain_id"));
                                maintain.setRepair_id(jsonArray.getJSONObject(i).getString("repair_id"));
                                maintain.setRepair_name(jsonArray.getJSONObject(i).getString("repair_name"));
                                maintain.setMaintain_staff_name(jsonArray.getJSONObject(i).getString("maintain_staff_name"));
                                maintain.setMaintain_fees(jsonArray.getJSONObject(i).getString("maintain_fees"));
                                maintain.setMaintain_time(jsonArray.getJSONObject(i).getString("maintain_time"));
                                maintain.setPayment_status(jsonArray.getJSONObject(i).getString("payment_status"));
                                maintain.setMaintain_status(jsonArray.getJSONObject(i).getString("maintain_status"));
                                maintainList.add(maintain);
                            }
                            List<Maintain> currentMaintainList = new ArrayList<>();
                            for (MalfunctionRepair malfunctionRepair : currentMalfuncionRepairlist) {
                                for (Maintain maintain : maintainList) {
                                    if (malfunctionRepair.getRepair_id().equals(maintain.getRepair_id())) {
                                        maintain.setRepair_content(malfunctionRepair.getRepair_content());
                                        currentMaintainList.add(maintain);
                                    }
                                }
                            }
                            initAdapter(currentMaintainList);
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

    private void initAdapter(List<Maintain> currentMaintainList) {
        mRvFeedbackApplication.setHasFixedSize(true);
        mRvFeedbackApplication.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvFeedbackApplication.setAdapter(myAdapter);
        myAdapter.add(currentMaintainList);
    }

    public class MyAdapter extends RecyclerAdapter<Maintain> {
        @Override
        protected int getItemViewType(int position, Maintain maintain) {
            return R.layout.item_feedback_applicatioin;
        }

        @Override
        protected ViewHolder<Maintain> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Maintain> {

            public TextView tv_maintain_staff;
            public TextView tv_maintain_time;
            public TextView tv_maintain_money;
            public TextView tv_household_name;
            public TextView tv_repair_status;
            public Button btn_payment;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_maintain_staff = (TextView) itemView.findViewById(R.id.tv_maintain_staff);
                this.tv_maintain_time = (TextView) itemView.findViewById(R.id.tv_maintain_time);
                this.tv_maintain_money = (TextView) itemView.findViewById(R.id.tv_maintain_money);
                this.tv_household_name = (TextView) itemView.findViewById(R.id.tv_household_name);
                this.tv_repair_status = (TextView) itemView.findViewById(R.id.tv_repair_status);
                this.btn_payment = (Button) itemView.findViewById(R.id.btn_payment);
            }

            @Override
            protected void onBind(Maintain maintain, int position) {
                tv_maintain_staff.setText(maintain.getMaintain_staff_name());
                tv_maintain_time.setText(maintain.getMaintain_time());
                tv_maintain_money.setText(maintain.getMaintain_fees());
                tv_household_name.setText(maintain.getRepair_name());
                if (maintain.getPayment_status().equals("0")) {
                    if (maintain.getMaintain_status().equals("0")) {
                        tv_repair_status.setText("维修失败");
                        btn_payment.setEnabled(false);
                        btn_payment.setBackgroundResource(R.drawable.bg_gray_button);
                    } else {
                        tv_repair_status.setText("已维修");
                        btn_payment.setEnabled(true);
                        btn_payment.setBackgroundResource(R.drawable.bg_red_button);
                    }
                } else {
                    tv_repair_status.setText("已维修");
                    btn_payment.setText("已付款");
                    btn_payment.setEnabled(false);
                    btn_payment.setBackgroundResource(R.drawable.bg_gray_button);
                }
                btn_payment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (maintain.getPayment_status().equals("0")) {
                            if (maintain.getMaintain_status().equals("0")) {
                                T.showShort("当前无法付款");
                            } else {
                                paymentRepairMalfunction(maintain);
                            }
                        } else {
                            T.showShort("当前无法付款");
                        }
                    }
                });
            }

        }

    }

    private void paymentRepairMalfunction(Maintain maintain) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "0");
        params.put("payment_name", maintain.getRepair_content());
        params.put("payment_staff_name", BmobUser.getCurrentUser(User.class).getuName());
        params.put("pending_fee", maintain.getMaintain_fees());
        params.put("payment_status", "1");
        params.put("record_time", maintain.getMaintain_time());
        NetUtils.getApi().insertPayment(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            String info = jsonArray.getJSONObject(0).getString("info");
                            if (info.equals("success")) {
                                update(maintain.getMaintain_id());
                            } else {
                                T.showShort("付款失败，请稍后重试");
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

    private void update(String maintain_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "3");
        params.put("payment_status", "1");
        params.put("maintain_id", maintain_id);
        NetUtils.getApi().selectAllMaintain(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        Log.i(TAG, "onNext: --------------------"+result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            String info = jsonArray.getJSONObject(0).getString("info");
                            if (info.equals("success")) {
                                T.showShort("付款成功");
                                finish();
                            } else {
                                T.showShort("付款失败，请稍后重试");
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

    private void initListener() {
        mCtbMalfunctionRepair.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }
}
