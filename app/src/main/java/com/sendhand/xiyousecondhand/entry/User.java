package com.sendhand.xiyousecondhand.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 用户属性
 * Created by Administrator on 2017/12/8 0008.
 */

public class User extends BmobObject implements Serializable {
    private Integer id;
    private String tel;
    private String password;
    private String username;
    private String school;
    private String sex;
    private Integer age;
    private String postwd;
    private String address;
    private BmobFile pic;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPostwd() {
        return postwd;
    }

    public void setPostwd(String postwd) {
        this.postwd = postwd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    //    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(tel);
//        parcel.writeString(username);
//        parcel.writeString(school);
//        parcel.writeString(sex);
//        parcel.writeInt(age);
//        parcel.writeString(postwd);
//        parcel.writeString(address);
//        parcel.writeString(pic);
//        parcel.writeString(email);
//    }
//
//    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel parcel) {
//            User user = new User();
//            user.tel = parcel.readString();
//            user.username = parcel.readString();
//            user.school = parcel.readString();
//            user.sex = parcel.readString();
//            user.age = parcel.readInt();
//            user.postwd = parcel.readString();
//            user.address = parcel.readString();
//            user.pic = parcel.readString();
//            user.email = parcel.readString();
//            return user;
//        }
//
//        @Override
//        public User[] newArray(int i) {
//            return new User[i];
//        }
//    };
}
