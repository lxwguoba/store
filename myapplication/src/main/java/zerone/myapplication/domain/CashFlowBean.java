package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-10.
 */

public class CashFlowBean implements Serializable {
    private Long orderId;
    private String customerName;
    private  String orderNumber;
    private String orderCreateTime;
    private String orederMoney;
    private String orderPrice;
    private String orderStatus;

    public CashFlowBean(){

    }
    public CashFlowBean(String orderNumber, String orderCreateTime, String orederMoney, String orderPrice, String orderStatus) {
        this.orderNumber = orderNumber;
        this.orderCreateTime = orderCreateTime;
        this.orederMoney = orederMoney;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getOrederMoney() {
        return orederMoney;
    }

    public void setOrederMoney(String orederMoney) {
        this.orederMoney = orederMoney;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
