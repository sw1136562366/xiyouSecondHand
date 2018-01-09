package com.sendhand.xiyousecondhand.entry;

/**
 * 服务端url常量
 * Created by Administrator on 2017/12/5 0005.
 */

public class Constants {
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
    public static final String UPLOAD_IMAGE_URL = BASE_URL + "  ";


}
