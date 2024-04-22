package com.example.graduation;

import com.example.graduation.project.model.TaskImage;
import com.example.graduation.project.repository.TaskImgRepository;
import com.example.graduation.project.repository.TaskStatusRepository;
import com.example.graduation.project.service.TaskService;
import com.example.graduation.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class GraduationApplicationTests {

    @Autowired
    private TaskImgRepository taskImgRepository;
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;
    @Test
    void contextLoads() {
        Date date = new Date();
        System.out.println(date.toString());
        //System.out.println(repository.save(new TaskStatus(1,"1312366323@qq.com",1001)).toString());
    }

}
