package com.sendhand.xiyousecondhand.entry;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class Goods extends BmobObject implements Serializable {
    private String title;
    private String desc;
    private Double price;
    private BmobFile images;
    private Integer cat;
    private BmobDate publishDate;
    private BmobGeoPoint position;
    private User user; //发布者
    private Integer status;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BmobFile getImages() {
        return images;
    }

    public void setImages(BmobFile images) {
        this.images = images;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public BmobDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(BmobDate publishDate) {
        this.publishDate = publishDate;
    }

    public BmobGeoPoint getPosition() {
        return position;
    }

    public void setPosition(BmobGeoPoint position) {
        this.position = position;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
