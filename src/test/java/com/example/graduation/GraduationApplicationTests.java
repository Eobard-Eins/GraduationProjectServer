package com.example.graduation;

import com.example.graduation.task.model.TaskImage;
import com.example.graduation.task.model.TaskInfo;
import com.example.graduation.task.model.TaskStatus;
import com.example.graduation.task.repository.TaskImgRepository;
import com.example.graduation.task.repository.TaskInfoRepository;
import com.example.graduation.task.repository.TaskStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class GraduationApplicationTests {

    @Autowired
    private TaskStatusRepository repository;
    @Test
    void contextLoads() {
        System.out.println(repository.save(new TaskStatus(1,"1312366323@qq.com",1001)).toString());
    }

}
