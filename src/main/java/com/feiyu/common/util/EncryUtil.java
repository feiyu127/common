package com.feiyu.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-30 14:28
 */
public class EncryUtil {

    public static String byte2hex(byte[] b) {

        if (b == null) {
            return null;
        }

        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0" + stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    public static void main(String[] args) {
        Integer b = 1;
        System.out.println(MD5("123456".getBytes()));

    }

    private static char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static String MD5(byte[] bytes) {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = md5MessageDigest.digest(bytes);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                byte b = digest[i];
                builder.append(md5String[b >>> 4 & 0XF]);
                builder.append(md5String[b & 0XF]);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
