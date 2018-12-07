package zerone.myapplication.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-07.
 */

public class ImageBean implements Serializable {

    /**
     * license : http://zerone.01nnt.com/uploads/store/apply/2/15433995484380.png
     * idcard_pos : http://zerone.01nnt.com/uploads/store/apply/2/15433999136645.png
     * idcard_oth : http://zerone.01nnt.com/uploads/store/apply/2/15434001205882.png
     * opening_permit : http://zerone.01nnt.com/uploads/store/apply/2/15434001244900.png
     * bank_card : http://zerone.01nnt.com/uploads/store/apply/2/15434001268993.png
     * door_photo : http://zerone.01nnt.com/uploads/store/apply/2/15434001269909.png
     * shop_photo : http://zerone.01nnt.com/uploads/store/apply/2/15434001295891.png
     * cashier_photo : http://zerone.01nnt.com/uploads/store/apply/2/15434001314983.png
     * increment_agree : http://zerone.01nnt.com/uploads/store/apply/2/15434001325344.png
     * is_apply : 0
     */
    private String license;
    private String idcard_pos;
    private String idcard_oth;
    private String opening_permit;
    private String bank_card;
    private String door_photo;
    private String shop_photo;
    private String cashier_photo;
    private String increment_agree;
    private int is_apply;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getIdcard_pos() {
        return idcard_pos;
    }

    public void setIdcard_pos(String idcard_pos) {
        this.idcard_pos = idcard_pos;
    }

    public String getIdcard_oth() {
        return idcard_oth;
    }

    public void setIdcard_oth(String idcard_oth) {
        this.idcard_oth = idcard_oth;
    }

    public String getOpening_permit() {
        return opening_permit;
    }

    public void setOpening_permit(String opening_permit) {
        this.opening_permit = opening_permit;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getDoor_photo() {
        return door_photo;
    }

    public void setDoor_photo(String door_photo) {
        this.door_photo = door_photo;
    }

    public String getShop_photo() {
        return shop_photo;
    }

    public void setShop_photo(String shop_photo) {
        this.shop_photo = shop_photo;
    }

    public String getCashier_photo() {
        return cashier_photo;
    }

    public void setCashier_photo(String cashier_photo) {
        this.cashier_photo = cashier_photo;
    }

    public String getIncrement_agree() {
        return increment_agree;
    }

    public void setIncrement_agree(String increment_agree) {
        this.increment_agree = increment_agree;
    }

    public int getIs_apply() {
        return is_apply;
    }

    public void setIs_apply(int is_apply) {
        this.is_apply = is_apply;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "is_apply=" + is_apply +
                '}';
    }
}
