package com.cyt.smarthome.utils;

import com.cyt.smarthome.api.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetUtils {

    private static Retrofit retrofit;
    private static Api api;
    private static final String baseUrl = "http://39.106.171.79:7002/contorler/";

    private static Retrofit getRetrofit() {
        //使用Gson解析器
        //.addConverterFactory(ScalarsConverterFactory.create())//字符串
        //使用Gson解析器
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())//字符串
                    //.addConverterFactory(GsonConverterFactory.create())//使用Gson解析器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return retrofit;
    }

    public static Api getApi() {
        if (api == null) {
            api = getRetrofit().create(Api.class);
        }
        return api;
    }


}
