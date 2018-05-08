package com.sendhand.xiyousecondhand;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.mob.MobApplication;
import com.mob.MobSDK;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;

/**
 * 获取全局Context
 * Created by Administrator on 2017/12/4 0004.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        MobSDK.init(context);
        //初始化Fresco图片加载框架
        OkHttpClient mOkHttpClient = new OkHttpClient();
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(this, mOkHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, imagePipelineConfig);
        RongIM.init(this);
        Bmob.initialize(this, "42a8614c8b2a17cd5e60c7931ef4f58f");
//        SDKInitializer.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }
}
