package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-18.
 */

public class IntoListBean implements Serializable {
    private int type;
    private String  stroe_id;
    private String stroe_name;
    private String stroe_create_tme;
    private String stroe_address;
    private String stroe_lmzk;
    private int is_apply;
    private String  remark;


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(int is_apply) {
        this.is_apply = is_apply;
    }

    public String getStroe_id() {
        return stroe_id;
    }

    public void setStroe_id(String stroe_id) {
        this.stroe_id = stroe_id;
    }

    public String getStroe_name() {
        return stroe_name;
    }

    public void setStroe_name(String stroe_name) {
        this.stroe_name = stroe_name;
    }

    public String getStroe_create_tme() {
        return stroe_create_tme;
    }

    public void setStroe_create_tme(String stroe_create_tme) {
        this.stroe_create_tme = stroe_create_tme;
    }

    public String getStroe_address() {
        return stroe_address;
    }

    public void setStroe_address(String stroe_address) {
        this.stroe_address = stroe_address;
    }

    public String getStroe_lmzk() {
        return stroe_lmzk;
    }

    public void setStroe_lmzk(String stroe_lmzk) {
        this.stroe_lmzk = stroe_lmzk;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
