package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-05.
 */

public class CouponsBean implements Serializable{

    /**
     * id : 13
     * name : 测试优惠券
     * full_price : 0.00
     * ratio : 9
     * num : 100
     * start_time : 1543939200
     * end_time : 1546617600
     */

    private int id;
    private String name;
    private String full_price;
    private float ratio;
    private int num;
    private int start_time;
    private int end_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_price() {
        return full_price;
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }
}
