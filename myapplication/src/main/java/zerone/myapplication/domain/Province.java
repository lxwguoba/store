package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-06.
 */

public class Province implements Serializable {
    /**
     * id : 0
     * province_name : 请选择
     */
    private int id;
    private String province_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", province_name='" + province_name + '\'' +
                '}';
    }
}
