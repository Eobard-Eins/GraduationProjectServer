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

) {
    private val logger= LoggerFactory.getLogger(TimeoutService::class.java)
    @Scheduled(fixedRate = 6000)
    fun checkTimeout() =try{
        //非过期且非完成
        val tasks=taskInfoRepository.findAllByLastTimeBeforeAndStatusIsNotAndStatusIsNot(Date(), status.taskTimeout, status.taskDone)
        if(tasks.isNotEmpty()) logger.info("定时任务 time: "+ LocalDateTime.now()+" num of tasks: "+tasks.size)
        for (task in tasks){
            task.status= status.taskTimeout
            taskInfoRepository.save(task)

            val res=pyServer.disableTask(task.id!!)
            if(res.isError) throw Exception("pyServer disableTask error: "+res.message)
        }
    }catch (e:Exception){
        logger.error("定时任务出错: "+e.message)
    }

}