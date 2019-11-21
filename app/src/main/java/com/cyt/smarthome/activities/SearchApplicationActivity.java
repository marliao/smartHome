package com.cyt.smarthome.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class SearchApplicationActivity extends BaseActivity {

    private CustomTitleBar mCtbSearchApplication;
    private RecyclerView mRvSearchApplication;
    private MyAdapter myAdapter;
    private List<MalfunctionRepair> currentMalfuncionRepairlist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search_application;
    }

    @Override
    protected void initLayout() {
        mCtbSearchApplication = findViewById(R.id.ctb_search_application);
        mRvSearchApplication = findViewById(R.id.rv_search_application);
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
                                malfunctionRepair.setHousehold_name(jsonArray.getJSONObject(i).getString("household_name"));
                                malfunctionRepair.setRepair_content(jsonArray.getJSONObject(i).getString("repair_content"));
                                malfunctionRepair.setRepair_addr(jsonArray.getJSONObject(i).getString("repair_addr"));
                                malfunctionRepair.setRepair_time(jsonArray.getJSONObject(i).getString("repair_time"));
                                malfunctionRepair.setFeedback(false);
                                malfunctionRepairList.add(malfunctionRepair);
                            }
                            currentMalfuncionRepairlist = new ArrayList<>();
                            for (MalfunctionRepair malfunctionRepair : malfunctionRepairList) {
                                if (BmobUser.getCurrentUser(User.class).getuName().equals(malfunctionRepair.getHousehold_name())) {
                                    currentMalfuncionRepairlist.add(malfunctionRepair);
                                }
                            }
                            getMalfunctionRepairStatus();
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

    private void getMalfunctionRepairStatus() {
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
                                maintain.setRepair_id(jsonArray.getJSONObject(i).getString("repair_id"));
                                maintain.setMaintain_status(jsonArray.getJSONObject(i).getString("maintain_status"));
                                maintainList.add(maintain);
                            }
                            for (MalfunctionRepair malfunctionRepair : currentMalfuncionRepairlist) {
                                for (Maintain maintain : maintainList) {
                                    if (malfunctionRepair.getRepair_id().equals(maintain.getRepair_id())) {
                                        malfunctionRepair.setMaintain_status(maintain.getMaintain_status());
                                        malfunctionRepair.setFeedback(true);
                                    }
                                }
                            }
                            initAdapter();
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
        mRvSearchApplication.setHasFixedSize(true);
        mRvSearchApplication.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        mRvSearchApplication.setAdapter(myAdapter);
        myAdapter.add(currentMalfuncionRepairlist);
    }

    public class MyAdapter extends RecyclerAdapter<MalfunctionRepair> {

        @Override
        protected int getItemViewType(int position, MalfunctionRepair malfunctionRepair) {
            return R.layout.item_search_application;
        }

        @Override
        protected ViewHolder<MalfunctionRepair> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<MalfunctionRepair> {

            public TextView tv_repair_content;
            public TextView tv_repair_time;
            public TextView tv_repair_addr;
            public TextView tv_household_name;
            public TextView tv_repair_status;
            public ImageView iv_delete;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_repair_content = (TextView) itemView.findViewById(R.id.tv_repair_content);
                this.tv_repair_time = (TextView) itemView.findViewById(R.id.tv_repair_time);
                this.tv_repair_addr = (TextView) itemView.findViewById(R.id.tv_repair_addr);
                this.tv_household_name = (TextView) itemView.findViewById(R.id.tv_household_name);
                this.tv_repair_status = (TextView) itemView.findViewById(R.id.tv_repair_status);
                this.iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            }

            @Override
            protected void onBind(MalfunctionRepair malfunctionRepair, int position) {
                tv_repair_content.setText(malfunctionRepair.getRepair_content());
                tv_repair_time.setText(malfunctionRepair.getRepair_time());
                tv_repair_addr.setText(malfunctionRepair.getRepair_addr());
                tv_household_name.setText(malfunctionRepair.getHousehold_name());
                if (malfunctionRepair.isFeedback()) {
                    if (malfunctionRepair.getMaintain_status().equals(0 + "")) {
                        tv_repair_status.setText("维修失败");
                    } else {
                        tv_repair_status.setText("已维修");
                    }
                } else {
                    tv_repair_status.setText("等待中");
                }
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (malfunctionRepair.isFeedback()) {
                            T.showShort("已维修申请不可删除");
                        } else {
                            deleteMalfunctionRepair(malfunctionRepair, position);
                        }
                    }
                });
            }
        }
    }

    private void deleteMalfunctionRepair(MalfunctionRepair malfunctionRepair, int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "1");
        params.put("repair_id", malfunctionRepair.getRepair_id());
        NetUtils.getApi().deleteMalfunctionRepair(params)
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
                                T.showShort("删除成功");
                                currentMalfuncionRepairlist.remove(position);
                                if (currentMalfuncionRepairlist.size() == 0) {
                                    myAdapter.clear();
                                } else {
                                    myAdapter.replace(currentMalfuncionRepairlist);
                                }
                            } else {
                                T.showShort("删除失败，请稍后重试");
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
        mCtbSearchApplication.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
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
