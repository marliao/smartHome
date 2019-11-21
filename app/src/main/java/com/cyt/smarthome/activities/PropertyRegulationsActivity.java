package com.cyt.smarthome.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.R;
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

public class PropertyRegulationsActivity extends BaseActivity {

    private CustomTitleBar mCtbTitle;
    private RecyclerView mRvPropertyRegulations;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_property_regulations;
    }

    @Override
    protected void initLayout() {
        mCtbTitle = findViewById(R.id.ctb_title);
        mRvPropertyRegulations = findViewById(R.id.rv_property_regulations);
    }

    @Override
    protected void init() {
        initlistener();
        getPropertyRegulations();
    }

    private void getPropertyRegulations() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllPropertyRegulations(params)
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
                            List<String> regulationlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                regulationlist.add(jsonArray.getJSONObject(i).getString("regulation_content"));
                            }
                            initAdapter(regulationlist);
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

    private void initAdapter(List<String> regulationlist) {
        mRvPropertyRegulations.setHasFixedSize(true);
        mRvPropertyRegulations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvPropertyRegulations.setAdapter(myAdapter);
        myAdapter.add(regulationlist);
    }

    public class MyAdapter extends RecyclerAdapter<String> {

        @Override
        protected int getItemViewType(int position, String s) {
            return R.layout.item_property_regulations;
        }

        @Override
        protected ViewHolder<String> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<String> {

            private TextView tv_property_regulations_content;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_property_regulations_content = itemView.findViewById(R.id.tv_property_regulations);
            }

            @Override
            protected void onBind(String s, int position) {
                tv_property_regulations_content.setText(s);
            }
        }

    }

    private void initlistener() {
        mCtbTitle.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
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
