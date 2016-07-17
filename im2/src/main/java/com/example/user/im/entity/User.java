package com.example.user.im.entity;

/**
 * Copyright  : 2015-2033 Beijing Startimes Communication & Network Technology Co.Ltd
 * <p/>
 * Created by xiongl on 2016/7/14..
 * ClassName  :
 * Description  :
 */
public class User {
    public enum Gendar {male, female}

    ;
    private String _id;
    private String account;
    private String email;
    private String picture;
    private String tel;
    private String birthday;
    private Gendar gendar;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Gendar getGendar() {
        return gendar;
    }

    public void setGendar(Gendar gendar) {
        this.gendar = gendar;
    }
}
