package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-04.
 */

public class DoOperationBean implements Serializable{


    /**
     * username : 10001
     * realname : 果粒橙
     * operation_info : 修改了用户密码
     * created_at : 1543832394
     */

    private String username;
    private String realname;
    private String operation_info;
    private String created_at;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getOperation_info() {
        return operation_info;
    }

    public void setOperation_info(String operation_info) {
        this.operation_info = operation_info;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
