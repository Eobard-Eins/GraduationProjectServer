package com.example.graduation.task.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "task_info")
data class TaskInfo(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long?=null,

        @Column(name="title")
        var title:String,

        @Column(name="content")
        var content:String,

        @Column(name="address")
        var address:String,

        @Column(name="online")
        var online:Boolean,

        @Column(name="hot")
        var hot:Long,

        @Column(name="time", columnDefinition = "TIMESTAMP")
        var lastTime:Date,
){
    constructor(): this(null, "", "", "", false, 0, Date())
    constructor(title: String, content: String, address: String, online: Boolean, lastTime: Date): this(null, title, content, address, online, 0, lastTime)
}
