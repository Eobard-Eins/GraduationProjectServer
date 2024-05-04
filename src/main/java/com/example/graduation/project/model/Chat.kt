package com.example.graduation.project.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "chat")
data class Chat(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long?=null,

        @Column(name="conv_id")
        var convId: Long,

        @Column(name="sender")
        var sender:String,

        @Column(name="receiver")
        var receiver:String,

        @Column(name="time", columnDefinition = "TIMESTAMP")
        var time: Date,

        @Column(name="msg")
        var msg:String,

        @Column(name="status")
        var status:Int

){
    constructor(): this(null, 0, "", "", Date(), "", 0)
    constructor(convId: Long, sender:String, receiver:String, time: Date, msg: String, status: Int): this(null, convId,sender,receiver, time, msg, status)
}
