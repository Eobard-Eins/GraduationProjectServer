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
    public Res<User> loginWithPassword(@RequestParam("phone") String phone, @RequestParam("password") String password){
        return userService.loginWithPassword(phone, password);
    }

    @GetMapping("/loginWithCaptcha")
    public Res<User> loginWithCaptcha(@RequestParam("phone") String phone, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptcha(phone, captcha);
    }

    @GetMapping("/loginWithCaptchaByUserExist")
    public Res<User> loginWithCaptchaByUserExist(@RequestParam("phone") String phone, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptchaByUserExist(phone, captcha);
    }

    @PostMapping("/loginWithCaptchaByUserNotExist")
    public Res<Boolean> loginWithCaptchaByUserNotExist(@RequestParam("phone") String phone, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptchaByUserNotExist(phone, captcha);
    }

    @GetMapping("/sendCaptcha")
    public Res<Boolean> sendCaptcha(@RequestParam("phone") String phone){
        return userService.sendCaptcha(phone);
    }

}
