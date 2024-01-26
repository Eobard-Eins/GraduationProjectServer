package com.example.graduation.user.service;

import com.example.graduation.server.OSS.FileStatus;
import com.example.graduation.server.OSS.FileUploadResult;
import com.example.graduation.server.OSS.OssService;
import com.example.graduation.user.model.User;
import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import jakarta.transaction.Transactional;
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
    public Res<Boolean> setPassword(String phone, String password) {
        try{
            int res=userRepository.updatePassword(password, phone);
            //更新条数不为1时出现密码设置错误，但是大于1的情况不存在（因为phone是主键），不存在影响其他用户的情况
            if(res!=1) return Res.Error(status.setPasswordError);
            else return Res.Sucess(true);
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }

//    public Res<Boolean> setNameAndAvatar(String phone, String username, MultipartFile uploadFile) {
//        Res<Boolean> res1=setName(phone, username);
//        if(res1.statusCode!=status.success) return res1;
//
//        Res<Boolean> res2=setAvatar(phone, uploadFile);
//        if(res2.statusCode!=status.success) return res2;
//
//        return Res.Sucess(true);
//
//    }

    public Res<Boolean> setName(String phone, String username) {
        try{
            int res=userRepository.updateUsername(username, phone);
            if(res!=1) return Res.Error(status.setUsernameError);
            else return Res.Sucess(true);
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setAvatar(String phone, MultipartFile uploadFile) {
        FileUploadResult fres = ossService.upload(uploadFile);
        if(fres.getStatus()== FileStatus.error){
            return Res.Error(status.ossError);
        }else if(fres.getStatus()== FileStatus.done){
            String furl= fres.getName();
            //将url存入数据库
            try{
                int res=userRepository.updateAvatar(furl, phone);
                if(res!=-1) return Res.Error(status.setAvatarError);
                else return Res.Sucess(true);
            }catch (Exception e){
                System.out.println(e);
                return Res.Error(status.netError);
            }

        }else{
            return Res.Error(status.ossError);
        }
    }

    public Res<Boolean> setLongitudeAndLatitude(String phone, Double longitude, Double latitude) {
        try{
            int res=userRepository.updateLongitudeAndLongitude(longitude, latitude, phone);
            if(res!=1) return Res.Error(status.setLongitudeAndLatitudeError);
            else return Res.Sucess(true);
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setPoint(String phone, Double point) {
        try{
            int res=userRepository.updatePoint(point, phone);
            if(res!=1) return Res.Error(status.setPointError);
            else return Res.Sucess(true);
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }

    public Res<Boolean> setRegistered(String phone, boolean registered) {
        try{
            int res=userRepository.updateRegistered(registered, phone);
            if(res!=1) return Res.Error(status.setRegisteredError);
            else return Res.Sucess(true);
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }

    public Res<User> getInfo(String phone) {
        try{
            Optional<User> user=userRepository.findById(phone);
            if(user.isEmpty()) return Res.Error(status.userNotExist);
            else return Res.Sucess(user.get());
        }catch (Exception e){
            System.out.println(e);
            return Res.Error(status.netError);
        }
    }
}
