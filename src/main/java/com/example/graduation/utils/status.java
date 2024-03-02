package com.example.graduation.utils;

public class status {
    static public int success=1000;//成功
    static public int successButUserNotExist=1001;//成功登录但是用户不存在

    /**
     * 手机号、验证码、密码
     * */
    static public int phoneFormatError=1100;//手机号格式错误
    static public int passwordFormatError=1101;//密码格式错误
    static public int passwordError=1102;//密码错误
    static public int captchaError=1103;//验证码错误
    static public int passwordInconsistent=1104;//密码不一致
    static public int setPasswordError=1105;//通信数据库设置密码错误
    static public int captchaExpiration=1106;//验证码过期

    /**
     * 用户信息
     */
    static public int userNotExist=1200;//用户不存在
    static public int userExist=1201;//用户已存在
    static public int setUsernameError=1202;//通信数据库设置用户名时出现错误
    static public int setLongitudeAndLatitudeError=1203;//通信数据库设置经纬度时出现错误
    static public int setPointError=1204;//通信数据库设置积分时出现错误
    static public int setRegisteredError=1204;//通信数据库设置注册状态时出现错误
    static public int setAvatarError=1205;//通信数据库设置头像时出现错误
    static public int avatarMissing=1206;//头像路径为空
    /**
     * 网络
     */
    static public int netError=1300;//网络错误
    static public int ossError=1301;//Oss服务器错误
    static public int infoMiss=1302;//传输信息缺失
    static public int mailServiceError=1303;//邮箱验证码服务错误

    //不存在状态码
    static public int noStatusCode=0000;//其他
}
