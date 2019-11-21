package com.cyt.smarthome.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.view.CustomTitleBar;

public class PropertyActivity extends BaseActivity {

    private CustomTitleBar mCtbPropertyAnnouncement;
    private TextView mTvvPropertyRegulations;
    private TextView mTvPropertyAnnouncement;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_property;
    }

    @Override
    protected void initLayout() {
        mCtbPropertyAnnouncement = findViewById(R.id.ctb_property_announcement);
        mTvvPropertyRegulations = findViewById(R.id.tvv_property_regulations);
        mTvPropertyAnnouncement = findViewById(R.id.tv_property_announcement);
    }

    @Override
    protected void init() {
        initListener();
    }

    private void initListener() {
        mCtbPropertyAnnouncement.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mTvPropertyAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PropertyAnnouncementActivity.class));
            }
        });
        mTvvPropertyRegulations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PropertyRegulationsActivity.class));
            }
        });
    }
}
