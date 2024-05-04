package com.example.graduation.project.controller

import com.example.graduation.project.model.Chat
import com.example.graduation.project.service.ChatService
import com.example.graduation.project.service.TaskService
import com.example.graduation.server.im.SocketMsg
import com.example.graduation.utils.Res
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat")
class ChatController @Autowired constructor(private val chatService: ChatService){

    @GetMapping("/getHistory")
    fun getHistory(
            @RequestParam("u1") user1: String,
            @RequestParam("u2") user2: String,
            @RequestParam("page",defaultValue = "0") page: Int,
            @RequestParam("size",defaultValue = "10") size: Int
    ): Res<Page<Chat>> {
        val pageable:Pageable = PageRequest.of(page, size)
        return chatService.getHistory(user1, user2, pageable)
    }


    @GetMapping("/getChatUser")
    fun getChatUser(
            @RequestParam("u") user: String,
            @RequestParam("page",defaultValue = "0") page: Int,
            @RequestParam("size",defaultValue = "20") size: Int
    ): Res<Page<Map<String, String>>> {
        val pageable:Pageable = PageRequest.of(page, size)
        return chatService.getChatUser(user,pageable)
    }

    @PutMapping("/setRead")
    fun setRead(
            @RequestParam("u1") u1:String,
            @RequestParam("u2") u2:String
    ): Res<Boolean> = chatService.setRead(u1, u2)

    @PostMapping("/saveChat")
    fun saveChat(
            @RequestBody info:String
    ): Res<Chat> {
        val socketMsg=SocketMsg.decode(info)
        if(socketMsg.isError) return Res.Error("info miss")
        return chatService.saveChat(socketMsg.data)
    }
}