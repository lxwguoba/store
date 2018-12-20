package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-18.
 */

public class StoreInfoBean implements Serializable {


    /**
     * id : 17
     * name : 美家百货
     * industry_id : 6
     * province_id : 35
     * province_name : 深圳经济特区
     * city_id : 449
     * city_name : 龙岗区
     * area_id : 3195
     * area_name : 宝龙街道
     * thirdpay_rate : 15
     * is_recommend : 0
     * is_culling : 0
     * status : 1
     * browse : 0
     * is_apply : 0
     * apply_time : null
     * created_at : 1544752877
     * updated_at : 1544863031
     * deleted_at : null
     * remark
     */


    private int id;
    private String name;
    private String industry_id;
    private int province_id;
    private String province_name;
    private int city_id;
    private String city_name;
    private int area_id;
    private String area_name;
    private int thirdpay_rate;
    private int is_recommend;
    private int is_culling;
    private int status;
    private int browse;
    private int is_apply;
    private String apply_time;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry_id() {
        return industry_id;
    }

    public void setIndustry_id(String industry_id) {
        this.industry_id = industry_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public int getThirdpay_rate() {
        return thirdpay_rate;
    }

    public void setThirdpay_rate(int thirdpay_rate) {
        this.thirdpay_rate = thirdpay_rate;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getIs_culling() {
        return is_culling;
    }

    public void setIs_culling(int is_culling) {
        this.is_culling = is_culling;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBrowse() {
        return browse;
    }

    public void setBrowse(int browse) {
        this.browse = browse;
    }

    public int getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(int is_apply) {
        this.is_apply = is_apply;
    }

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
