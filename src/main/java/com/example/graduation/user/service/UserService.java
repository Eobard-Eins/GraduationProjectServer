package com.example.graduation.user.service;

import com.example.graduation.user.model.User;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.utils.result;
import com.example.graduation.utils.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/**
 * @desc 用户登录相关
 */
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public result<Boolean> loginWithPassword(String phone, String password) {
        try{
            if(!userRepository.existsUserByPhone(phone)) return new result<>(status.userNotExist,false);
            User u=userRepository.getUserByPhone(phone);
            if(u.getPassword().equals(password)){
                return new result<>(true);
            }else{
                return new result<>(status.passwordError,false);
            }
        }catch (Exception e) {
            return new result<>(status.netError, false);
        }
    }

    public result<Boolean> loginWithCaptcha(String phone, String captcha) {
    }

    public result<Boolean> loginWithCaptchaByUserExist(String phone, String captcha) {
    }

    public result<Boolean> loginWithCaptchaByUserNotExist(String phone, String captcha) {
    }

    public result<Boolean> sendCaptcha(String phone) {
    }


}
