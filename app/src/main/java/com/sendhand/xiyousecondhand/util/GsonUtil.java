package com.sendhand.xiyousecondhand.util;

import com.google.gson.Gson;
import com.sendhand.xiyousecondhand.entry.User;

/**
 * 解析json工具类
 * Created by Administrator on 2017/12/8 0008.
 */

public class GsonUtil {
    private static Gson gson;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static User parseJsonWithGson(String jsonData) {
        User user = gson.fromJson(jsonData, User.class);
        return user;
    }
}
