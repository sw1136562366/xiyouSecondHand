package com.sendhand.xiyousecondhand.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static java.lang.String.valueOf;

/**
 * http请求工具类
 * Created by Administrator on 2017/12/4 0004.
 */

public class HttpUtil {
    public static OkHttpClient client = null;
    // 超时时间
    public static final int TIMEOUT = 1000 * 600;

    static {
        if (client == null) {
            client = new OkHttpClient();
            client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
    }

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute().body().string();
    }

    public static String post(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return client.newCall(request).execute().body().string();
    }

    public static void getCallback(String url, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postCallback(RequestBody requestBody, String url, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void post_file(final String url, final Map<String, String> map, File file,  okhttp3.Callback callback) {
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("pic", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newCall(request).enqueue(callback);
    }
}
