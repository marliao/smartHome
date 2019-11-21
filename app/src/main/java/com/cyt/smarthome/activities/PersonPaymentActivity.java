package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.Propertypay;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.cyt.smarthome.view.RecyclerAdapter;
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

public class PersonPaymentActivity extends BaseActivity {
    private CustomTitleBar tvPropertyPayment;
    private RecyclerView rvPropertyPayment;

    @Override
    protected int getLayout() {
        return R.layout.activity_property_payment;
    }

    @Override
    protected void initLayout() {
        tvPropertyPayment = (CustomTitleBar) findViewById(R.id.tv_property_payment);
        rvPropertyPayment = (RecyclerView) findViewById(R.id.rv_property_payment);
    }

    @Override
    protected void init() {
        initlistenger();
        initData();
    }

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllPropertyPayment(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println(":" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            List<Propertypay> PropertypayArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Propertypay Propertypay = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Propertypay.class);
                                if(Propertypay.getPayment_staff_name().equals(BmobUser.getCurrentUser(User.class).getuName())) {
                                    PropertypayArrayList.add(Propertypay);
                                }
                            }
                            initAdapter(PropertypayArrayList);
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

    private void initAdapter(List<Propertypay> peripheralServiceList) {
        rvPropertyPayment.setHasFixedSize(true);
        rvPropertyPayment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        rvPropertyPayment.setAdapter(myAdapter);
        myAdapter.add(peripheralServiceList);
        myAdapter.setListener(new RecyclerAdapter.AdapterListener<Propertypay>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Propertypay propertypay, int pos) {
                Propertypay propertypay1 = peripheralServiceList.get(pos);
                if(propertypay1.getPayment_status().equals("0")){
                    PayDialog(propertypay1);
                }
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Propertypay propertypay, int pos) {

            }
        });
    }

    private void PayDialog(Propertypay propertypay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonPaymentActivity.this);
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
                        if(result.contains("success")){
                            initData();
                            T.show("支付成功(ˇ∀ˇ)",1000);
                        }else {
                            T.show("支付失败(⊙﹏⊙)",1000);
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

    public class MyAdapter extends RecyclerAdapter<Propertypay> {

        @Override
        protected int getItemViewType(int position, Propertypay peripheralService) {
            return R.layout.item_propertypay;
        }

        @Override
        protected ViewHolder<Propertypay> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Propertypay> {
            private TextView tvSuggestionId;
            private TextView tvSuggestionCounter;
            private TextView tvMoney;
            private TextView tvTime;
            private TextView tvType;
            private TextView tvJfsj;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvSuggestionId = (TextView) itemView.findViewById(R.id.tv_suggestion_id);
                tvSuggestionCounter = (TextView) itemView.findViewById(R.id.tv_suggestion_counter);
                tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvType = (TextView) itemView.findViewById(R.id.tv_type);
                tvJfsj = (TextView) itemView.findViewById(R.id.tv_jfsj);
            }

            @Override
            protected void onBind(Propertypay peripheralService, int position) {
                tvSuggestionId.setText(peripheralService.getPayment_staff_name());
                if (peripheralService.getPayment_status().equals("0")) {
                    tvSuggestionCounter.setText("未缴费");
                    tvJfsj.setText("");
                } else {
                    tvSuggestionCounter.setText("已缴费");
                    if (peripheralService.getPayment_time() != null)
                        tvJfsj.setText("" + peripheralService.getPayment_time().toString());
                }
                tvMoney.setText("待缴费用：" + peripheralService.getPending_fee());
                tvTime.setText("记录时间：" + peripheralService.getRecord_time());
                tvType.setText("缴费类型：" + peripheralService.getPayment_name());

//                tv_surroundings_name.setText(peripheralService.getSurrounding_name());
//                tv_surroundings_content.setText("\t\t\t" + peripheralService.getSurrounding_content());
//                tv_surroundings_addr.setText("地址：" + peripheralService.getSurrounding_addr());
//                tv_surroundings_phone.setText("联系电话：" + peripheralService.getSurrounding_phone());
            }
        }
    }

    private void initlistenger() {
        tvPropertyPayment.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
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
