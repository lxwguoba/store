package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-06.
 */

public class CollectionData implements Serializable {

    /**
     * sub_type : 1
     * sub_name : 果粒橙
     * sub_idcard : 452731199302020202 银行卡号
     * sub_address : 深圳市龙岗支行
     * sub_bankname : 招商银行
     * sub_bankider : 530800
     * bank_mobile : 15277049514
     * bank_province_id : 10
     * bank_city_id : 79
     * bank_area_id : 872
     * is_apply : 0
     */

    private int sub_type;
    private String sub_name;
    private String sub_idcard;
    private String sub_address;
    private String sub_bankname;
    private String sub_bankider;
    private String bank_mobile;
    private int bank_province_id;
    private int bank_city_id;
    private int bank_area_id;
    private int is_apply;

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

    public String getSub_address() {
        return sub_address;
    }

    public void setSub_address(String sub_address) {
        this.sub_address = sub_address;
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

    public int getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(int is_apply) {
        this.is_apply = is_apply;
    }

    @Override
    public String toString() {
        return "CollectionData{" +
                "sub_type=" + sub_type +
                ", sub_name='" + sub_name + '\'' +
                ", sub_idcard='" + sub_idcard + '\'' +
                ", sub_address='" + sub_address + '\'' +
                ", sub_bankname='" + sub_bankname + '\'' +
                ", sub_bankider='" + sub_bankider + '\'' +
                ", bank_mobile='" + bank_mobile + '\'' +
                ", bank_province_id=" + bank_province_id +
                ", bank_city_id=" + bank_city_id +
                ", bank_area_id=" + bank_area_id +
                ", is_apply=" + is_apply +
                '}';
    }
}
