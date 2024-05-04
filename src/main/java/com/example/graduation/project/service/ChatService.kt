package com.example.graduation.project.service

import com.example.graduation.project.model.Chat
import com.example.graduation.project.model.Conversation
import com.example.graduation.project.repository.ChatRepository
import com.example.graduation.project.repository.ConversationRepository
import com.example.graduation.project.repository.HistoryRepository
import com.example.graduation.project.repository.UserRepository
import com.example.graduation.server.im.SocketMsg
import com.example.graduation.server.im.WebSocketServer
import com.example.graduation.utils.Res
import com.example.graduation.utils.status
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
@Service
@Transactional
class ChatService @Autowired constructor(
    private val chatRepository: ChatRepository,
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    private val log: Logger = LoggerFactory.getLogger(ChatService::class.java)
    fun getHistory(user1: String, user2: String, pageable: Pageable): Res<Page<Chat>> =try{
        val (u1, u2) = if (user1 < user2) { user1 to user2 } else { user2 to user1 }
        val conv=conversationRepository.findByUser1IsAndUser2Is(u1,u2)
        if(conv.isEmpty) Res.SuccessBut(status.successButNoChat)
        else{
            val convId = conv.get().id ?: throw Exception("conversation id is null")
            val chats = chatRepository.findAllByConvIdIsOrderByTimeDesc(convId, pageable)
            //log.debug("获取对话列表成功: "+chats)
            Res.Success(chats)
        }
    }catch (e:Exception){
        log.error("获取聊天记录失败 "+e.message)
        Res.Error("获取聊天记录失败: "+e.message)
    }

    fun getChatUser(
            user: String,
            pageable: Pageable
    ): Res<Page<Map<String, String>>> =try {
        val res = conversationRepository.findAllByUser1IsOrUser2Is(user, user, pageable).map {
            val mp=mutableMapOf<String, String>()
            if(it.id==null) throw Exception("conversation id is null")
            val chat=chatRepository.findTopByConvIdIsOrderByTimeDesc(it.id!!)//找到最后一条记录
            if(chat.isEmpty) throw Exception("char info not found")
            mp["sender"]=chat.get().sender.toString()
            mp["receiver"]=chat.get().receiver.toString()
            mp["msg"]=chat.get().msg.toString()
            mp["time"]=chat.get().time.toString()
            mp["read"]=(chat.get().status==status.read).toString()

            val him:String=if(chat.get().sender==user){chat.get().receiver}else{chat.get().sender}
            mp["isSender"]=(chat.get().sender==user).toString()//user的角色是否是发送者

            val u=userRepository.findById(him)
            if(u.isEmpty) throw Exception("user info not found")

            mp["email"]=u.get().email
            mp["avatar"]=u.get().avatar
            mp["name"]=u.get().username

            mp.toMap()
        }
        Res.Success(res)
    }catch (e:Exception){
        log.error("获取对话列表失败 "+e.message)
        Res.Error("获取对话列表失败: "+e.message)
    }

    fun setRead(user1:String, user2:String):Res<Boolean> =try{
        val (u1, u2) = if (user1 < user2) { user1 to user2 } else { user2 to user1 }
        val conv = conversationRepository.findByUser1IsAndUser2Is(u1,u2)
        if (conv.isEmpty) Res.SuccessBut(status.successButNoChat)
        else{
            val id = conv.get().id ?: throw Exception("conversation id is null")
            val ls = chatRepository.findAllByConvIdIsAndStatusIs(id, status.notRead).map { Chat(it.id, it.convId, it.sender, it.receiver, it.time, it.msg, status.read) }
            chatRepository.saveAll(ls)
            Res.Success(true)
        }
    }catch (e:Exception){
        log.error("设置已读状态失败 "+e.message)
        Res.Error("设置已读失败: "+e.message)
    }

    fun saveChat(socketMsg: SocketMsg): Res<Chat> =try {
        //字典序排序
        val (u1, u2) = if (socketMsg.sender < socketMsg.receiver) {
            socketMsg.sender to socketMsg.receiver
        } else {
            socketMsg.receiver to socketMsg.sender
        }
        val id: Long? =
        if(!conversationRepository.existsByUser1IsAndUser2Is(u1,u2)){
            val conv= Conversation(
                user1 = u1,
                user2 = u2
            )
            conversationRepository.save(conv).id
        }else{
            conversationRepository.findByUser1IsAndUser2Is(u1,u2).get().id
        }
        if(id==null) throw Exception("conversation id is null")

        val chat= Chat(
            convId = id,
            time = socketMsg.time,
            msg = socketMsg.msg,
            status = socketMsg.status,
            sender = socketMsg.sender,
            receiver = socketMsg.receiver
        )
        val model=chatRepository.save(chat)
        Res.Success(model)
    }catch (e:Exception){
        log.error(e.message)
        Res.Error("聊天记录保存失败: "+e.message)
    }

}