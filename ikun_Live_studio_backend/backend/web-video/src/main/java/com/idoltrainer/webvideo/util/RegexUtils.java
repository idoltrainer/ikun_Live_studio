package com.idoltrainer.webvideo.util;


import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验数据是否符合规范，先不加强校验，先用简单的
 *  密码校验   用户名校验
 */
public class RegexUtils {
    //密码长度为4到20位,必须包含字母和数字，字母区分大小写
    private static String regEx1 = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{4,20})$";
    //密码中必须包含字母、数字、特称字符，至少6个字符，最多16个字符
    private static String regEx2 = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{6,16})$";

    //校验https格式
    private static String httpRegex1 = "/https?\\/\\/:[-a-z0-9]+(\\.[-a-z0-9])*\\.(com|cn|edu|uk)\\/[-a-z0-9_:@&?=+,.!/~*'%$]*/ig";
    private static String httpRegex2 = "/http?\\/\\/:[-a-z0-9]+(\\.[-a-z0-9])*\\.(com|cn|edu|uk)\\/[-a-z0-9_:@&?=+,.!/~*'%$]*/ig";
    /**
     * 密码长度为4到20位,必须包含字母和数字，字母区分大小写
     * @param password
     * @return
     */
    public static boolean checkPassword(String password){
        Pattern Password_Pattern = Pattern.compile(regEx1);
        Matcher matcher = Password_Pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 密码中必须包含字母、数字、特称字符，至少8个字符，最多16个字符
     * @param password
     * @return
     */
    public static boolean password(String password){
        Pattern Password_Pattern = Pattern.compile(regEx2);
        Matcher matcher = Password_Pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


}

