package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-04.
 */

public class EditShopInfoBean implements Serializable {
    /**
     * store_id : 2
     * realname : 果粒橙
     * mobile : 15277049514
     * kf_mobile : 13798336280
     * logo : uploads/store/20181130034311840.jpg
     * theme_img : null
     * name : 零售店1
     */

    private int store_id;
    private String realname;
    private String mobile;
    private String kf_mobile;
    private String logo;
    private Object theme_img;
    private String name;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKf_mobile() {
        return kf_mobile;
    }

    public void setKf_mobile(String kf_mobile) {
        this.kf_mobile = kf_mobile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Object getTheme_img() {
        return theme_img;
    }

    public void setTheme_img(Object theme_img) {
        this.theme_img = theme_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
