package com.example.graduation.server.im

import com.alibaba.fastjson.JSONObject
import com.example.graduation.utils.Res
import java.util.Date

data class SocketMsg(
        var sender: String, //发送者
        var receiver: String, //接受者
        var msg: String,
        var status: Int,
        var time: Date
){
    constructor(sender: String, receiver: String, msg: String, status:Int, time: Long):this(sender, receiver, msg, status, Date(time))
    fun toJSON(id:Int):String{
        return "{\"id\":$id,\"sender\":\"$sender\",\"receiver\":\"$receiver\",\"msg\":\"$msg\",\"status\":$status,\"time\":\"${time.toInstant().toString()}\"}"
    }
    companion object{
        fun decode(info:String):Res<SocketMsg>{
            val json = JSONObject.parseObject<Map<*, *>>(info, MutableMap::class.java)
            return if(json.containsKey("sender") && json.containsKey("receiver") && json.containsKey("msg") && json.containsKey("status") && json.containsKey("time")){
                Res.Success(SocketMsg(
                        sender = json["sender"] as String,
                        receiver = json["receiver"] as String,
                        msg = json["msg"] as String,
                        status = json["status"] as Int,
                        time = Date(json["time"] as Long)
                ))
            }else{
                Res.Error("info miss")
            }
        }
    }

}
