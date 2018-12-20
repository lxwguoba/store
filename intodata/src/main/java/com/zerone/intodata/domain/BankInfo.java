package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-19.
 */

public class BankInfo implements Serializable {


    /**
     * id : 11
     * store_id : 29
     * sub_type : 2
     * sub_name : 锅巴
     * sub_idcard : 6227555563412547893
     * sub_bankname : 建设银行
     * sub_bankider : 3508291177496328521
     * bank_mobile : null
     * bank_province_id : 35
     * bank_city_id : 449
     * bank_area_id : 3193
     * store_address : null
     * contact_name : 锅巴哈哈
     * contact_mobile : 12345678901
     * contact_email : 111111@163.com
     * license_number : null
     * license_date : null
     * remark : null
     * created_at : 1545182028
     * updated_at : 1545182028
     * deleted_at : null
     */

    private int id;
    private int store_id;
    private int sub_type;
    private String sub_name;
    private String sub_idcard;
    private String sub_bankname;
    private String sub_bankider;
    private String bank_mobile;
    private int bank_province_id;
    private int bank_city_id;
    private int bank_area_id;
    private String store_address;
    private String contact_name;
    private String contact_mobile;
    private String contact_email;
    private String license_number;
    private String license_date;
    private String remark;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getSub_type() {
        return sub_type;
    }

    public void setSub_type(int sub_type) {
        this.sub_type = sub_type;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_idcard() {
        return sub_idcard;
    }

    public void setSub_idcard(String sub_idcard) {
        this.sub_idcard = sub_idcard;
    }

    public String getSub_bankname() {
        return sub_bankname;
    }

    public void setSub_bankname(String sub_bankname) {
        this.sub_bankname = sub_bankname;
    }

    public String getSub_bankider() {
        return sub_bankider;
    }

    public void setSub_bankider(String sub_bankider) {
        this.sub_bankider = sub_bankider;
    }

    public String getBank_mobile() {
        return bank_mobile;
    }

    public void setBank_mobile(String bank_mobile) {
        this.bank_mobile = bank_mobile;
    }

    public int getBank_province_id() {
        return bank_province_id;
    }

    public void setBank_province_id(int bank_province_id) {
        this.bank_province_id = bank_province_id;
    }

    public int getBank_city_id() {
        return bank_city_id;
    }

    public void setBank_city_id(int bank_city_id) {
        this.bank_city_id = bank_city_id;
    }

    public int getBank_area_id() {
        return bank_area_id;
    }

    public void setBank_area_id(int bank_area_id) {
        this.bank_area_id = bank_area_id;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getLicense_date() {
        return license_date;
    }

    public void setLicense_date(String license_date) {
        this.license_date = license_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public String toString() {
        return "BankInfo{" +
                "id=" + id +
                ", store_id=" + store_id +
                ", sub_type=" + sub_type +
                ", sub_name='" + sub_name + '\'' +
                ", sub_idcard='" + sub_idcard + '\'' +
                ", sub_bankname='" + sub_bankname + '\'' +
                ", sub_bankider='" + sub_bankider + '\'' +
                ", bank_mobile='" + bank_mobile + '\'' +
                ", bank_province_id=" + bank_province_id +
                ", bank_city_id=" + bank_city_id +
                ", bank_area_id=" + bank_area_id +
                ", store_address='" + store_address + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", contact_mobile='" + contact_mobile + '\'' +
                ", contact_email='" + contact_email + '\'' +
                ", license_number='" + license_number + '\'' +
                ", license_date='" + license_date + '\'' +
                ", remark='" + remark + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }
}
