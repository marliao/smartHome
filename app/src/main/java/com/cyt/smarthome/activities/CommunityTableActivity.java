package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.Food;
import com.cyt.smarthome.bean.SubmitFoodOrder;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.cyt.smarthome.view.RecyclerAdapter;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommunityTableActivity extends BaseActivity implements View.OnClickListener {


    private CustomTitleBar mCtbCommunityNews;
    private TextView mTvStapleFood;
    private TextView mTvVegetableDish;
    private TextView mTvChives;
    private TextView mTvSoup;
    private RecyclerView mRvCommunityTable;
    private TextView mTvFoodMoneyAll;
    private Button mBtnSubmit;
    private MyAdapter myAdapter;
    private List<Food> foodList;
    private int sum;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_community_table;
    }

    @Override
    protected void initLayout() {
        mCtbCommunityNews = findViewById(R.id.ctb_community_news);
        mTvStapleFood = findViewById(R.id.tv_staple_food);
        mTvVegetableDish = findViewById(R.id.tv_vegetable_dish);
        mTvChives = findViewById(R.id.tv_chives);
        mTvSoup = findViewById(R.id.tv_soup);
        mRvCommunityTable = findViewById(R.id.rv_community_table);
        mTvFoodMoneyAll = findViewById(R.id.tv_food_money_all);
        mBtnSubmit = findViewById(R.id.btn_submit);
    }

    @Override
    protected void init() {
        initlistener();
        getMenu();
    }

    private void getMenu() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "3");
        params.put("food_provide_date", "2019-4-1");
        NetUtils.getApi().selectAllMenu(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        Log.i(TAG, "onNext: ---------------------" + result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            foodList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Food food = new Food();
                                food.setWeek_foood_name(jsonArray.getJSONObject(i).getString("week_food_name"));
                                food.setWeek_food_type(jsonArray.getJSONObject(i).getString("week_food_type"));
                                food.setFood_provide_time(jsonArray.getJSONObject(i).getString("food_provide_time"));
                                switch (food.getWeek_food_type()) {
                                    case 0 + "":
                                        food.setFood_money(1);
                                        break;
                                    case 1 + "":
                                        food.setFood_money(15);
                                        break;
                                    case 2 + "":
                                        food.setFood_money(30);
                                        break;
                                    case 3 + "":
                                        food.setFood_money(20);
                                        break;
                                }
                                food.setFood_account(0);
                                foodList.add(food);
                            }
                            initAdapter(0);
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

    private void initAdapter(int food_type) {
        List<Food> currentlist = new ArrayList<>();
        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            if (food.getWeek_food_type().equals(food_type + "")) {
                currentlist.add(food);
            }
        }
        if (myAdapter == null) {
            mRvCommunityTable.setHasFixedSize(true);
            mRvCommunityTable.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            myAdapter = new MyAdapter();
            mRvCommunityTable.setAdapter(myAdapter);
        }
        myAdapter.clear();
        myAdapter.add(currentlist);
        initBottomUI();
    }

    public class MyAdapter extends RecyclerAdapter<Food> {

        @Override
        protected int getItemViewType(int position, Food food) {
            return R.layout.item_community_food;
        }

        @Override
        protected ViewHolder<Food> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Food> {

            public TextView tv_food_name;
            public TextView tv_food_provide_time;
            public TextView tv_food_money;
            public TextView tv_food_amount;
            public ImageView iv_reduce;
            public ImageView iv_add;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_food_name = (TextView) itemView.findViewById(R.id.tv_food_name);
                this.tv_food_provide_time = (TextView) itemView.findViewById(R.id.tv_food_provide_time);
                this.tv_food_money = (TextView) itemView.findViewById(R.id.tv_food_money);
                this.tv_food_amount = (TextView) itemView.findViewById(R.id.tv_food_amount);
                this.iv_reduce = (ImageView) itemView.findViewById(R.id.iv_reduce);
                this.iv_add = (ImageView) itemView.findViewById(R.id.iv_add);
            }

            @Override
            protected void onBind(Food food, int position) {
                tv_food_name.setText(food.getWeek_foood_name());
                tv_food_provide_time.setText(food.getFood_provide_time());
                tv_food_amount.setText(food.getFood_account() + "");
                tv_food_money.setText(food.getFood_money() + "");
                iv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Food fod : foodList) {
                            if (fod.getWeek_foood_name().equals(food.getWeek_foood_name())) {
                                if (fod.getFood_account() < 99) {
                                    fod.setFood_account(fod.getFood_account() + 1);
                                } else {
                                    T.showShort("当前已达购买数量最大值");
                                }
                            }
                        }
                        initAdapter(Integer.parseInt(food.getWeek_food_type()));
                        initBottomUI();
                    }
                });
                iv_reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Food fod : foodList) {
                            if (fod.getWeek_foood_name().equals(food.getWeek_foood_name())) {
                                if (fod.getFood_account() > 0) {
                                    fod.setFood_account(fod.getFood_account() - 1);
                                } else {
                                    T.showShort("购买数量以最低！");
                                }
                            }
                        }
                        initAdapter(Integer.parseInt(food.getWeek_food_type()));
                        initBottomUI();
                    }
                });
            }
        }
    }

    private void initBottomUI() {
        sum = 0;
        for (Food food : foodList) {
            sum += food.getFood_account() * food.getFood_money();
        }
        String format = new DecimalFormat("0.00").format(sum);
        mTvFoodMoneyAll.setText(format + "");
    }

    private void initlistener() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sum == 0) {
                    T.showShort("您还没有选择菜品！");
                }else {
                    List<SubmitFoodOrder> submitFoodOrderList = new ArrayList<>();
                    for (Food food : foodList) {
                        if (food.getFood_account() > 0) {
                            SubmitFoodOrder submitFoodOrder = new SubmitFoodOrder();
                            submitFoodOrder.setFood_account(food.getFood_account());
                            submitFoodOrder.setFood_money(food.getFood_money());
                            submitFoodOrder.setFood_name(food.getWeek_foood_name());
                            submitFoodOrder.setCurrentUser(BmobUser.getCurrentUser(User.class).getuName());
                            submitFoodOrderList.add(submitFoodOrder);
                        }
                    }
                    showToast(submitFoodOrderList);
                }
            }
        });
        mCtbCommunityNews.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        mTvStapleFood.setOnClickListener(this);
        mTvVegetableDish.setOnClickListener(this);
        mTvChives.setOnClickListener(this);
        mTvSoup.setOnClickListener(this);
    }

    private void showToast(List<SubmitFoodOrder> submitFoodOrderList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否提交订单？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (SubmitFoodOrder submitFoodOrder : submitFoodOrderList) {
                    submitFoodOrder.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            //
                        }
                    });
                }
                dialog.dismiss();
                insertPayment();
                getMenu();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void insertPayment() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "0");
        params.put("payment_name", "社区小饭桌");
        params.put("payment_staff_name", BmobUser.getCurrentUser(User.class).getuName());
        params.put("pending_fee", sum + "");
        params.put("payment_status", "0");
        String format = new SimpleDateFormat("yyyy-M-dd").format(new Date(System.currentTimeMillis()));
        params.put("record_time", format);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_staple_food:
                mTvStapleFood.setBackgroundColor(Color.parseColor("#D9DEE4"));
                mTvVegetableDish.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvChives.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvSoup.setBackgroundColor(Color.parseColor("#ffffff"));
                initAdapter(0);
                break;
            case R.id.tv_vegetable_dish:
                mTvStapleFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvVegetableDish.setBackgroundColor(Color.parseColor("#D9DEE4"));
                mTvChives.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvSoup.setBackgroundColor(Color.parseColor("#ffffff"));
                initAdapter(1);
                break;
            case R.id.tv_chives:
                mTvStapleFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvVegetableDish.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvChives.setBackgroundColor(Color.parseColor("#D9DEE4"));
                mTvSoup.setBackgroundColor(Color.parseColor("#ffffff"));
                initAdapter(2);
                break;
            case R.id.tv_soup:
                mTvStapleFood.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvVegetableDish.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvChives.setBackgroundColor(Color.parseColor("#ffffff"));
                mTvSoup.setBackgroundColor(Color.parseColor("#D9DEE4"));
                initAdapter(3);
                break;
        }
    }


}
