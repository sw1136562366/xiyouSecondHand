package com.sendhand.xiyousecondhand.entry;

/**
 * Created by Administrator on 2018/4/24 0024.
 * 生成融云Token时返回的数据格式
 */

public class RYReturnData {
    public int code;
    public String userId;
    public String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
