package com.example.dpene.wallefy.model.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dpene on 4/7/2016.
 */
public class RegisterHelper {

    public static String md5(String password) {
        try {

            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean validateEmail(String email) {
        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email != null && email.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static boolean validateUsername(String username) {
        String pattern = "(?=.*[a-z]).{3,}";
        if (username != null && username.matches(pattern)) {
            return true;
        }
        return false;
    }

    public static boolean strongPassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z]).{5,10}";
        if (password != null && password.matches(pattern)) {
            return true;
        }
        return false;

    }

}
