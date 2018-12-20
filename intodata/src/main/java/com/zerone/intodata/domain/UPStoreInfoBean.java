package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-19.
 */

public class UPStoreInfoBean implements Serializable {

    /**
     * id : 32
     * name : 锅巴4号
     * industry_id : 1
     * thirdpay_rate : 3
     * province_id : 1
     * city_id : 349
     * area_id : 0
     * status : 3
     * created_at : 1545182828
     * realname : null
     * mobile : null
     * industry_name : 美食
     */
    private int id;
    private String name;
    private String industry_id;
    private int thirdpay_rate;
    private int province_id;
    private int city_id;
    private int area_id;
    private int status;
    private String created_at;
    private String realname;
    private String mobile;
    private String industry_name;

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

    public int getThirdpay_rate() {
        return thirdpay_rate;
    }

    public void setThirdpay_rate(int thirdpay_rate) {
        this.thirdpay_rate = thirdpay_rate;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }
}
