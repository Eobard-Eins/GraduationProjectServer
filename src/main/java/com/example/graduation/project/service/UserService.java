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
            if(u.isEmpty()) throw new Exception("用户不存在:user not exist");
            if(password.equals(u.get().getPassword())){
                return Res.Success(u.get());
            }else{
                throw new Exception("密码不正确:password is wrong");
            }
        }catch (Exception e) {
            logger.error("login with password"+e.getMessage());
            return Res.Error(e.getMessage());
        }
    }

    public Res<User> loginWithCaptcha(String email, String captcha) {
       try{
           Res<Boolean> res=mailService.verify(email, captcha);
           if(res.isSuccess()){//验证通过
               Optional<User> u=userRepository.findById(email);
               if(u.isEmpty()||u.get().getPassword()==null) {//用户不存在或未设置密码
                   Res<Boolean> pyres=pyServer.addUser(email);
                   if(pyres.isError()) throw new Exception("验证码正确，引擎出错:"+pyres.getMessage());//pyServer添加用户失败
                   User t=new User(email);
                   userRepository.save(t);
                   return Res.SuccessBut(status.successButUserNotExist);
               }
               return Res.Success(u.get());
           }
           throw new Exception(res.getMessage());
       }catch (Exception e){
           logger.error("login with captcha"+e.getMessage());
           return Res.Error(e.getMessage());
       }
    }


    public Res<Boolean> sendCaptcha(String email) {
        try{
            return mailService.sendMailCode(email);
        }catch (Exception e){
            logger.error("send captcha"+e.getMessage());
            return Res.Error("发送验证码时出错:"+e.getMessage());
        }
    }

}
