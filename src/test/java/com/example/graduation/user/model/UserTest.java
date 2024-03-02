package com.example.graduation.user.model;

import com.alibaba.fastjson.JSONObject;
import com.example.graduation.server.OSS.OssConfig;
import com.example.graduation.server.mailService.MailService;
import com.example.graduation.server.mailService.MailServiceConfig;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.user.service.UserInfoService;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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