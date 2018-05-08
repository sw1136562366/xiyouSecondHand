package com.sendhand.xiyousecondhand.view.fragment.home.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class RongYunUtil {

    /**
     * 融云连接时提供的随机数
     * @return
     */
    public static String rand() {
        Random rand = new Random();
        int r = rand.nextInt(1000000);
        return String.valueOf(r);
    }

    public static String SHA1(String decript) {
               try {
                   MessageDigest digest = java.security.MessageDigest
                  .getInstance("SHA-1");
                  digest.update(decript.getBytes());
                    byte messageDigest[] = digest.digest();
                    // Create Hex String
                    StringBuffer hexString = new StringBuffer();
                   // 字节数组转换为 十六进制 数
                    for (int i = 0; i < messageDigest.length; i++) {
                           String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                           if (shaHex.length() < 2) {
                                     hexString.append(0);
                                }
                             hexString.append(shaHex);
                        }
                    return hexString.toString();

               } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return "";
             }
}
