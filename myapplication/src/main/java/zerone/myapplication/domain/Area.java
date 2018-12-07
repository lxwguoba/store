package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-06.
 */

public class Area implements Serializable {

    /**
     * id : 39
     * area_name : 桥东区
     * city_id : 5
     */

    private int id;
    private String area_name;
    private int city_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", area_name='" + area_name + '\'' +
                ", city_id=" + city_id +
                '}';
    }
}
