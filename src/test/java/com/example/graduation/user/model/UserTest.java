package com.example.graduation.user.model;

import com.example.graduation.server.OSS.OssConfig;
import com.example.graduation.server.mailService.MailService;
import com.example.graduation.server.mailService.MailServiceConfig;
import com.example.graduation.project.repository.UserRepository;
import com.example.graduation.project.service.UserInfoService;
import com.example.graduation.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private MailServiceConfig mailServiceConfig;
    @Autowired
    private MailService mailService;
    @Test
    void test() {
        //userService.sendCaptcha("1312366323@qq.com");
        System.out.println(mailService.verify("1312366323@qq.com","349584"));
    }

}