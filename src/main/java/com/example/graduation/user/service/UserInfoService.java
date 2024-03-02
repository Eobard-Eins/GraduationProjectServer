package com.example.graduation.user.service;

import com.example.graduation.server.OSS.FileStatus;
import com.example.graduation.server.OSS.FileUploadResult;
import com.example.graduation.server.OSS.OssService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @desc 用户信息相关
 */
@Service
@Transactional
public class UserInfoService {

    @Autowired
    private OssService ossService;
    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    public Res<Boolean> setPassword(String mailAddress, String password) {
        try{
            int res=userRepository.updatePassword(password, mailAddress);
            //更新条数不为1时出现密码设置错误，但是大于1的情况不存在（因为mailAddress是主键），不存在影响其他用户的情况
            if(res!=1) return Res.Error(status.setPasswordError);
            else return Res.Success(true);
        }catch (Exception e){
            logger.error("set password error"+e.getMessage());
            return Res.Error(status.netError);
        }
    }

//    public Res<Boolean> setNameAndAvatar(String mailAddress, String username, MultipartFile uploadFile) {
//        Res<Boolean> res1=setName(mailAddress, username);
//        if(res1.statusCode!=status.success) return res1;
//
//        Res<Boolean> res2=setAvatar(mailAddress, uploadFile);
//        if(res2.statusCode!=status.success) return res2;
//
//        return Res.Success(true);
//
//    }

    public Res<Boolean> setName(String mailAddress, String username) {
        try{
            int res=userRepository.updateUsername(username, mailAddress);
            if(res!=1) return Res.Error(status.setUsernameError);
            else return Res.Success(true);
        }catch (Exception e){
            logger.error("set name error"+e.getMessage());
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setAvatar(String mailAddress, MultipartFile uploadFile) {
        Optional<User> user=userRepository.findById(mailAddress);
        if(user.isEmpty()) {
            logger.error("user not exist");
            return Res.Error(status.userNotExist);
        }
        String s=user.get().getAvatar();

        FileUploadResult fres = ossService.upload(uploadFile);
        if(fres.getStatus()== FileStatus.error){
            logger.error("oss server error");
            return Res.Error(status.ossError);
        }else if(fres.getStatus()== FileStatus.done){
            //删除旧的
            if(s!=null) ossService.delete(s);
            String furl= fres.getName();
            //将url存入数据库
            try{
                int res=userRepository.updateAvatar(furl, mailAddress);
                if(res!=1) return Res.Error(status.setAvatarError);
                else return Res.Success(true);
            }catch (Exception e){
                logger.error("set avatar error"+e.getMessage());
                return Res.Error(status.netError);
            }

        }else{
            logger.error("oss server error");
            return Res.Error(status.ossError);
        }
    }

    public Res<Boolean> setLongitudeAndLatitude(String mailAddress, Double longitude, Double latitude) {
        try{
            int res=userRepository.updateLongitudeAndLongitude(longitude, latitude, mailAddress);
            if(res!=1) return Res.Error(status.setLongitudeAndLatitudeError);
            else return Res.Success(true);
        }catch (Exception e){
            logger.error("set longitude and latitude "+e.getMessage());
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setPoint(String mailAddress, Double point) {
        try{
            int res=userRepository.updatePoint(point, mailAddress);
            if(res!=1) return Res.Error(status.setPointError);
            else return Res.Success(true);
        }catch (Exception e){
            logger.error("set point error"+e.getMessage());
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setRegistered(String mailAddress, boolean registered) {
        try{
            int res=userRepository.updateRegistered(registered, mailAddress);
            if(res!=1) return Res.Error(status.setRegisteredError);
            else return Res.Success(true);
        }catch (Exception e){
            logger.error("set registered error "+e.getMessage());
            return Res.Error(status.netError);
        }
    }

    public Res<User> getInfo(String mailAddress) {
        try{
            Optional<User> user=userRepository.findById(mailAddress);
            if(user.isEmpty()) return Res.Error(status.userNotExist);
            else return Res.Success(user.get());
        }catch (Exception e){
            logger.error("get info error"+e.getMessage());
            return Res.Error(status.netError);
        }
    }
}
