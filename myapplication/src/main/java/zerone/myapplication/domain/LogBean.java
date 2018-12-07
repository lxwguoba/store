package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-17.
 */

public class LogBean  implements Serializable{


    /**
     * username : 10008
     * ip : 116.7.46.83
     * ip_position : 中国广东深圳
     * created_at : 1543889562
     */

    private String username;
    private String ip;
    private String ip_position;
    private String created_at;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp_position() {
        return ip_position;
    }

    public void setIp_position(String ip_position) {
        this.ip_position = ip_position;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
