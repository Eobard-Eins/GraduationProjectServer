package com.example.graduation.user.controller;

import com.example.graduation.user.model.User;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userLogin")
/**
 * @desc 用户登录相关
 */
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/loginWithPassword")
    public Res<User> loginWithPassword(String phone, String password){
        return userService.loginWithPassword(phone, password);
    }

    @GetMapping("/loginWithCaptcha")
    public Res<User> loginWithCaptcha(String phone, String captcha){
        return userService.loginWithCaptcha(phone, captcha);
    }

    @GetMapping("/loginWithCaptchaByUserExist")
    public Res<User> loginWithCaptchaByUserExist(String phone, String captcha){
        return userService.loginWithCaptchaByUserExist(phone, captcha);
    }

    @PostMapping("/loginWithCaptchaByUserNotExist")
    public Res<Boolean> loginWithCaptchaByUserNotExist(String phone, String captcha){
        return userService.loginWithCaptchaByUserNotExist(phone, captcha);
    }

    @GetMapping("/sendCaptcha")
    public Res<Boolean> sendCaptcha(String phone){
        return userService.sendCaptcha(phone);
    }

}
