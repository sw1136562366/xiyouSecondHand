package com.sendhand.xiyousecondhand.application;

import android.app.Application;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;

import org.litepal.LitePal;

/**
 * 获取全局Context
 * Created by Administrator on 2017/12/4 0004.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
        MobSDK.init(context);
    }

    public static Context getContext() {
        return context;
    }
}
