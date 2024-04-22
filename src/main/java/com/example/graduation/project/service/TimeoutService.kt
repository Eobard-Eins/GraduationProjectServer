package com.example.graduation.project.service

import com.example.graduation.project.repository.*
import com.example.graduation.server.OSS.OssService
import com.example.graduation.server.PyServer.PyServer
import com.example.graduation.utils.status
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class TimeoutService@Autowired constructor(
        private val pyServer: PyServer,
        private val taskInfoRepository: TaskInfoRepository,
        private val taskStatusRepository: TaskStatusRepository,

) {
    private val logger= LoggerFactory.getLogger(TimeoutService::class.java)
    @Scheduled(fixedRate = 6000)
    fun checkTimeout() =try{
        val tasks=taskInfoRepository.findAllByLastTimeBefore(Date())
        for (task in tasks){
            if(task.id==null) throw Exception("task id is null")
            val taskStatus=taskStatusRepository.findById(task.id!!)
            if(taskStatus.isEmpty) throw Exception("task status is null")
            taskStatus.get().status= status.taskTimeout
            taskStatusRepository.save(taskStatus.get())

            pyServer.disableTask(task.id!!)
        }
    }catch (e:Exception){
        logger.error("定时任务出错: "+e.message)
    }

}