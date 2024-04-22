package com.example.graduation.utils;

public class status {
    static public int success=1000;//成功
    static public int successButUserNotExist=1001;//成功登录但是用户不存在

    /**
     * 手机号、验证码、密码
     * */
    static public int mailFormatError=1100;//手机号格式错误
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
    static public int userGetError=1207;
    static public int userPointNotEnough=1208;
    /**
     * 网络
     */
    static public int netError=1300;//网络错误
    static public int ossError=1301;//Oss服务器错误
    static public int infoMiss=1302;//传输信息缺失
    static public int mailServiceError=1303;//邮箱验证码服务错误
    static public int pyServerError=1304;//PyServer错误

    /**
     * task状态
     */
    static public int taskAddError=1400;
    static public int taskGetError=1401;
    static public int taskPublic=1402;
    static public int taskRequestExist=1403;
    static public int taskStatusChangeError=1404;
    static public int taskBeAccessed=1404;
    static public int historyGetError=1405;
    static public int taskTimeout=1406;//超时
    static public int taskDone=1407;//处理完成

    /**
     * 动作状态
     */
    static public int click=1500;
    static public int like=1501;
    static public int chat=1502;
    static public int access=1503;
    static public int dislike=1504;
    static public int accessError=1505;
    static public int dislikeError=1506;
    static public int likeError=1507;
    static public int getAll=1508;

    //不存在状态码
    static public int noStatusCode=0000;//其他
}
