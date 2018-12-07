package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-15.
 *
 */


public class Customer implements Serializable {
    public String username;
    public String paymoney;
    public String userhead;
    public String logintime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }
}
