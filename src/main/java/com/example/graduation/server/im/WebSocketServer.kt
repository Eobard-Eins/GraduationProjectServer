package com.example.graduation.server.im

import com.alibaba.fastjson.JSONObject
import com.example.graduation.GraduationApplication
import com.example.graduation.project.service.ChatService
import com.example.graduation.utils.status
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import jakarta.websocket.*
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@ServerEndpoint("/api/imserver/{userId}")
@Component
class WebSocketServer{

    private var session: Session? = null
    private var userId: String? = null
    @OnOpen
    fun onOpen(session: Session?, @PathParam("userId") userId: String) {
        this.session = session
        this.userId = userId
        if (webSocketMap.containsKey(userId)) {
            //包含此id说明此时其他地方开启了一个webSocket通道，直接kick下线重新连接
            webSocketMap.remove(userId)
            webSocketMap[userId] = this
        } else {
            webSocketMap[userId] = this
            addOnlineCount()
        }

        log.info("用户连接:$userId,当前在线人数为:$onlineCount")
    }

    @OnClose
    fun onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId)
            subOnlineCount()
        }
        log.info("用户退出:$userId,当前在线人数为:$onlineCount")
    }

    @OnMessage
    fun onMessage(message: String, session: Session?) {
        try {
            log.info("收到消息:$message")
            //解析为socketMsg对象
            val json = JSONObject.parseObject<Map<*, *>>(message, MutableMap::class.java)
            val socketMsg: SocketMsg =SocketMsg(
                    sender = json["sender"] as String,
                    receiver = json["receiver"] as String,
                    time = json["time"] as Long,
                    status = json["status"] as Int,
                    msg = json["msg"] as String
            )
            val id:Int = json["id"] as Int
            //log.info("收到消息:${socketMsg.toString()}")
            if(socketMsg.status==status.newMessage){
                val fromSession: Session? = webSocketMap[socketMsg.sender]?.session
                val toSession: Session? = webSocketMap[socketMsg.receiver]?.session
                if (fromSession == null) throw Exception("发送者不存在")
                log.info("返回消息: ${socketMsg.toJSON(id)}")
                if (toSession != null) {//对方在线
                    //发送消息
                    log.info("发送消息：${socketMsg.sender}->${socketMsg.receiver}：${socketMsg.msg}")
                    socketMsg.status=status.send
                    fromSession.asyncRemote.sendText(socketMsg.toJSON(id))

                    socketMsg.status=status.receive
                    toSession.asyncRemote.sendText(socketMsg.toJSON(id))
                } else {
                    log.error("接收方不在线")
                    socketMsg.status=status.unSend
                    fromSession.asyncRemote.sendText(socketMsg.toJSON(id))
                }
            }


        } catch (e: Exception) {
            log.error(e.message)
        }
    }



    @OnError
    fun onError(session: Session?, error: Throwable) {
        log.info("用户错误:$userId 原因: ${error.message}")
    }


    //这是发送消息用到的函数
    @Throws(IOException::class)
    fun sendMessage(message: String?) {
        session!!.basicRemote.sendText(message)
    }

    //静态变量和方法
    companion object {
        //统计在线人数
        private var onlineCount: Int = 0
        private val log: Logger = LoggerFactory.getLogger(WebSocketServer::class.java)

        //用于存放每个用户对应的webSocket对象
        val webSocketMap = ConcurrentHashMap<String, WebSocketServer>()

//        @Autowired
//        private lateinit var chatService: ChatService
        //服务端主动推送消息的对外开放的方法
        @Throws(IOException::class)
        fun sendInfo(message: String, status:Int, @PathParam("userId") userId: String) =try {
            val socketMsg = SocketMsg(
                    sender = "admin",
                    receiver = userId,
                    status = status,
                    time = Date().toInstant().toEpochMilli(),
                    msg = message
            )
            if (webSocketMap.containsKey(userId)) webSocketMap[userId]?.sendMessage(socketMsg.toJSON(0))
            else log.info("用户$userId：不在线")
            //chatService.saveChat(socketMsg)
        }catch (e: Exception){
            log.error(e.message)
        }

        //在线统计
        @Synchronized
        fun addOnlineCount() {
            onlineCount++

        }

        //离线统计
        @Synchronized
        fun subOnlineCount() {
            onlineCount--
        }
    }
}
