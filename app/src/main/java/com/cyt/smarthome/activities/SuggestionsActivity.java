package com.cyt.smarthome.activities;

import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.bean.PeripheralService;
import com.cyt.smarthome.bean.Suggestion;
import com.cyt.smarthome.bean.User;
import com.cyt.smarthome.utils.L;
import com.cyt.smarthome.utils.NetUtils;
import com.cyt.smarthome.utils.T;
import com.cyt.smarthome.view.CustomTitleBar;
import com.cyt.smarthome.view.RecyclerAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SuggestionsActivity extends BaseActivity {
    private CustomTitleBar tvSuggestionTitle;
    private RecyclerView rvSuggestions;

    @Override
    protected int getLayout() {
        return R.layout.activity_suggestions;
    }

    @Override
    protected void initLayout() {
        tvSuggestionTitle = (CustomTitleBar) findViewById(R.id.tv_suggestion_title);
        rvSuggestions = (RecyclerView) findViewById(R.id.rv_suggestions);
    }

    @Override
    protected void init() {
        initlistenger();
        initData();
    }

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "4");
        NetUtils.getApi().selectAllSuggestions(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println("----"+result);
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            List<Suggestion> suggestionArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Suggestion suggestion = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Suggestion.class);
                              if(suggestion.getComplaint_staff_name().equals(BmobUser.getCurrentUser(User.class).getuName())){
                                  suggestionArrayList.add(suggestion);
                              }
                            }
                          initAdapter(suggestionArrayList);
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
    private void initAdapter(List<Suggestion> peripheralServiceList) {
        rvSuggestions.setHasFixedSize(true);
        rvSuggestions.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        MyAdapter myAdapter = new MyAdapter();
        rvSuggestions.setAdapter(myAdapter);
        myAdapter.add(peripheralServiceList);
    }

    public class MyAdapter extends RecyclerAdapter<Suggestion> {

        @Override
        protected int getItemViewType(int position, Suggestion peripheralService) {
            return R.layout.item_suggestion;
        }

        @Override
        protected ViewHolder<Suggestion> onCreateViewHolder(View root, int viewType) {
            return new  MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Suggestion> {

            private TextView tvSuggestionId;
            private TextView tvSuggestionCounter;
            private TextView tvSuggestionUser;
            private TextView tvSuggestionFeedback;
            public MyViewHolder(View itemView) {
                super(itemView);
                this.tvSuggestionId = (TextView) itemView.findViewById(R.id.tv_suggestion_id);
                this.tvSuggestionCounter = (TextView) itemView.findViewById(R.id.tv_suggestion_counter);
                this.tvSuggestionUser = (TextView) itemView.findViewById(R.id.tv_suggestion_user);
                this.tvSuggestionFeedback = (TextView) itemView.findViewById(R.id.tv_suggestion_feedback);

            }

            @Override
            protected void onBind(Suggestion peripheralService, int position) {
                tvSuggestionId.setText("投诉编号："+peripheralService.getComplaint_id());
                tvSuggestionCounter.setText("投诉建议："+peripheralService.getComplaint_content());
                tvSuggestionUser.setText("投诉住户："+peripheralService.getComplaint_staff_name());
                String property_feedback = peripheralService.getProperty_feedback();
                if(!TextUtils.isEmpty(property_feedback)){
                    tvSuggestionFeedback.setText(property_feedback);
                }
//                tv_surroundings_name.setText(peripheralService.getSurrounding_name());
//                tv_surroundings_content.setText("\t\t\t" + peripheralService.getSurrounding_content());
//                tv_surroundings_addr.setText("地址：" + peripheralService.getSurrounding_addr());
//                tv_surroundings_phone.setText("联系电话：" + peripheralService.getSurrounding_phone());
            }
        }
    }
    private void initlistenger() {
        tvSuggestionTitle.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                ShowAddSuggestionAlter();
            }
        });
    }

    private void ShowAddSuggestionAlter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionsActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(getApplicationContext(), R.layout.add_suggsion_dialog, null);
        alertDialog.setView(inflate);
        alertDialog.show();
          EditText etSuggestion;
          Button btnDialogCancel;
          Button btnDialogAdd;
        etSuggestion = (EditText) inflate.findViewById(R.id.et_suggestion);
        btnDialogCancel = (Button) inflate.findViewById(R.id.btn_dialog_cancel);
        btnDialogAdd = (Button) inflate.findViewById(R.id.btn_dialog_add);
        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etSuggestion.getText().toString();
                if(TextUtils.isEmpty(s)){
                    T.show("请先填写建议内容(´ｰ∀ｰ`)",1000);
                }else{
                     AddSuggestionAlter(s);
                    alertDialog.dismiss();
                }
            }
        });
       btnDialogCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               alertDialog.dismiss();
           }
       });

    }

    private void AddSuggestionAlter(String s) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method_id", "0");
        params.put("complaint_staff_name", BmobUser.getCurrentUser(User.class).getuName());
        params.put("complaint_content", s);
//        params.put("property_feedback", null);
        NetUtils.getApi().selectAllSuggestions(params)
                .subscribeOn(Schedulers.io())//网络访问运行的耗时线程中
                .observeOn(AndroidSchedulers.mainThread())//订阅者运行在主线程中
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
                        System.out.println("----"+result);
                         if(result.contains("success")){
                             initData();
                             T.show("添加成功(ˇ∀ˇ)",1000);
                         }else {
                             T.show("添加失败(⊙﹏⊙)",1000);
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
}
