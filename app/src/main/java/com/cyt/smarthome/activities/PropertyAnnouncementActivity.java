package com.cyt.smarthome.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.PropertyAnnouncement;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.view.CustomTitleBar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PropertyAnnouncementActivity extends BaseActivity {

    private CustomTitleBar mCtbPropertyAnnouncement;
    private RecyclerView mRvPropertyAnnouncement;

    @Override
    protected int getLayout() {
        return R.layout.activity_property_announcement;
    }

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected void initLayout() {
        mCtbPropertyAnnouncement = (CustomTitleBar) findViewById(R.id.ctb_property_announcement);
        mRvPropertyAnnouncement = (RecyclerView) findViewById(R.id.rv_property_announcement);
    }

    @Override
    protected void init() {
        initlistener();
        getAllPropertyAnnouncement();
    }

    private void initlistener() {
        mCtbPropertyAnnouncement.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void getAllPropertyAnnouncement() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllPropertyAnnouncement(params)
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
                            List<PropertyAnnouncement> propertyAnnouncementList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PropertyAnnouncement propertyAnnouncement = new PropertyAnnouncement();
                                propertyAnnouncement.setAnnouncement_id(jsonArray.getJSONObject(i).getString("announcement_id"));
                                propertyAnnouncement.setAnnouncement_content(jsonArray.getJSONObject(i).getString("announcement_content"));
                                propertyAnnouncement.setRelease_date(jsonArray.getJSONObject(i).getString("release_date"));
                                propertyAnnouncement.setRelease_name(jsonArray.getJSONObject(i).getString("release_name"));
                                propertyAnnouncementList.add(propertyAnnouncement);
                            }
                            initAdapter(propertyAnnouncementList);
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

    private void initAdapter(List<PropertyAnnouncement> propertyAnnouncementList) {
        mRvPropertyAnnouncement.setHasFixedSize(true);
        mRvPropertyAnnouncement.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter(propertyAnnouncementList);
        mRvPropertyAnnouncement.setAdapter(myAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<PropertyAnnouncement> propertyAnnouncementList;

        public MyAdapter(List<PropertyAnnouncement> propertyAnnouncementList) {
            this.propertyAnnouncementList = propertyAnnouncementList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_property_announcement, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            PropertyAnnouncement propertyAnnouncement = propertyAnnouncementList.get(i);
            viewHolder.tv_release_time.setText("发布时间：" + propertyAnnouncement.getRelease_date());
            viewHolder.tv_release_staff.setText("发布人员：" + propertyAnnouncement.getRelease_name());
            viewHolder.tv_release_content.setText("\t\t\t" + propertyAnnouncement.getAnnouncement_content());
        }

        @Override
        public int getItemCount() {
            return propertyAnnouncementList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View rootView;
            public TextView tv_release_time;
            public TextView tv_release_staff;
            public TextView tv_release_content;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.rootView = itemView;
                this.tv_release_time = (TextView) rootView.findViewById(R.id.tv_release_time);
                this.tv_release_staff = (TextView) rootView.findViewById(R.id.tv_release_staff);
                this.tv_release_content = (TextView) rootView.findViewById(R.id.tv_release_content);
            }
        }
    }

}
