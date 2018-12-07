package zerone.myapplication.domain;

/**
 * Created by Administrator on 2018-11-12.
 */

public class CashFlowListBean {

    /**
     * customerName : 锅巴来也
     * orderCreateTime : 2018-11-12 16:54:48
     * orderId : 1
     * orderNumber : XW20181112165448F1000001
     * orderPrice : 176
     * orderStatus : 1
     * orederMoney : 927
     */

    private String customerName;
    private String orderCreateTime;
    private int orderId;
    private String orderNumber;
    private String orderPrice;
    private String orderStatus;
    private String orederMoney;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getOrederMoney() {
        return orederMoney;
    }

    public void setOrederMoney(String orederMoney) {
        this.orederMoney = orederMoney;
    }
}
