package com.zerone.intodata.domain;

import java.io.Serializable;

/**
 * Created by 刘兴文 on 2018-12-19.
 */

public class PictureInfoBean implements Serializable {

    /**
     * license :
     * idcard_pos :
     * idcard_oth :
     * opening_permit :
     * bank_card :
     * relation :
     * hand_idcard :
     * door_photo :
     * shop_photo :
     * cashier_photo :
     * increment_agree :
     * special_agree :
     * fas_idcard_pos :
     * fas_idcard_oth :
     * person_prove :
     */

    private String license;
    private String idcard_pos;
    private String idcard_oth;
    private String opening_permit;
    private String bank_card;
    private String relation;
    private String hand_idcard;
    private String door_photo;
    private String shop_photo;
    private String cashier_photo;
    private String increment_agree;
    private String special_agree;
    private String fas_idcard_pos;
    private String fas_idcard_oth;
    private String person_prove;

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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHand_idcard() {
        return hand_idcard;
    }

    public void setHand_idcard(String hand_idcard) {
        this.hand_idcard = hand_idcard;
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

    public String getSpecial_agree() {
        return special_agree;
    }

    public void setSpecial_agree(String special_agree) {
        this.special_agree = special_agree;
    }

    public String getFas_idcard_pos() {
        return fas_idcard_pos;
    }

    public void setFas_idcard_pos(String fas_idcard_pos) {
        this.fas_idcard_pos = fas_idcard_pos;
    }

    public String getFas_idcard_oth() {
        return fas_idcard_oth;
    }

    public void setFas_idcard_oth(String fas_idcard_oth) {
        this.fas_idcard_oth = fas_idcard_oth;
    }

    public String getPerson_prove() {
        return person_prove;
    }

    public void setPerson_prove(String person_prove) {
        this.person_prove = person_prove;
    }
}
