package com.cyt.smarthome;

import android.app.Application;
import android.content.Context;

import com.cyt.smarthome.bean.HouseKeepHistory;
import com.cyt.smarthome.bean.HouseKeeps;
import com.cyt.smarthome.bean.Propertypay;

import java.util.List;

import cn.bmob.v3.Bmob;

public class AppClient extends Application {

    public static Context mContext;
    public static List<HouseKeepHistory> list1;
    public static List<HouseKeeps> list;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Bmob.initialize(this, "22012c9b2a9c9521ea61881e21c140d6");
    }

}
