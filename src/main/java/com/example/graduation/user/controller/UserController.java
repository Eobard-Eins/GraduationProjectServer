package com.example.graduation.user.controller;

import com.example.graduation.user.model.User;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.Res;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userLogin")
/**
 * @desc 用户登录相关
 */
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    @GetMapping("/loginWithPassword")
    public Res<User> loginWithPassword(@RequestParam("mailAddress") String mailAddress, @RequestParam("password") String password){
        return userService.loginWithPassword(mailAddress, password);
    }

    @GetMapping("/loginWithCaptcha")
    public Res<User> loginWithCaptcha(@RequestParam("mailAddress") String mailAddress, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptcha(mailAddress, captcha);
    }

    @GetMapping("/loginWithCaptchaByUserExist")
    public Res<User> loginWithCaptchaByUserExist(@RequestParam("mailAddress") String mailAddress, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptchaByUserExist(mailAddress, captcha);
    }

    @PostMapping("/loginWithCaptchaByUserNotExist")
    public Res<Boolean> loginWithCaptchaByUserNotExist(@RequestParam("mailAddress") String mailAddress, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptchaByUserNotExist(mailAddress, captcha);
    }

    @GetMapping("/sendCaptcha")
    public Res<Boolean> sendCaptcha(@RequestParam("mailAddress") String mailAddress){
        return userService.sendCaptcha(mailAddress);
    }

}
