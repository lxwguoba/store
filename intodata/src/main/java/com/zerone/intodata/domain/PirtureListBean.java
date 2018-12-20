package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-10.
 */

public class PirtureListBean implements Serializable {
    private String purl;
    private String pname;
    private int p_type;

    public PirtureListBean() {
    }

    /**
     *
     * @param purl 图片地址
     * @param pname 名字
     * @param p_type 类型
     */
    public PirtureListBean(String purl, String pname, int p_type) {
        this.purl = purl;
        this.pname = pname;
        this.p_type = p_type;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getP_type() {
        return p_type;
    }

    public void setP_type(int p_type) {
        this.p_type = p_type;
    }
}
