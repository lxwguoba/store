package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-05.
 */

public class PostersBean implements Serializable {
    /**
     * ordersn : HB20181205030209A2A125189
     * order_price : 0.00
     * status : 3
     * created_at : 1543993329
     * share_code : 未受理
     * ad_num : null
     * shop_num : null
     * name : 简版海报
     * product_price : 0.00
     * type : 0
     * day_num : 0
     * day_person : 0
     */

    private String ordersn;
    private String order_price;
    private String status;
    private String created_at;
    private String share_code;
    private int ad_num;
    private int shop_num;
    private String name;
    private String product_price;
    private int type;
    private int day_num;
    private int day_person;

    public String getOrdersn() {
        return ordersn;
    }

    public void setOrdersn(String ordersn) {
        this.ordersn = ordersn;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }

    public int getAd_num() {
        return ad_num;
    }

    public void setAd_num(int ad_num) {
        this.ad_num = ad_num;
    }

    public int getShop_num() {
        return shop_num;
    }

    public void setShop_num(int shop_num) {
        this.shop_num = shop_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDay_num() {
        return day_num;
    }

    public void setDay_num(int day_num) {
        this.day_num = day_num;
    }

    public int getDay_person() {
        return day_person;
    }

    public void setDay_person(int day_person) {
        this.day_person = day_person;
    }
}
