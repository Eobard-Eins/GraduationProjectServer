package com.example.graduation.user.service;

import com.example.graduation.server.OSS.OssService;
import com.example.graduation.utils.result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @desc 用户信息相关
 */
@Service
public class UserInfoService {

    @Autowired
    private OssService ossService;
    public result<Boolean> setPassword(String phone, String password) {
    }

    public result<Boolean> setNameAndAvatar(String phone, String username, MultipartFile uploadFile) {
    }
}
