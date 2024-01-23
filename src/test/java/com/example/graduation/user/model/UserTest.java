package com.example.graduation.user.model;

import com.example.graduation.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void test() {
//        User user = User.builder().phone("12345678912").registered(false).point(0.0).build();
//        userRepository.save(user);
        Optional<User> res= userRepository.findById("12345678912");
        if(res.isPresent()){
            System.out.println(res.get());
        }else{
            System.out.println("error");
        }

    }

}