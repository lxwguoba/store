package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-01.
 */

public class CategoryBean implements Serializable {

    /**
     * id : 3
     * name : 分类一
     * displayorder : 0
     * created_at : 1543565202
     */

    private int id;
    private String name;
    private int displayorder;
    private String created_at;

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

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
