package com.example.graduation.user.model;

import com.alibaba.fastjson.JSONObject;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.user.service.UserInfoService;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
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
    @Test
    void test() {
//
        String s="{\"phone\":\"12345678912\",\"password\":\"654321\"}";
        Map map = JSONObject.parseObject(s, Map.class);
        System.out.println(userInfoService.setPassword("12345678912","123456").statusCode);
        //System.out.println(userRepository.updatePassword("123456","12345678912"));
    }

}