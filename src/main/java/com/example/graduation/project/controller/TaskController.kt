package com.example.graduation.project.controller

import com.alibaba.fastjson.JSONObject
import com.example.graduation.project.service.TaskService
import com.example.graduation.utils.Res
import com.example.graduation.utils.status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin("*")
@RestController
@RequestMapping("/api/task")
class TaskController @Autowired constructor(private val taskService: TaskService) {

    @PostMapping("/addTask")//taskAddError
    suspend fun addTask(
            @RequestParam("user") user: String,
            @RequestParam("images", required = false) images: List<MultipartFile>,
            @RequestParam("title") title: String,
            @RequestParam("latitude") lat: Double,
            @RequestParam("longitude") lon: Double,
            @RequestParam("content") description: String,
            @RequestParam("addressName") addressName:String,
            @RequestParam("address") address:String,
            @RequestParam("onLine") onLine:Boolean,
            @RequestParam("tags", required = false) tags: List<String>,
            @RequestParam("time") time: Long,
            @RequestParam("point") point: Double
    ): Res<Boolean> = taskService.addTask(user,images?:listOf(),title,lat,lon,description,addressName,address,onLine,tags?:listOf(),time,point)


    @PutMapping("/like")
    fun like(
            @RequestBody info:String
    ): Res<Boolean> {
        val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
        if(json.containsKey("id") && json.containsKey("user"))
            return taskService.like(json["id"] as Long, json["user"] as String)
        return Res.Error(status.infoMiss)
    }

    @PutMapping("/dislike")
    fun dislike(
            @RequestBody info:String
    ): Res<Boolean> {
        val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
        if(json.containsKey("id") && json.containsKey("user"))
            return taskService.dislike(json["id"] as Long, json["user"] as String)
        return Res.Error(status.infoMiss)
    }

    @GetMapping("/getList")//taskGetError/userNotExist
    fun getTaskList(
            @RequestParam("user") user: String,
            @RequestParam("num") num: Int,
            @RequestParam("search") search: String,
            @RequestParam("distance") distance: Double,
            @RequestParam("lat") lat: Double,
            @RequestParam("lon") lon: Double
    ): Res<List<Map<String,String>>> = taskService.getTaskList(user,num,search,distance,lat,lon)

    @GetMapping("/get")//taskGetError
    fun getTask(
            @RequestParam("id") id: Long,
            @RequestParam("user") user: String
    ): Res<Map<String,String>> = taskService.getTask(id,user)

    @GetMapping("/history")
    fun getHistory(
            @RequestParam("user") user: String,
            @RequestParam("num") num: Int
    ): Res<List<Map<String,String>>> = taskService.getHistory(user,num)

    @PostMapping("/requestTask")
    fun requestTask(
            @RequestBody info:String,
    ): Res<Boolean> {
        val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
        if(json.containsKey("id") && json.containsKey("user"))
            return taskService.requestTask(json["user"] as String,json["id"] as Long)
        return Res.Error(status.infoMiss)
    }
    @PutMapping("/access")
    fun accessTask(
            @RequestBody info:String,
    ): Res<Boolean> {
        val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
        if(json.containsKey("id") && json.containsKey("user"))
            return taskService.accessTask(json["user"] as String,json["id"] as Long)
        return Res.Error(status.infoMiss)
    }

    @GetMapping("/getTasksByPublicUser")
    fun getAllTaskByRequestUser(
            @RequestParam("user") user: String,
            @RequestParam("status") status: Int,
    ):Res<List<Map<String, String>>> = taskService.getTasksByPublishUserIdAndStatus(user,status)
    @GetMapping("/getTasksByAccessUser")
    fun getAllTaskByAccessUser(
            @RequestParam("user") user: String,
            @RequestParam("status") status: Int,
    ):Res<List<Map<String, String>>> = taskService.getTasksByAccessUserIdAndStatus(user,status)
    @GetMapping("/getRequestsWithTask")
    fun getAllRequestWithTask(
            @RequestParam("id") id: Long,
    ):Res<List<Map<String, String>>> = taskService.getAllRequestWithTask(id)
    @PutMapping("/setStatus")
    fun changeStatus(
            @RequestBody info:String,
    ): Res<Boolean> {
        val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
        if(json.containsKey("id") && json.containsKey("status"))
            return taskService.changeStatus(json["id"] as Long,json["status"] as Int)
        return Res.Error(status.infoMiss)
    }

}