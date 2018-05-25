package com.coder520.common.util;


import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *密码加密算法
 */

public class SerurituUtils {

    public static String encrptyPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //使用MD5加密
        MessageDigest md5=MessageDigest.getInstance("MD5");
        //防止出现乱码
        BASE64Encoder base64Encoder=new BASE64Encoder();
        String result=base64Encoder.encode(md5.digest(password.getBytes("utf-8")));

        return result;
    }

    //检查密码是否正确
    public static boolean checkPassword(String inputPwd,String dbPwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String result= encrptyPassword(inputPwd);
        if(result.equals(dbPwd)){
            return true;
        }else{
            return false;
        }
    }

}
