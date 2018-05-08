package com.sendhand.xiyousecondhand.view.fragment.home.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.sendhand.xiyousecondhand.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class ImageUtil {

    public static Uri getUriFromDrawableRes(int id) {
        Resources resources = MyApplication.getContext().getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    /** * 图片转成string *  * @param bitmap * @return
     * @param bitmap*/
    public static String convertIconToString(int bitmapInt) {
        Resources resources = MyApplication.getContext().getResources();
        Bitmap bitmap =  BitmapFactory.decodeResource(resources, bitmapInt);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }


    /** * string转成bitmap *  * @param st */
    public static Bitmap convertStringToIcon(String st) {
         OutputStream out;
        Bitmap bitmap = null;
        try {
             out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
             bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) { return null; } }

}
