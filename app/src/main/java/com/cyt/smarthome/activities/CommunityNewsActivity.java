package com.cyt.smarthome.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.CommunityNews;
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

public class CommunityNewsActivity extends BaseActivity {

    private CustomTitleBar mCtbCommunityNews;
    private RecyclerView mRvCommunityNews;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_community_news;
    }

    @Override
    protected void initLayout() {
        mCtbCommunityNews = findViewById(R.id.ctb_community_news);
        mRvCommunityNews = findViewById(R.id.rv_community_news);
    }

    @Override
    protected void init() {
        initlistener();
        getAllCommunityNews();
    }

    private void initlistener() {
        mCtbCommunityNews.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void getAllCommunityNews() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllCommunityNews(params)
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
                            List<CommunityNews> communityNewsList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CommunityNews communityNews = new CommunityNews();
                                communityNews.setNews_title(jsonArray.getJSONObject(i).getString("news_title"));
                                communityNews.setNews_content(jsonArray.getJSONObject(i).getString("news_content"));
                                communityNewsList.add(communityNews);
                            }
                            Log.i(TAG, "onNext: -----------------"+result);
                            initAdapter(communityNewsList);
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

    private void initAdapter(List<CommunityNews> communityNewsList) {
        mRvCommunityNews.setHasFixedSize(true);
        mRvCommunityNews.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        MyAdapter myAdapter = new MyAdapter();
        mRvCommunityNews.setAdapter(myAdapter);
        myAdapter.add(communityNewsList);
    }

    public class MyAdapter extends RecyclerAdapter<CommunityNews> {

        @Override
        protected int getItemViewType(int position, CommunityNews communityNews) {
            return R.layout.item_community_news;
        }

        @Override
        protected ViewHolder<CommunityNews> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<CommunityNews> {
            public TextView tv_news_title;
            public TextView tv_news_content;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
                this.tv_news_content = (TextView) itemView.findViewById(R.id.tv_news_content);
            }

            @Override
            protected void onBind(CommunityNews communityNews, int position) {
                tv_news_title.setText(communityNews.getNews_title());
                tv_news_content.setText("\t\t\t" + communityNews.getNews_content());
            }
        }

    }

}
