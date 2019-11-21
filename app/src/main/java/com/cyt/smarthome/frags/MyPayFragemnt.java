package com.cyt.smarthome.frags;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.activities.PersonPaymentActivity;
import com.cyt.smarthome.activities.ServiceRecordDetailsActivity;
import com.cyt.smarthome.bean.HouseKeepHistory;
import com.cyt.smarthome.bean.HouseKeeps;

import com.cyt.smarthome.bean.Propertypay;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.RecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyPayFragemnt extends BaseFragment {

    private RecyclerView rvHousekeeping;
    private List<HouseKeeps> list;

    @Override
    protected int getLayout() {
        return R.layout.frag_pay;
    }

    @Override
    protected void initView(View view) {
        rvHousekeeping = (RecyclerView) view.findViewById(R.id.rv_property_payment);
    }

    @Override
    protected void init() {
        list = AppClient.list;
        L.i("TAG", list.size() + "");
        rvHousekeeping.setHasFixedSize(true);
        rvHousekeeping.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        rvHousekeeping.setAdapter(myAdapter);
        myAdapter.add(list);
        myAdapter.setListener(new RecyclerAdapter.AdapterListener<HouseKeeps>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, HouseKeeps houseKeeps, int pos) {
                List<HouseKeepHistory> list1 = AppClient.list1;
                boolean isfk = true;
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getAhousekeeping_id().equals(houseKeeps.getAhousekeeping_id())) {
                        isfk = false;
                    }
                }
                if (!isfk) {
                    Intent intent = new Intent(getContext(), ServiceRecordDetailsActivity.class);
                    intent.putExtra("Ahousekeeping_id", houseKeeps.getAhousekeeping_id());
                    startActivity(intent);
                }else {
                    T.showShort("尚未反馈");
                }
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, HouseKeeps houseKeeps, int pos) {
                List<HouseKeepHistory> list1 = AppClient.list1;
                boolean isfk = true;
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getAhousekeeping_id().equals(houseKeeps.getAhousekeeping_id())) {
                        isfk = false;
                    }
                }
                if (isfk) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    AlertDialog alertDialog = builder.create();
                    View inflate = View.inflate(getContext(), R.layout.delete_house, null);
                    TextView tvPayContent;
                    Button btnDialogCancel;
                    Button btnDialogPay;
                    tvPayContent = (TextView) inflate.findViewById(R.id.tv_pay_content);
                    btnDialogCancel = (Button) inflate.findViewById(R.id.btn_dialog_cancel);
                    btnDialogPay = (Button) inflate.findViewById(R.id.btn_dialog_pay);
                    alertDialog.setView(inflate);
                    alertDialog.show();
                    tvPayContent.setText("确认删除该订单");
                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    btnDialogPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PayMoney(houseKeeps.getAhousekeeping_id() + "", houseKeeps);
                            alertDialog.dismiss();
                        }
                    });

                } else {
                    T.showShort("已反馈，不可删除");
                }
            }
        });
    }

    private void PayMoney(String propertypay, HouseKeeps houseKeeps) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "1");
        params.put("ahousekeeping_id", propertypay);
        NetUtils.getApi().ahousekeeaping(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        if (result.contains("success")) {
                            T.show("删除成功(ˇ∀ˇ)", 1000);
                            list.remove(houseKeeps);
                            init();
                        } else {
                            T.show("删除失败(⊙﹏⊙)", 1000);
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

    public class MyAdapter extends RecyclerAdapter<HouseKeeps> {

        @Override
        protected int getItemViewType(int position, HouseKeeps peripheralService) {
            return R.layout.item_mypay;
        }

        @Override
        protected ViewHolder<HouseKeeps> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<HouseKeeps> {
            private TextView tvName;
            private TextView tvJob;
            private TextView tvHouseadress;
            private TextView tvTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvJob = (TextView) itemView.findViewById(R.id.tv_job);
                tvHouseadress = (TextView) itemView.findViewById(R.id.tv_houseadress);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            }

            @Override
            protected void onBind(HouseKeeps peripheralService, int position) {
                tvName.setText("预约人员：" + peripheralService.getAhousekeeping_staff_name());
                tvHouseadress.setText("地址：" + peripheralService.getAhousekeeping_addr());
                tvJob.setText("" + peripheralService.getAhousekeeping_content());
                tvTime.setText("预约时间:" + peripheralService.getAhousekeeping_time());
//                tvSuggestionId.setText(peripheralService.getPayment_staff_name());
//                if (peripheralService.getPayment_status().equals("0")) {
//                    tvSuggestionCounter.setText("未缴费");
//                    tvJfsj.setText("");
//                } else {
//                    tvSuggestionCounter.setText("已缴费");
//                    if (peripheralService.getPayment_time() != null)
//                        tvJfsj.setText("" + peripheralService.getPayment_time().toString());
//                }
//                tvMoney.setText("待缴费用：" + peripheralService.getPending_fee());
//                tvTime.setText("记录时间：" + peripheralService.getRecord_time());
//                tvType.setText("缴费类型：" + peripheralService.getPayment_name());

//                tv_surroundings_name.setText(peripheralService.getSurrounding_name());
//                tv_surroundings_content.setText("\t\t\t" + peripheralService.getSurrounding_content());
//                tv_surroundings_addr.setText("地址：" + peripheralService.getSurrounding_addr());
//                tv_surroundings_phone.setText("联系电话：" + peripheralService.getSurrounding_phone());
            }
        }
    }
}
