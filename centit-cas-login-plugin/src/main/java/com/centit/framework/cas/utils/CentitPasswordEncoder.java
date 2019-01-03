package com.centit.framework.cas.utils;


import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.Md5Encoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * Created by codefan on 17-1-20.
 * 采用Spring 推荐的 BCryptPasswordEncoder 加密方式，并进行改造，添加了自定义的盐，而不是随机的盐
 *
 * 这个密码的加密方式是：
 * 1. 前端输入密码，在post之前用md5散列一次
 * 2. 后端 验证是 需要用用户（userDetails）中获取一个主键（不能修改的属性)作为盐，（isPasswordValid）
 * ------------------------------密码设置---------------------------------------
 * 1. 用户修改密码时，新旧密码都在前端md 5 散列一次，所以在设置用户pin信息是需要调用 encodePassword
 * 2. 后台设置密码时，最好也要对密码md5一次，如果没有md5 需要调用 createPassword
 *
 */
@SuppressWarnings("deprecation")
public class CentitPasswordEncoder implements PasswordEncoder {
    private int strength;

    public CentitPasswordEncoder() {
        this.strength = 11;
    }

    public CentitPasswordEncoder(int strength) {
        if(strength<5 ||strength >31){
            this.strength = strength;
        }else {
            this.strength = 11;
        }
    }

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return CentitPasswordEncoder.createPassword(rawPass, StringBaseOpt.castObjectToString(salt),strength);
    }

    @Override
    public boolean isPasswordValid(String encodedPassword, String rawPass, Object salt) {
        return StringUtils.equals(
                encodedPassword,
                CentitPasswordEncoder.createPassword(rawPass, StringBaseOpt.castObjectToString(salt),strength));
    }

    //salt 可能为null
    public static String createPassword(String password, String rawSalt, int logRounds) {
        String salt = StringUtils.isBlank(rawSalt)?"salt":rawSalt;
        try {
            BCrypt b = new BCrypt();
            Method method = BCrypt.class.getDeclaredMethod("crypt_raw", byte[].class,byte[].class,int.class);
            method.setAccessible(true); //没有设置就会报错
            byte[] pb = (byte[]) method.invoke(b,password.getBytes(), salt.getBytes(), logRounds );
            return new String(Hex.encodeHex(pb));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Md5Encoder.encodePasswordAsJasigCas(password, salt, logRounds);
        }
    }
}
