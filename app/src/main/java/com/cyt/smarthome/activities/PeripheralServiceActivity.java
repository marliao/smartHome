package com.cyt.smarthome.activities;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.PeripheralService;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.view.CustomTitleBar;
import com.cyt.smarthome.view.RecyclerAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PeripheralServiceActivity extends BaseActivity implements View.OnClickListener {

    private CustomTitleBar mCtbPeripheralService;
    private LinearLayout mLlFood;
    private LinearLayout mLlShopping;
    private LinearLayout mLlViewpoint;
    private LinearLayout mLlPlay;
    private LinearLayout mLlHotel;
    private LinearLayout mLlOther;
    private RecyclerView mRvPeripheralService;
    private List<PeripheralService> peripheralServiceList;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_peripheral_service;
    }

    @Override
    protected void initLayout() {
        mCtbPeripheralService = findViewById(R.id.ctb_peripheral_service);
        mLlFood = findViewById(R.id.ll_food);
        mLlShopping = findViewById(R.id.ll_shopping);
        mLlViewpoint = findViewById(R.id.ll_viewpoint);
        mLlPlay = findViewById(R.id.ll_play);
        mLlHotel = findViewById(R.id.ll_hotel);
        mLlOther = findViewById(R.id.ll_other);
        mRvPeripheralService = findViewById(R.id.rv_peripheral_service);
    }

    @Override
    protected void init() {
        mLlFood.setBackgroundColor(Color.parseColor("#552091FF"));
        mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
        initlistener();
        getAllPeripheralService();
    }

    private void getAllPeripheralService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllPeripheralService(params)
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
                            peripheralServiceList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PeripheralService peripheralService = new PeripheralService();
                                peripheralService.setSurrounding_name(jsonArray.getJSONObject(i).getString("surrounding_name"));
                                peripheralService.setSurrounding_content(jsonArray.getJSONObject(i).getString("surrounding_content"));
                                peripheralService.setSurrounding_addr(jsonArray.getJSONObject(i).getString("surrounding_addr"));
                                peripheralService.setSurrounding_phone(jsonArray.getJSONObject(i).getString("surrounding_phone"));
                                peripheralService.setSurrounding_type(jsonArray.getJSONObject(i).getString("surrounding_type"));
                                peripheralServiceList.add(peripheralService);
                            }
                            prepareData(1);
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

    private void prepareData(int type) {
        List<PeripheralService> peripheralServices = new ArrayList<>();
        for (PeripheralService peripheralService : peripheralServiceList) {
            if (peripheralService.getSurrounding_type().equals(type + "")) {
                peripheralServices.add(peripheralService);
            }
        }
        initAdapter(peripheralServices);
    }

    private void initAdapter(List<PeripheralService> peripheralServiceList) {
        mRvPeripheralService.setHasFixedSize(true);
        mRvPeripheralService.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvPeripheralService.setAdapter(myAdapter);
        myAdapter.add(peripheralServiceList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_food:
                mLlFood.setBackgroundColor(Color.parseColor("#552091FF"));
                mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
                prepareData(1);
                break;
            case R.id.ll_shopping:
                mLlFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlShopping.setBackgroundColor(Color.parseColor("#552091FF"));
                mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
                prepareData(2);
                break;
            case R.id.ll_viewpoint:
                mLlFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#552091FF"));
                mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
                prepareData(3);
                break;
            case R.id.ll_play:
                mLlFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlPlay.setBackgroundColor(Color.parseColor("#552091FF"));
                mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
                prepareData(4);
                break;
            case R.id.ll_hotel:
                mLlFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlHotel.setBackgroundColor(Color.parseColor("#552091FF"));
                mLlOther.setBackgroundColor(Color.parseColor("#ffffff"));
                prepareData(5);
                break;
            case R.id.ll_other:
                mLlFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlShopping.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlPlay.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlViewpoint.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlHotel.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlOther.setBackgroundColor(Color.parseColor("#552091FF"));
                prepareData(0);
                break;
        }
    }

    public class MyAdapter extends RecyclerAdapter<PeripheralService> {

        @Override
        protected int getItemViewType(int position, PeripheralService peripheralService) {
            return R.layout.item_peripheral_service;
        }

        @Override
        protected ViewHolder<PeripheralService> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<PeripheralService> {

            public TextView tv_surroundings_name;
            public TextView tv_surroundings_content;
            public TextView tv_surroundings_addr;
            public TextView tv_surroundings_phone;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_surroundings_name = (TextView) itemView.findViewById(R.id.tv_surroundings_name);
                this.tv_surroundings_content = (TextView) itemView.findViewById(R.id.tv_surroundings_content);
                this.tv_surroundings_addr = (TextView) itemView.findViewById(R.id.tv_surroundings_addr);
                this.tv_surroundings_phone = (TextView) itemView.findViewById(R.id.tv_surroundings_phone);
            }

            @Override
            protected void onBind(PeripheralService peripheralService, int position) {
                tv_surroundings_name.setText(peripheralService.getSurrounding_name());
                tv_surroundings_content.setText("\t\t\t" + peripheralService.getSurrounding_content());
                tv_surroundings_addr.setText("地址：" + peripheralService.getSurrounding_addr());
                tv_surroundings_phone.setText("联系电话：" + peripheralService.getSurrounding_phone());
            }
        }
    }

    private void initlistener() {
        mCtbPeripheralService.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mLlFood.setOnClickListener(this);
        mLlShopping.setOnClickListener(this);
        mLlViewpoint.setOnClickListener(this);
        mLlPlay.setOnClickListener(this);
        mLlHotel.setOnClickListener(this);
        mLlOther.setOnClickListener(this);
    }
}
