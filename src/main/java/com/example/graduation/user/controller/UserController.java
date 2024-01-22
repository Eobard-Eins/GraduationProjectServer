package com.example.graduation.user.controller;

import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userLogin")
/**
 * 用户登录相关
 */
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/loginWithPassword")
    public result<Boolean> loginWithPassword(String phone, String password){
        return userService.loginWithPassword(phone, password);
    }

    @GetMapping("/loginWithCaptcha")
    public result<Boolean> loginWithCaptcha(String phone, String captcha){
        return userService.loginWithCaptcha(phone, captcha);
    }

    @GetMapping("/loginWithCaptchaByUserExist")
    public result<Boolean> loginWithCaptchaByUserExist(String phone, String captcha){
        return userService.loginWithCaptchaByUserExist(phone, captcha);
    }

    @GetMapping("/loginWithCaptchaByUserNotExist")
    public result<Boolean> loginWithCaptchaByUserNotExist(String phone, String captcha){
        return userService.loginWithCaptchaByUserNotExist(phone, captcha);
    }

    @GetMapping("/sendCaptcha")
    public result<Boolean> sendCaptcha(String phone){
        return userService.sendCaptcha(phone);
    }

}
