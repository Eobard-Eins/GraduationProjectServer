package com.example.graduation.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.graduation.user.model.User;
import com.example.graduation.user.service.UserInfoService;
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

    @PutMapping("/setPassword")
    public Res<Boolean> setPassword(@RequestBody String info){
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("password"))
           return userInfoService.setPassword((String) json.get("mailAddress"), (String) json.get("password"));
        return Res.Error(status.infoMiss);
    }

//    @PostMapping("/setNameAndAvatar")
//    public Res<Boolean> setNameAndAvatar(@RequestParam("file") MultipartFile uploadFile, @RequestParam("pn") String mailAddress, @RequestParam("un") String username){
//        //TODO: 设置头像和用户名，调用两个函数分别设置
//        return userInfoService.setNameAndAvatar(mailAddress, username, uploadFile);
//    }

    @PutMapping("/setName")
    public Res<Boolean> setName(@RequestBody String info) {
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("username"))
            return userInfoService.setName((String) json.get("mailAddress"), (String) json.get("username"));
        return Res.Error(status.infoMiss);
    }

    @PostMapping("/setAvatar")
    public Res<Boolean> setAvatar(@RequestParam("file") MultipartFile uploadFile, @RequestParam("pn") String mailAddress){
        return userInfoService.setAvatar(mailAddress, uploadFile);
    }

//    @PutMapping("/setLongitudeAndLatitude")
//    public Res<Boolean> setLongitudeAndLatitude(String mailAddress, Double longitude, Double latitude){
//        return userInfoService.setLongitudeAndLatitude(mailAddress, longitude, latitude);
//    }

    @PutMapping("/setPoint")
    public Res<Boolean> setPoint(@RequestBody String info){
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("point"))
            return userInfoService.setPoint((String) json.get("mailAddress"), (Double) json.get("point"));
        return Res.Error(status.infoMiss);
    }


    @PutMapping("/setRegistered")
    public Res<Boolean> setRegistered(@RequestBody String info){
        Map json= JSONObject.parseObject(info, Map.class);;
        if(json.containsKey("mailAddress")&&json.containsKey("registered"))
            return userInfoService.setRegistered((String) json.get("mailAddress"), (Boolean) json.get("registered"));
        return Res.Error(status.infoMiss);
    }

    /**
     * 获取用户信息
     * @param mailAddress 手机号码
     * @return 用户信息实体类对象
     */
    @GetMapping("/getInfo")
    public Res<User> getInfo(@RequestParam("mailAddress") String mailAddress){
        return userInfoService.getInfo(mailAddress);
    }

}
