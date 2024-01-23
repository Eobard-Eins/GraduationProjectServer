package com.example.graduation.user.controller;

import com.example.graduation.user.model.User;
import com.example.graduation.user.service.UserInfoService;
import com.example.graduation.utils.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userInfo")
/**
 * @desc 用户信息相关
 */
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/setPassword")
    public Res<Boolean> setPassword(String phone, String password){
        return userInfoService.setPassword(phone, password);
    }

    @PutMapping("/setNameAndAvatar")
    public Res<Boolean> setNameAndAvatar(@RequestParam("file") MultipartFile uploadFile, String phone, String username){
        //TODO: 设置头像和用户名，调用两个函数分别设置
        return userInfoService.setNameAndAvatar(phone, username, uploadFile);
    }

    @PutMapping("/setName")
    public Res<Boolean> setName(String phone, String username) {
        return userInfoService.setName(phone, username);
    }

    @PutMapping("/setAvatar")
    public Res<Boolean> setAvatar(String phone, MultipartFile uploadFile){
        return userInfoService.setAvatar(phone, uploadFile);
    }

    @PutMapping("/setLongitudeAndLatitude")
    public Res<Boolean> setLongitudeAndLatitude(String phone, Double longitude, Double latitude){
        return userInfoService.setLongitudeAndLatitude(phone, longitude, latitude);
    }

    @PutMapping("/setPoint")
    public Res<Boolean> setPoint(String phone, Double point){
        return userInfoService.setPoint(phone, point);
    }


    @PutMapping("/setRegistered")
    public Res<Boolean> setRegistered(String phone, boolean registered){
        return userInfoService.setRegistered(phone, registered);
    }

    /**
     * 获取用户信息
     * @param phone 手机号码
     * @return 用户信息实体类对象
     */
    @GetMapping("/getInfo")
    public Res<User> getInfo(String phone){
        return userInfoService.getInfo(phone);
    }

}
