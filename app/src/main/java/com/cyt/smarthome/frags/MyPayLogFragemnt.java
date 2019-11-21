package com.cyt.smarthome.frags;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.HouseKeepHistory;
import com.cyt.smarthome.view.RecyclerAdapter;

import java.util.List;

public class MyPayLogFragemnt extends BaseFragment {
    private RecyclerView rvHousekeeping;

    @Override
    protected int getLayout() {
        return R.layout.frag_pay;
    }

    @Override
    protected void initView(View view) {
        rvHousekeeping = (RecyclerView)view.findViewById(R.id.rv_property_payment);
    }

    @Override
    protected void init() {
        List<HouseKeepHistory> list = AppClient.list1;
        rvHousekeeping.setHasFixedSize(true);
        rvHousekeeping.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        rvHousekeeping.setAdapter(myAdapter);
        myAdapter.add(list);

    }
    public class MyAdapter extends RecyclerAdapter< HouseKeepHistory> {

        @Override
        protected int getItemViewType(int position,  HouseKeepHistory peripheralService) {
            return R.layout.item_housekeephistory;
        }

        @Override
        protected ViewHolder< HouseKeepHistory> onCreateViewHolder(View root, int viewType) {
            return new MyAdapter.MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder< HouseKeepHistory> {
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
            protected void onBind( HouseKeepHistory peripheralService, int position) {
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
