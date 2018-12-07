package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-21.
 * 消费者列表 Consumers 消费者
 */

public class ConsumersBean implements Serializable {

    /**
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/lnfg7lbvgnavQRnI7nxG5RWedaUk8mbJcB2CZvvwlz8veKibXtXVoy039icBic2DZWsicmpuDOHQdzmib4y0xzfWfNA/132
     * nickname : 时光取名叫无心
     * mobile : 17688713334
     * shopping_times : 1
     * amount : 10.20
     * last_time : 1543045807
     * first_time : 1543045807
     */

    private String headimgurl;
    private String nickname;
    private String mobile;
    private int shopping_times;
    private String amount;
    private String last_time;
    private String  first_time;

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getShopping_times() {
        return shopping_times;
    }

    public void setShopping_times(int shopping_times) {
        this.shopping_times = shopping_times;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }
}
