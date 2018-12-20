package com.zerone.intodata.config;

/**
 * Created by 刘兴文 on 2018-12-17.
 */

public interface IPConfig {
    //获取行业分类
    public final String GET_STORE_INDUSTRY = "http://zerone.01nnt.com/api/apply/get_store_industry";
    //获取市
    public final String CITY = "http://zerone.01nnt.com/api/apply/city";
    //获取省份
    public static String PROVINCE = "http://zerone.01nnt.com/api/apply/province";
    //获取区
    public static String AREA = "http://zerone.01nnt.com/api/apply/area";

    public static String  LOGIN="http://zerone.01nnt.com/api/apply/login";

    //保存店铺信息
    public static  String STORE_APPLY_CHECK="http://zerone.01nnt.com/api/apply/store_apply_check";
    //保存银行卡信息
    public static String  APPLY_DATA_CHECK= "http://zerone.01nnt.com/api/apply/apply_data_check";
    //保存图片信息
    public static String APPLY_PHOTO_CHECK="http://zerone.01nnt.com/api/apply/apply_photo_check";
    //获取店铺列表
    public  static String GET_STORE_LIST="http://zerone.01nnt.com/api/apply/get_store_list";
    //提交审核
    public  static  String UPDATE_STATUS="http://zerone.01nnt.com/api/apply/update_status";
    //获取店铺详情
    public static String  GET_STORE_DETAIL="http://zerone.01nnt.com/api/apply/get_store_detail";
    //获取收款资料
    public static String  GET_STORE_DATACULATION="http://zerone.01nnt.com/api/apply/get_store_dataculation";
    //获取收款资料的图片
    public static String  GET_STORE_DATAPHOTO="http://zerone.01nnt.com/api/apply/get_store_dataphoto";
    //修改店铺资料
    public static String STORE_APPLY_UPDATE="http://zerone.01nnt.com/api/apply/store_apply_update";
}
