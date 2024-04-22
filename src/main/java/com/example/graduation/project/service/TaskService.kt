package com.example.graduation.project.service

import com.alibaba.fastjson.JSON
import com.example.graduation.project.model.*
import com.example.graduation.server.OSS.FileStatus
import com.example.graduation.server.OSS.FileUploadResult
import com.example.graduation.server.OSS.OssService
import com.example.graduation.server.PyServer.PyServer
import com.example.graduation.project.repository.*
import com.example.graduation.utils.Res
import com.example.graduation.utils.status
import jakarta.transaction.Transactional
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@Transactional
class TaskService @Autowired constructor(
        private val userRepository: UserRepository,
        private val pyServer: PyServer,
        private val taskInfoRepository: TaskInfoRepository,
        private val taskImgRepository: TaskImgRepository,
        private val ossService: OssService,
        private val taskTagRepository: TaskTagRepository,
        private val taskStatusRepository: TaskStatusRepository,
        private val historyRepository: HistoryRepository,
        private val taskRequestRepository: TaskRequestRepository
){



    suspend fun addTask(
            user:String,
            images: List<MultipartFile>,
            title: String,
            lat: Double,
            lon: Double,
            content: String,
            addressName:String,
            address:String,
            onLine:Boolean,
            tags: List<String>,
            time: Long,
            point:Double
    ): Res<Boolean> = coroutineScope{
        try {
            val u=userRepository.findById(user)
            if(u.isEmpty) throw Exception("user not found")
            if(u.get().point<point) throw Exception("user point not enough")
            u.get().point-=point
            userRepository.save(u.get())

            val tk: TaskInfo = taskInfoRepository.save(TaskInfo(title,content,addressName,address,onLine,point, Date(time)))
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
                    if(imgUrls.size != images.size) throw Exception("Failed to upload image")
                    imgUrls.forEach { imgUrl ->
                        taskImgRepository.save(TaskImage(id, imgUrl))
                    }
                    Res.Success(true)
                } catch (e: Exception) {
                    LoggerFactory.getLogger(TaskService::class.java).error("upload img server error")
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
                    LoggerFactory.getLogger(TaskService::class.java).error("upload tag server error")
                    Res.Error(status.taskAddError)
                }
            }
            // 上传发布数据库
            val addPublic = async {
                try {
                    taskStatusRepository.save(TaskStatus(id, user, status.taskPublic))
                    Res.Success(true)
                } catch (e: Exception) {
                    LoggerFactory.getLogger(TaskService::class.java).error("upload public info server error")
                    Res.Error(status.taskAddError)
                }
            }
            // 上传至Python服务器
            val pyRes = async {
                try {
                    val res = pyServer.addTask(user,id, title, lat, lon, onLine, tags)
                    if (res.isError || res.data == false) {
                        throw Exception("Error while uploading to Python server")
                    }
                    Res.Success(true)
                } catch (e: Exception) {
                    Res.Error(status.taskAddError)
                }
            }

            val results = awaitAll(uploadImg, addTag, addPublic, pyRes)

            // 检查是否有错误
            if (results.any { it.isError }) {
                // 如果有错误，回滚并记录日志
                taskInfoRepository.deleteById(id)
                throw Exception("Task addition failed due to errors in sub-tasks")
            }

            Res.Success(true)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("add task error " + e.message)
            if(e.message=="user point not enough") return@coroutineScope Res.Error(status.userPointNotEnough)
            return@coroutineScope Res.Error(status.taskAddError)
        }
    }

    fun like(
            id:Long,
            email:String
    ): Res<Boolean> {
        try {
            val task=taskInfoRepository.findById(id)
            if(task.isEmpty) throw Exception("Task not found")
            task.get().hot+=2
            taskInfoRepository.save(task.get())

            val ht=historyRepository.findById(History.HistoryPK(email,id))
            if(ht.isEmpty) throw Exception("History not found")
            ht.get().like=true
            ht.get().dislike=false
            historyRepository.save(ht.get())

            pyServer.updatePrefer(email, id.toInt(),status.like)
            return Res.Success(true)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("like error " + e.message)
            return Res.Error(status.likeError)
        }
    }

    fun dislike(
            id:Long,
            email:String
    ): Res<Boolean> {
        try {
            val task=taskInfoRepository.findById(id)
            if(task.isEmpty) throw Exception("Task not found")
            task.get().hot-=2
            taskInfoRepository.save(task.get())

            val ht=historyRepository.findById(History.HistoryPK(email,id))
            if(ht.isEmpty) throw Exception("History not found")
            ht.get().dislike=true
            ht.get().like=false
            historyRepository.save(ht.get())

            pyServer.updatePrefer(email, id.toInt(),status.dislike)
            return Res.Success(true)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("dislike error " + e.message)
            return Res.Error(status.dislikeError)
        }
    }

    fun getTask(//获取详细信息
            id:Long,
            email:String
    ):Res<Map<String,String>>{
        try {
            val mp=mutableMapOf<String, String>()
            val task=taskInfoRepository.findById(id)
            if(task.isEmpty) throw Exception("Task not found")
            mp["title"]=task.get().title.toString()
            mp["content"]=task.get().content.toString()
            mp["time"]=task.get().lastTime.toString()
            mp["address"]=task.get().address.toString()
            mp["address_name"]=task.get().addressName.toString()
            mp["tags"]= JSON.toJSONString(taskTagRepository.getTags(id))
            mp["imgs"]= JSON.toJSONString(taskImgRepository.getImgs(id))
            mp["onLine"]=task.get().online.toString()

            //热度+1
            task.get().hot+=1
            taskInfoRepository.save(task.get())

            val a=taskStatusRepository.findById(id)
            if(a.isEmpty) throw Exception("task not found")
            val ur=userRepository.findById(a.get().publishUserId)
            if(ur.isEmpty) throw Exception("User not found")
            mp["username"]=ur.get().username
            mp["avatar"]=ur.get().avatar
            mp["account"]=ur.get().email

            //获取点赞记录
            val history = historyRepository.findById(History.HistoryPK(email,id))
            if(history.isEmpty){
                mp["like"]=false.toString()
                mp["dislike"]=false.toString()
            }else{
                mp["like"]=history.get().like.toString()
                mp["dislike"]=history.get().dislike.toString()
            }
            //获取接取记录
            if(a.get().status==status.taskPublic){
                val request=taskRequestRepository.findById(TaskRequest.TaskRequestPK(id,email))
                if(request.isPresent) mp["request"]=true.toString()
                else mp["request"]=false.toString()
            }else{
                mp["request"]=false.toString()
            }

            //添加记录
            historyRepository.save(History(id,email,Date(),mp["like"].toBoolean(),mp["dislike"].toBoolean()))

            pyServer.updatePrefer(email, id.toInt(),status.click)
            return Res.Success(mp)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("get task error " + e.message)
            return Res.Error(status.taskGetError)
        }
    }
    fun getTaskList(//获取推荐预览信息
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
                    mp["point"]=task.get().point.toString()
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

    fun getHistory(
            user:String,
            num:Int
    ): Res<List<Map<String,String>>>{
        try {
            val tasks=historyRepository.findByEmail(user,num).map { it.PKid.taskId }
            val res=getTaskInfosById(tasks)
            if(res.isError) throw Exception("get history error")
            return res
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("get history list error " + e.message)
            return Res.Error(status.historyGetError)
        }
    }

    //申请委托
    fun requestTask(
            user:String,
            taskId:Long
    ): Res<Boolean>{
        try {
            val task=taskRequestRepository.findById(TaskRequest.TaskRequestPK(taskId,user))
            if(task.isPresent) throw Exception("task request exist")

            val t=taskInfoRepository.findById(taskId)
            if(t.isEmpty) throw Exception("task not found")
            if(t.get().lastTime<Date()) throw Exception("task time out")

            taskRequestRepository.save(TaskRequest(taskId,user))
            return Res.Success(true)
        }catch (e:Exception){
            LoggerFactory.getLogger(TaskService::class.java).error("request task error " + e.message)
            if(e.message=="task request exist") return Res.Error(status.taskRequestExist)
            return Res.Error(status.taskGetError)
        }
    }


    fun getAllRequestWithTask(
            id:Long
    ):Res<List<Map<String,String>>> =try{
        val userIds = taskRequestRepository.getTaskRequestsByTaskId(id)
        val res=mutableListOf<MutableMap<String,String>>()
        for(uid in userIds){
            val u=userRepository.findById(uid)
            if(u.isEmpty) throw Exception("user not found")
            val mp=mutableMapOf<String, String>()
            mp["uid"]=u.get().email.toString()
            mp["username"]=u.get().username.toString()
            mp["avatar"]=u.get().avatar.toString()
            res.addLast(mp)
        }
        Res.Success(res)
    }catch (e:Exception){
        LoggerFactory.getLogger(TaskService::class.java).error("request user get error " + e.message)
        Res.Error(status.userGetError)
    }

    //接受委托申请
    fun accessTask(
            requestUserId:String,
            taskId:Long
    ): Res<Boolean> =try{
        val ts = taskStatusRepository.findById(taskId)
        val t=taskInfoRepository.findById(taskId)
        if(ts.isEmpty||t.isEmpty) throw Exception("task not found")

        //删除记录
        taskRequestRepository.deleteTaskRequestsByTaskIdIs(taskId)

        //使其在推荐引擎中失效，不再推荐给他人
        pyServer.disableTask(taskId)

        ts.get().accessUserId=requestUserId
        ts.get().status=status.taskBeAccessed
        taskStatusRepository.save(ts.get())
        Res.Success(true)
    }catch (e:Exception){
        LoggerFactory.getLogger(TaskService::class.java).error("change status error " + e.message)
        Res.Error(status.taskStatusChangeError)
    }

    fun changeStatus(
            task:Long,
            newStatus:Int
    ):Res<Boolean> =try{
        val ts = taskStatusRepository.findById(task)
        if(ts.isEmpty) throw Exception("task not found")
        if(ts.get().status==status.taskTimeout) throw Exception("status time out")
        if(newStatus==status.taskDone){
            val taskInfo = taskInfoRepository.findById(task)
            if(taskInfo.isEmpty) throw Exception("task info not found")
            val user = userRepository.findById(ts.get().accessUserId)
            if(user.isEmpty) throw Exception("user not found")
            user.get().point+=taskInfo.get().point
            userRepository.save(user.get())
        }
        ts.get().status=newStatus
        taskStatusRepository.save(ts.get())
        Res.Success(true)
    }catch (e:Exception){
        LoggerFactory.getLogger(TaskService::class.java).error("change status error " + e.message)
        Res.Error(status.taskStatusChangeError)
    }

    fun getTasksByPublishUserIdAndStatus(
            user:String,
            taskStatus:Int
    ):Res<List<Map<String,String>>> =try {
        val ids = if(taskStatus!=status.getAll)
                taskStatusRepository.getTaskStatusByPublishUserIdIsAndStatusIs(user,taskStatus).map{ it.taskId }
            else taskStatusRepository.getTaskStatusByPublishUserIdIs(user).map { it.taskId }
        val res=getTaskInfosById(ids)
        if(res.isError) throw Exception("get tasks error")
        res
    }catch(e:Exception){
        LoggerFactory.getLogger(TaskService::class.java).error("get task error " + e.message)
        Res.Error(com.example.graduation.utils.status.taskGetError)
    }
    fun getTasksByAccessUserIdAndStatus(
            user:String,
            taskStatus:Int
    ):Res<List<Map<String,String>>> =try {
        val ids = if(taskStatus!=status.getAll)
                taskStatusRepository.getTaskStatusByAccessUserIdIsAndStatusIs(user,taskStatus).map{ it.taskId }
            else taskStatusRepository.getTaskStatusByAccessUserIdIs(user).map { it.taskId }
        val res=getTaskInfosById(ids)
        if(res.isError) throw Exception("get tasks error")
        res
    }catch(e:Exception){
        LoggerFactory.getLogger(TaskService::class.java).error("get task error " + e.message)
        Res.Error(com.example.graduation.utils.status.taskGetError)
    }
    

    private fun getTaskInfosById(
            tasks:List<Long>
    ):Res<List<Map<String,String>>> =try{
        val res = mutableListOf<MutableMap<String,String>>()
        for(id in tasks){
            val task=taskInfoRepository.findById(id)
            if(task.isEmpty) throw Exception("task not found")
            val mp=mutableMapOf<String, String>()
            mp["id"]=id.toString()
            mp["title"]=task.get().title.toString()
            mp["time"]=task.get().lastTime.toString()
            //mp["address"]=task.get().address.toString()
            mp["address_name"]=task.get().addressName.toString()
            //mp["tags"]= JSON.toJSONString(taskTagRepository.getTags(id))
            mp["point"]=task.get().point.toString()
            //mp["hot"]= task.get().hot.toString()
            //mp["read_time"]=historyRepository.findById(History.HistoryPK(user,id)).get().time.toString()
            res.addLast(mp)
        }
        Res.Success(res)
    }catch (e:Exception){
        Res.Error(status.taskGetError)
    }

}