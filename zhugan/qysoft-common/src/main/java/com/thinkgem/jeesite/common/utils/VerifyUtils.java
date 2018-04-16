package com.thinkgem.jeesite.common.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * Created by cai on 2017/5/22.
 */
public class VerifyUtils {
    /**
     * 手机号验证
     * @param mobile
     * @return
     */
    public static Boolean isMobile(String mobile) {
        String pattern = "^1\\d{10}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(mobile);
        return m.matches ();
    }

    /**
     * 密码验证
     * @param password
     * @return
     */
    public static Boolean isPassword(String password) {
        String pattern = "^[0-9A-Za-z]{8,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(password);
        return m.matches ();
    }


    /**
     * 银行卡验证
     * @param bankCode
     * @return
     */
    public static Boolean isBankCard(String bankCode) {
        String pattern = "^([0-9]{16}|[0-9]{19})$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bankCode);
        return m.matches ();
    }


    /**
     * 验证正整数
     * @param number
     * @return
     */
    public static Boolean isInteger(String number) {
        String pattern = "^[1-9][0-9]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(number.toString());
        return m.matches ();
    }

    /**
     * 验证正整数
     * @param number
     * @return
     */
    public static Boolean isInteger(BigDecimal number) {
      return  isInteger(number.toString());
    }

    /**
     * 判断是否是数字,可以是小数(两位)
     * @param number
     * @return
     */
    public static Boolean isNumber(String number) {
        String pattern = "^\\d{1,5}(\\.\\d{1,2})?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(number);
        return m.matches ();
    }
}
