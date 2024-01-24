package com.example.graduation.user.model;

import com.example.graduation.user.repository.UserRepository;
import com.example.graduation.user.service.UserService;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Test
    void test() {
//        User user = User.builder().phone("12345678912").registered(false).point(0.0).build();
//        userRepository.save(user);
        Optional<User> u=userRepository.findById("12345678912");
        System.out.println(u);
        if(u.isEmpty()) System.out.println(1);
        String password="123123";
        if(password.equals(u.get().getPassword())){
            System.out.println(2);
        }else{
            System.out.println(3);
        }

    }

}