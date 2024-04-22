package com.example.graduation.project.service;

import com.example.graduation.server.PyServer.PyServer;
import com.example.graduation.server.mailService.MailService;
import com.example.graduation.project.model.User;
import com.example.graduation.project.repository.UserRepository;
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
    @Autowired
    private PyServer pyServer;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    public Res<User> loginWithPassword(String email, String password) {
        try{
            Optional<User> u=userRepository.findById(email);
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

    public Res<User> loginWithCaptcha(String email, String captcha) {
       try{
           Res<Boolean> res=mailService.verify(email, captcha);
           if(res.isSuccess()){//验证通过
               Optional<User> u=userRepository.findById(email);
               if(u.isEmpty()||u.get().getPassword()==null) {//用户不存在或未设置密码
                   Res<Boolean> pyres=pyServer.addUser(email);
                   if(pyres.isError()) return Res.Error(pyres.getStatusCode());//pyServer添加用户失败
                   User t=new User(email);
                   userRepository.save(t);
                   return Res.Error(status.successButUserNotExist);
               }
               return Res.Success(u.get());
           }
           return Res.Error(res.getStatusCode());
       }catch (Exception e){
           logger.error("login with captcha"+e.getMessage());
           return Res.Error(status.netError);
       }
    }


    public Res<Boolean> sendCaptcha(String email) {
        try{
            return mailService.sendMailCode(email);
        }catch (Exception e){
            logger.error("send captcha"+e.getMessage());
            return Res.Error(status.netError);
        }
    }

}
