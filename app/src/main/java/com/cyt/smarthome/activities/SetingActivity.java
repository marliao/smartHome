package com.cyt.smarthome.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.AppClient;
import com.cyt.smarthome.R;
import com.cyt.smarthome.utils.PackageCode;
import com.cyt.smarthome.utils.SpUtils;
import com.cyt.smarthome.utils.StaticClass;
import com.cyt.smarthome.view.CustomTitleBar;

public class SetingActivity extends BaseActivity {
    private TextView mTvVersionNumber;
    private TextView mTvFeedback;
    private TextView mTvCheckForUpdates;
    private TextView mTvSoftwareDescription;
    private TextView mTvLogout;
    private TextView mTvSignOut;
    private CustomTitleBar mTvSetingTitle;

    @Override
    protected int getLayout() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initLayout() {
        mTvSetingTitle = (CustomTitleBar) findViewById(R.id.tv_seting_title);
        mTvVersionNumber = (TextView) findViewById(R.id.tv_VersionNumber);
        mTvFeedback = (TextView) findViewById(R.id.tv_feedback);
        mTvCheckForUpdates = (TextView) findViewById(R.id.tv_CheckForUpdates);
        mTvSoftwareDescription = (TextView) findViewById(R.id.tv_SoftwareDescription);
        mTvLogout = (TextView) findViewById(R.id.tv_logout);
        mTvSignOut = (TextView) findViewById(R.id.tv_SignOut);
    }

    @Override
    protected void init() {
        PackageInfo getpackcode = PackageCode.getpackcode();
      //  System.out.println("versionCode:"+getpackcode.versionCode+" versionName:"+getpackcode.versionName);
        mTvVersionNumber.setText("版本号 "+getpackcode.versionName+"."+getpackcode.versionCode);
        mTvSetingTitle.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        mTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SpUtils.put(AppClient.mContext, StaticClass.USERNAME, "");
//                SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, "");
                SpUtils.remove(AppClient.mContext,StaticClass.USERNAME);
                SpUtils.remove(AppClient.mContext,StaticClass.PASSWORD);
                startActivity(new Intent(getApplication(),LoginActivity.class));
                finish();
            }
        });
        mTvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),LoginActivity.class));
                finish();
            }
        });
    }
}
