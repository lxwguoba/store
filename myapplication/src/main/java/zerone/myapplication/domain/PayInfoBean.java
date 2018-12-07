package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-22.
 */

public class PayInfoBean implements Serializable{


    /**
     * nickname : 果粒橙
     * payment_price : 10398.04
     * order_price : 10888.00
     * created_at : 1543399254
     * status : 1
     */

    private String nickname;
    private String payment_price;
    private String order_price;
    private String created_at;
    private String status;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPayment_price() {
        return payment_price;
    }

    public void setPayment_price(String payment_price) {
        this.payment_price = payment_price;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
