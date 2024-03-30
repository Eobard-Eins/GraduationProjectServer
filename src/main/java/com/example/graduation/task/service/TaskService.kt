package com.example.graduation.task.service

import com.alibaba.fastjson.JSON
import com.example.graduation.server.OSS.FileStatus
import com.example.graduation.server.OSS.FileUploadResult
import com.example.graduation.server.OSS.OssService
import com.example.graduation.server.PyServer.PyServer
import com.example.graduation.task.model.TaskImage
import com.example.graduation.task.model.TaskInfo
import com.example.graduation.task.model.TaskTag
import com.example.graduation.task.repository.TaskImgRepository
import com.example.graduation.task.repository.TaskInfoRepository
import com.example.graduation.task.repository.TaskStatusRepository
import com.example.graduation.task.repository.TaskTagRepository
import com.example.graduation.user.repository.UserRepository
import com.example.graduation.utils.Res
import com.example.graduation.utils.status
import jakarta.transaction.Transactional
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@Transactional
open class TaskService @Autowired constructor(
        private val pyServer: PyServer,
        private val taskInfoRepository: TaskInfoRepository,
        private val taskImgRepository: TaskImgRepository,
        private val ossService: OssService,
        private val taskTagRepository: TaskTagRepository,
        private val userRepository: UserRepository,
        private val taskStatusRepository: TaskStatusRepository
){


    @Transactional
    open suspend fun addTask(
            images: List<MultipartFile>,
            title: String,
            lat: Double,
            lon: Double,
            content: String,
            address:String,
            onLine:Boolean,
            tags: List<String>
    ): Res<Boolean> = coroutineScope{
        try {
            val tk: TaskInfo = taskInfoRepository.save(TaskInfo(title,content,address,onLine, Date()))
            val id=tk.id!!
            // 上传图片至OSS
            val uploadImg = async {
                try {
                    val imgUrls = mutableListOf<String>()
                    images.forEach { i ->
                        val fres: FileUploadResult = ossService.upload(i)
                        if (fres.status == FileStatus.done) {
                            imgUrls.add(fres.name)
                        } else {
                            LoggerFactory.getLogger(TaskService::class.java).error("OSS server error")
                            throw Exception("Failed to upload image")
                        }
                    }
                    imgUrls.forEach { imgUrl ->
                        taskImgRepository.save(TaskImage(id, imgUrl))
                    }
                    Res.Success(true)
                } catch (e: Exception) {
                    Res.Error(status.taskAddError)
                }
            }
            // 上传tag至数据库
            val addTag = async {
                try {
                    tags.forEach { tag ->
                        taskTagRepository.save(TaskTag(id, tag))
                    }
                    Res.Success(true)
                } catch (e: Exception) {
                    Res.Error(status.taskAddError)
                }
            }
            // 上传至Python服务器
            val pyRes = async {
                try {
                    val res = pyServer.addTask(id, title, lat, lon, onLine, tags)
                    if (res.isError || res.data == false) {
                        throw Exception("Error while uploading to Python server")
                    }
                    Res.Success(true)
                } catch (e: Exception) {
                    Res.Error(status.taskAddError)
                }
            }

            val results = awaitAll(uploadImg, addTag, pyRes)

            // 检查是否有错误
            if (results.any { it.isError }) {
                // 如果有错误，回滚事务并记录日志
                throw Exception("Task addition failed due to errors in sub-tasks")
            }

            Res.Success(true)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("add task error" + e.message)
            return@coroutineScope Res.Error(status.taskAddError)
        }
    }

    fun like(): Res<Boolean> {
        return Res.Success(true)
    }

    fun dislike(): Res<Boolean> {
        return Res.Success(true)
    }

    fun getTask(
            id:Long
    ):Res<Map<String,String>>{
        try {
            val mp=mutableMapOf<String, String>()
            val task=taskInfoRepository.findById(id)
            if(task.isEmpty) return Res.Error(status.taskGetError)
            mp["title"]=task.get().title.toString()
            mp["content"]=task.get().content.toString()
            mp["time"]=task.get().lastTime.toString()
            mp["address"]=task.get().address.toString()
            mp["tags"]= JSON.toJSONString(taskTagRepository.getTags(id))
            mp["imgs"]= JSON.toJSONString(taskImgRepository.getImgs(id))
            mp["onLine"]=task.get().online.toString()

            val a=taskStatusRepository.findById(id)
            if(a.isEmpty) throw Exception("task not found")
            val ur=userRepository.findById(a.get().publishUserId)
            if(ur.isEmpty) throw Exception("User not found")
            mp["username"]=ur.get().username
            mp["avatar"]=ur.get().avatar

            return Res.Success(mp)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("get task error" + e.message)
            return Res.Error(status.taskGetError)
        }
    }
    fun getTaskList(
            user:String,
            num:Int,
            search:String,
            distance:Double,
            lat:Double,
            lon:Double
    ): Res<List<Map<String,String>>>{
        try{
            if(userRepository.findById(user).isEmpty) return Res.Error(status.userNotExist)

            val res=pyServer.getTaskList(user,search,num,lon,lat,distance)
            if(res.isError) return Res.Error(status.taskGetError)

            val resData=mutableListOf<MutableMap<String,String>>()
            for(item in res.data){
                val mp=mutableMapOf<String, String>()

                val id= item["taskId"] as Long
                mp["id"]=id.toString()
                mp["distance"] = item["distance"].toString()
                mp["online"] = item["onLine"].toString()
                val task = taskInfoRepository.findById(id)
                if(task.isPresent){
                    mp["title"]=task.get().title.toString()
                    mp["hot"]=task.get().hot.toString()
                    mp["time"]=task.get().lastTime.toString()
                    mp["tags"] = JSON.toJSONString(taskTagRepository.getTags(id))
                    val a=taskStatusRepository.findById(id)
                    if(a.isEmpty) throw Exception("task not found")
                    val ur=userRepository.findById(a.get().publishUserId)
                    if(ur.isEmpty) throw Exception("User not found")
                    mp["username"]=ur.get().username
                    mp["avatar"]=ur.get().avatar
                }
                else{
                    throw Exception("Task not found")
                }

                resData.addLast(mp)
            }
            return Res.Success(resData)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("get task list error " + e.message)
            return Res.Error(status.taskGetError)
        }
    }

    fun accessTask(): Res<Boolean> {
        return Res.Success(true)
    }

    fun setMyPublic(): Res<Boolean> {
        return Res.Success(true)
    }

    fun setMyAccess(): Res<Boolean> {
        return Res.Success(true)
    }

}