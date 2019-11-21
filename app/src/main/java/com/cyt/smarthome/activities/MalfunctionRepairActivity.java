package com.cyt.smarthome.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.view.CustomTitleBar;

public class MalfunctionRepairActivity extends BaseActivity {

    private CustomTitleBar mCtbMalfunctionRepair;
    private TextView mTvSubmitApplication;
    private View mTvSearchApplication;
    private TextView mTvFeedbackApplication;
    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_malfunction_repair;
    }

    @Override
    protected void initLayout() {
        mCtbMalfunctionRepair = findViewById(R.id.ctb_malfunction_repair);
        mTvSubmitApplication = findViewById(R.id.tv_submit_application);
        mTvSearchApplication = findViewById(R.id.tv_search_application);
        mTvFeedbackApplication = findViewById(R.id.tv_feedback_application);
    }

    @Override
    protected void init() {
        initListener();
    }

    private void initListener() {
        mCtbMalfunctionRepair.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        mTvSubmitApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubmitApplicationActivity.class));
            }
        });
        mTvSearchApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchApplicationActivity.class));
            }
        });
        mTvFeedbackApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FeedBackApplicationActivity.class));
            }
        });
    }
}
