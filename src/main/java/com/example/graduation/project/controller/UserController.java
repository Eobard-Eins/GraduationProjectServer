package com.example.graduation.project.controller;

import com.example.graduation.project.model.User;
import com.example.graduation.project.service.UserService;
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

    @GetMapping("/test")
    public String test(){
        return "OK";
    }

    /**
     * @desc 用户登录
     * @param mailAddress
     * @param password
     * @return netError/passwordError/userNotExist
     */
    @GetMapping("/loginWithPassword")
    public Res<User> loginWithPassword(@RequestParam("mailAddress") String mailAddress, @RequestParam("password") String password){
        return userService.loginWithPassword(mailAddress, password);
    }

    /**
     * @desc 验证码登录
     * @param mailAddress
     * @param captcha
     * @return captchaExpiration/captchaError/successButUserNotExist/netError/pyServerError
     */
    @GetMapping("/loginWithCaptcha")
    public Res<User> loginWithCaptcha(@RequestParam("mailAddress") String mailAddress, @RequestParam("captcha") String captcha){
        return userService.loginWithCaptcha(mailAddress, captcha);
    }

    /**
     * @desc 发送验证码
     * @param mailAddress
     * @return netError/mailServiceError
     */
    @GetMapping("/sendCaptcha")
    public Res<Boolean> sendCaptcha(@RequestParam("mailAddress") String mailAddress){
        return userService.sendCaptcha(mailAddress);
    }

}
