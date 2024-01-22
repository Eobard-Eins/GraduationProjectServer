package com.example.graduation.user.service;

import com.example.graduation.utils.result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserInfoService {
    public result<Boolean> setPassword(String phone, String password) {
    }

    public result<Boolean> setNameAndAvatar(String phone, String username, MultipartFile uploadFile) {
    }
}
