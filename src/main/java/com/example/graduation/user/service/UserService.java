package com.example.graduation.user.service;

import com.example.graduation.server.mailService.MailService;
import com.example.graduation.user.model.User;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
/**
 * @desc 用户登录相关
 */
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    public Res<User> loginWithPassword(String mailAddress, String password) {
        try{
            Optional<User> u=userRepository.findById(mailAddress);
            if(u.isEmpty()) return Res.Error(status.userNotExist);
            if(password.equals(u.get().getPassword())){
                return Res.Success(u.get());
            }else{
                return Res.Error(status.passwordError);
            }
        }catch (Exception e) {
            logger.error("login with password"+e.getMessage());
            return Res.Error(status.netError);
        }
    }

    //TODO: 验证码最后写
    public Res<User> loginWithCaptcha(String mailAddress, String captcha) {
        Res<Boolean> res=mailService.verify(mailAddress, captcha);
        if(res.isSuccess()){
            Optional<User> u=userRepository.findById(mailAddress);
            if(u.isEmpty()) {
                User t=new User();
                return Res.Error(status.successButUserNotExist);
            }
            return Res.Success(u.get());
        }
        return Res.Success(new User());
    }

    public Res<User> loginWithCaptchaByUserExist(String mailAddress, String captcha) {
        return Res.Success(new User());
    }

    public Res<Boolean> loginWithCaptchaByUserNotExist(String mailAddress, String captcha) {
        return Res.Success(true);
    }

    public Res<Boolean> sendCaptcha(String mailAddress) {
        return mailService.sendMailCode(mailAddress);
    }

}
