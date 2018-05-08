package com.sendhand.xiyousecondhand.view.fragment.home.main.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


import com.sendhand.xiyousecondhand.BR;

import java.io.Serializable;
import java.util.List;



public class HomeListEntity extends BaseObservable implements Serializable {
    private String objectId;
    private String tel;
    private double price;
    private String iconUrl;
    private String name;
    private String date;
    private String address;
    private String desc;
    private List<String> photoList;
    private String status;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTel() { return tel; }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        this.price = price;
    }

    @Bindable
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        notifyPropertyChanged(BR.iconUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
