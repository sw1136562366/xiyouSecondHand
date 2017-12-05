package com.sendhand.xiyousecondhand.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有活动
 * Created by Administrator on 2017/12/5 0005.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }

}
