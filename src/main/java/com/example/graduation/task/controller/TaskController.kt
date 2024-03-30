package com.example.graduation.task.controller

import com.example.graduation.task.service.TaskService
import com.example.graduation.utils.Res
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin("*")
@RestController
@RequestMapping("/api/task")
class TaskController @Autowired constructor(private val taskService: TaskService) {

    @PostMapping("/addTask")//taskAddError
    suspend fun addTask(
            @RequestParam("images") images: List<MultipartFile>,
            @RequestParam("title") title: String,
            @RequestParam("latitude") lat: Double,
            @RequestParam("longitude") lon: Double,
            @RequestParam("content") description: String,
            @RequestParam("address") address:String,
            @RequestParam("onLine") onLine:Boolean,
            @RequestParam("tags") tags: List<String>
    ): Res<Boolean> = taskService.addTask(images,title,lat,lon,description,address,onLine,tags)

    @PutMapping("/like")
    fun like(): Res<Boolean> = taskService.like()

    @PutMapping("/dislike")
    fun dislike(): Res<Boolean> = taskService.dislike()

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
    ): Res<Map<String,String>> = taskService.getTask(id)

    @PutMapping("/access")
    fun accessTask(): Res<Boolean> = taskService.accessTask()

    @PutMapping("/setMyPublic")
    fun setMyPublic(): Res<Boolean> = taskService.setMyPublic()

    @PutMapping("/setMyAccess")
    fun setMyAccess(): Res<Boolean> = taskService.setMyAccess()
}