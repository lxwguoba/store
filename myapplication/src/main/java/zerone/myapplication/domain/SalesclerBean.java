package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-24.
 */

public class SalesclerBean implements Serializable {

    /**
     * id : 55
     * username : 10049
     * mobile : 18488988
     * realname : 张三
     * created_at : 1544064398
     */

    private int id;
    private String username;
    private String mobile;
    private String realname;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
