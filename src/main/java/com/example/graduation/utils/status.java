package com.example.graduation.utils;

public class status {
    static public int success=1000;//成功
    static public int successButUserNotExist=1001;//成功登录但是用户不存在

    //手机号、验证码、密码
    static public int phoneFormatError=1100;//手机号格式错误
    static public int passwordFormatError=1101;//密码格式错误
    static public int passwordError=1102;//密码错误
    static public int captchaError=1103;//验证码错误
    static public int passwordInconsistent=1104;//密码不一致
    static public int setPasswordError=1105;//设置密码错误

    //用户
    static public int userNotExist=1200;//用户不存在
    static public int userExist=1201;//用户已存在

    //网络
    static public int netError=1300;//网络错误

    //不存在状态码
    static public int noStatusCode=0000;//其他
}
