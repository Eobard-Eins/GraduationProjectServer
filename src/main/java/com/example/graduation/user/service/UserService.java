package com.example.graduation.user.service;

import com.example.graduation.user.model.User;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
/**
 * @desc 用户登录相关
 */
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public Res<User> loginWithPassword(String phone, String password) {
        try{
            Optional<User> u=userRepository.findById(phone);
            if(u.isEmpty()) return Res.Error(status.userNotExist);
            if(password.equals(u.get().getPassword())){
                return Res.Sucess(u.get());
            }else{
                return Res.Error(status.passwordError);
            }
        }catch (Exception e) {
            return Res.Error(status.netError);
        }
    }

    //TODO: 验证码最后写
    public Res<User> loginWithCaptcha(String phone, String captcha) {
        return Res.Error(status.captchaError);
    }

    public Res<User> loginWithCaptchaByUserExist(String phone, String captcha) {
        return Res.Error(status.captchaError);
    }

    public Res<Boolean> loginWithCaptchaByUserNotExist(String phone, String captcha) {
        return Res.Error(status.captchaError);
    }

    public Res<Boolean> sendCaptcha(String phone) {
        return Res.Error(status.captchaError);
    }


}
