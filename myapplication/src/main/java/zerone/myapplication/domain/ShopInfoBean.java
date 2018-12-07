package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-01.
 */

public class ShopInfoBean  implements Serializable{


    /**
     * user_id : 2
     * store_id : 2
     * mobile : 15277049514
     * store_name : 零售店1
     * user_name : 果粒橙
     * store_address : 深圳市万汇大厦
     */


    private int user_id;
    private int store_id;
    private String mobile;
    private String store_name;
    private String user_name;
    private String store_address;
    private String safe_password;

    public String getSafe_password() {
        return safe_password;
    }

    public void setSafe_password(String safe_password) {
        this.safe_password = safe_password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }
}
