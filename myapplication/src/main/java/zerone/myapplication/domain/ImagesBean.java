package zerone.myapplication.domain;

/**
 * Created by 刘兴文 on 2018-12-03.
 */

public class ImagesBean {

    /**
     * id : 1
     * goods_id : 1
     * thumb : http://zerone.01nnt.com/uploads/store/20181203114040579.jpg
     * displayorder : 0
     * status : 1     1为主图，0为幅图
     */
    private int id;
    private int goods_id;
    private String thumb;
    private int displayorder;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(int displayorder) {
        this.displayorder = displayorder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
