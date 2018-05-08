package com.sendhand.xiyousecondhand.entry;

import static com.sendhand.xiyousecondhand.view.fragment.home.utils.RongYunUtil.SHA1;
import static com.sendhand.xiyousecondhand.view.fragment.home.utils.RongYunUtil.rand;

/**
 * 服务端url常量
 * Created by Administrator on 2017/12/5 0005.
 */

public class Constants {

    //融云appsecret
    public static String RONGYUN_APPSECRET = "PH0quShHAou9";

    public static final String APP_KEY = "cpj2xarlc7man";
    public static String appSecret = "PH0quShHAou9"; // 开发者平台分配的 App Secret。
    public static String nonce = rand(); // 获取随机数。
    public static String timestamp = String.valueOf(System.currentTimeMillis()); // 获取时间戳（毫秒）。
    public static String signature = SHA1(appSecret + nonce + timestamp); //數字簽名

    public static String GET_TOKEN_URL = "http://api.cn.ronghub.com/user/getToken.json";

    private static final String PROTOCOL = "http://";
    private static final String HOST = "118.24.22.165";
    private static final String PORT = "8080";
    //与服务端连接的基础URL
    public static final String BASE_URL = PROTOCOL + HOST + ":" + PORT + "/shop/";
    //登录
    public static final String LOGIN_URL = BASE_URL + "user.do?action=login";
    //注册
    public static final String REGISTER_URL = BASE_URL + "user.do?action=add";
    //验证手机号
    public static final String VALIDATE_PHONE_URL = BASE_URL + "user.do?action=isExit";
    //重置密码
    public static final String RESETTING_PASSWORD_URL = BASE_URL + "user.do?action=reset";
    //验证登录
    public static final String VALIDATE_LOGIN_URL = BASE_URL + "user.do?action=check";
    //加载服务器图片
    public static final String GET_IMAGE_BASEURL = PROTOCOL + HOST + ":" + PORT + "/";
    //上传图片
    public static final String UPLOAD_IMAGE_URL = BASE_URL + "user.do?action=modifyPic";
    //修改信息
    public static final String MODIFY_USERNAME_URL = BASE_URL + "user.do?action=modifyUserName";
    public static final String MODIFY_SEX_URL = BASE_URL + "user.do?action=modifySex";
    public static final String MODIFY_AGE_URL = BASE_URL + "user.do?action=modifyAge";
    public static final String MODIFY_SCHOOL_URL = BASE_URL + "user.do?action=modifySchool";
    public static final String MODIFY_EMAIL_URL = BASE_URL + "user.do?action=modifyEmail";
    public static final String MODIFY_POSTWD_URL = BASE_URL + "user.do?action=modifyPostwd";
    public static final String MODIFY_ADDRESS_URL = BASE_URL + "user.do?action=modifyAddress";
}
