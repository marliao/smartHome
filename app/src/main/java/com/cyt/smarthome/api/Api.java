package com.cyt.smarthome.api;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    /**
     * 添加业主
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_owner.php")
    Observable<String> addOwener(@FieldMap Map<String, String> params);

    /**
     * 添加住户
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_household.php")
    Observable<String> addHouser(@FieldMap Map<String, String> params);

    /**
     * 查询物业公告
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_announcement.php")
    Observable<String> selectAllPropertyAnnouncement(@FieldMap Map<String, String> params);

    /**
     * 查询物业条例
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_regulation.php")
    Observable<String> selectAllPropertyRegulations(@FieldMap Map<String, String> params);

    /**
     * 查询社区新闻
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_news.php")
    Observable<String> selectAllCommunityNews(@FieldMap Map<String, String> params);

    /**
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_surroundings.php")
    Observable<String> selectAllPeripheralService(@FieldMap Map<String, String> params);

    /**
     * 查询投诉建议
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_complaint.php")
    Observable<String> selectAllSuggestions(@FieldMap Map<String, String> params);

    /**
     * 查询物业缴费订单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_payment.php")
    Observable<String> selectAllPropertyPayment(@FieldMap Map<String, String> params);

    /**
     * 查询所有菜单
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_week_foodmenu.php")
    Observable<String> selectAllMenu(@FieldMap Map<String, String> params);

    /**
     * 查看所有反馈
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_maintain.php")
    Observable<String> selectAllMaintain(@FieldMap Map<String, String> params);

    /**
     * 添加一个条目到缴费表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_payment.php")
    Observable<String> insertPayment(@FieldMap Map<String, String> params);
    /**
     * 家政服务申请表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_ahousekeeping.php")
    Observable<String> ahousekeeaping (@FieldMap Map<String, String> params);
    /**
     * 家政服务记录表
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_housekeeping.php")
    Observable<String> housekeeaping (@FieldMap Map<String, String> params);

    /**
     * 添加一条故障报修
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_repair.php")
    Observable<String> insertMalfunctionRepair(@FieldMap Map<String, String> params);

    /**
     * 查看故障报修申请
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_repair.php")
    Observable<String> searchMalfunctionRepair(@FieldMap Map<String, String> params);

    /**
     * 删除故障报修申请
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_repair.php")
    Observable<String> deleteMalfunctionRepair(@FieldMap Map<String, String> params);

    /**
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("conn_maintain.php")
    Observable<String> searchFeedBack(@FieldMap Map<String, String> params);

}
