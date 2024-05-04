package com.example.graduation.server.PyServer;

import com.example.graduation.project.repository.TaskRequestRepository;
import com.example.graduation.utils.Res;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PyServerTest {
    @Autowired
    private PyServer pyServer;
    @Autowired
    private TaskRequestRepository taskRequestRepository;
    @Test
    void addTask() {
        System.out.println(taskRequestRepository.deleteTaskRequestByPKidId(47));
    }

    @Test
    void disableTask() {
    }

    @Test
    void getTaskList() {
        //System.out.println("/api/addUser?"+"user="+"user"+"&search="+""+"&k="+20+"&longitude="+1.0+"&latitude="+1.0+"&s="+20.3);
        Res res=pyServer.getTaskList("test1@ads.con","",10,1.0,1.0,1.0);
        System.out.println(res.toString());
    }

    @Test
    void addUser() {
        Res res=pyServer.addUser("test1@ads.con");
        System.out.println(res.toString());
    }

    @Test
    void updatePrefer() {
        Res res=pyServer.updatePrefer("test1@ads.con",2,1);
        System.out.println(res.toString());
    }
}