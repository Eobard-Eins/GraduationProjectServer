package com.example.graduation.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.graduation.project.model.User;
import com.example.graduation.project.service.UserInfoService;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userInfo")
/**
 * @desc 用户信息相关
 */
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 设置密码
     * @param info
     * @return setPasswordError/netError/infoMiss
     */
    @PutMapping("/setPassword")
    public Res<Boolean> setPassword(@RequestBody String info){
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("password"))
           return userInfoService.setPassword((String) json.get("mailAddress"), (String) json.get("password"));
        return Res.Error(status.infoMiss);
    }

    /**
     * 设置用户名
     * @param info
     * @return infoMiss/netError/setUsernameError
     */
    @PutMapping("/setName")
    public Res<Boolean> setName(@RequestBody String info) {
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("username"))
            return userInfoService.setName((String) json.get("mailAddress"), (String) json.get("username"));
        return Res.Error(status.infoMiss);
    }

    /**
     * 设置头像
     * @param uploadFile
     * @param mailAddress
     * @return userNotExist/ossError/setAvatarError/netError
     */
    @PostMapping("/setAvatar")
    public Res<Boolean> setAvatar(@RequestParam("file") MultipartFile uploadFile, @RequestParam("pn") String mailAddress){
        return userInfoService.setAvatar(mailAddress, uploadFile);
    }

    /**
     * 设置积分
     * @param info
     * @return setPointError/netError/infoMiss
     */
    @PutMapping("/setPoint")
    public Res<Boolean> setPoint(@RequestBody String info){
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("point"))
            return userInfoService.setPoint((String) json.get("mailAddress"), (Double) json.get("point"));
        return Res.Error(status.infoMiss);
    }

    /**
     * 获取用户信息
     * @param mailAddress 手机号码
     * @return netError/userNotExist
     */
    @GetMapping("/getInfo")
    public Res<User> getInfo(@RequestParam("mailAddress") String mailAddress){
        return userInfoService.getInfo(mailAddress);
    }

}
