package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-11-16.
 */

public class ProductBean implements Serializable {

    /**
     * id : 3
     * name : 牛奶
     * category_id : 3
     * description : 这是牛奶
     * old_price : 0.00
     * price : 10.00
     * displayorder : 0
     * created_at : 1543646578
     * category_name : 牛奶
     * thumb : http://zerone.01nnt.com/uploads/store/20181201031430491.jpg
     */

    private int id;
    private String name;
    private int category_id;
    private String description;
    private String old_price;
    private String price;
    private int displayorder;
    private String created_at;
    private String category_name;
    private String thumb;

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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
