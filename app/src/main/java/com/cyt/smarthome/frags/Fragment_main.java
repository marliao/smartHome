package com.cyt.smarthome.frags;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cyt.smarthome.R;
import com.cyt.smarthome.activities.CommunityNewsActivity;
import com.cyt.smarthome.activities.CommunityTableActivity;
import com.cyt.smarthome.activities.HousekeepingActivity;
import com.cyt.smarthome.activities.MalfunctionRepairActivity;
import com.cyt.smarthome.activities.PeripheralServiceActivity;
import com.cyt.smarthome.activities.PropertyActivity;
import com.cyt.smarthome.activities.PersonPaymentActivity;
import com.cyt.smarthome.activities.SuggestionsActivity;

public class Fragment_main extends BaseFragment implements View.OnClickListener {
    private TextView mTvMalfunctionRepair;
    private TextView mTvHousekeeping;
    private TextView mTvSuggestions;
    private TextView mTvPropertyPayment;
    private TextView mTvPropertyAnnouncement;
    private TextView mTvCommunityNews;
    private TextView mTvPeripheralService;
    private TextView mTvCommunityTable;

    @Override
    protected int getLayout() {
        return R.layout.frag_main;
    }

    @Override
    protected void initView(View view) {
        mTvMalfunctionRepair = (TextView) view.findViewById(R.id.tv_MalfunctionRepair);
        mTvHousekeeping = (TextView) view.findViewById(R.id.tv_Housekeeping);
        mTvSuggestions = (TextView) view.findViewById(R.id.tv_Suggestions);
        mTvPropertyPayment = (TextView) view.findViewById(R.id.tv_PropertyPayment);
        mTvPropertyAnnouncement = (TextView) view.findViewById(R.id.tv_PropertyAnnouncement);
        mTvCommunityNews = (TextView) view.findViewById(R.id.tv_CommunityNews);
        mTvPeripheralService = (TextView) view.findViewById(R.id.tv_PeripheralService);
        mTvCommunityTable = (TextView) view.findViewById(R.id.tv_CommunityTable);
        mTvMalfunctionRepair.setOnClickListener(this);
        mTvHousekeeping.setOnClickListener(this);
        mTvSuggestions.setOnClickListener(this);
        mTvPropertyPayment.setOnClickListener(this);
        mTvPropertyAnnouncement.setOnClickListener(this);
        mTvCommunityNews.setOnClickListener(this);
        mTvPeripheralService.setOnClickListener(this);
        mTvCommunityTable.setOnClickListener(this);
    }

    @Override
    protected void init() {
        //   User user = new User();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_MalfunctionRepair://故障报修
                startActivity(new Intent(getContext(), MalfunctionRepairActivity.class));
                break;
            case R.id.tv_Housekeeping://  家政服务
                startActivity(new Intent(getContext(), HousekeepingActivity.class));
                break;
            case R.id.tv_Suggestions://投诉建议
                startActivity(new Intent(getContext(), SuggestionsActivity.class));
                break;
            case R.id.tv_PropertyPayment://  物业缴费
                startActivity(new Intent(getContext(), PersonPaymentActivity.class));
                break;
            case R.id.tv_PropertyAnnouncement://物业公告
                startActivity(new Intent(getContext(), PropertyActivity.class));
                break;
            case R.id.tv_CommunityNews://社区新闻
                startActivity(new Intent(getContext(), CommunityNewsActivity.class));
                break;
            case R.id.tv_PeripheralService://查看周边
                startActivity(new Intent(getContext(), PeripheralServiceActivity.class));
                break;
            case R.id.tv_CommunityTable:// 社区点餐
                startActivity(new Intent(getContext(), CommunityTableActivity.class));
                break;

        }
    }
}
