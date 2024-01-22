package com.example.graduation.user.controller;

import com.example.graduation.user.service.UserInfoService;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.result;
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
    public result<Boolean> setPassword(String phone, String password){
        return userInfoService.setPassword(phone, password);
    }

    @PutMapping("/setNameAndAvatar")
    public result<Boolean> setNameAndAvatar(@RequestParam("file") MultipartFile uploadFile, String phone, String username){
        //TODO: 设置头像和用户名，调用两个函数分别设置
        return userInfoService.setNameAndAvatar(phone, username, uploadFile);
    }
}
