package com.example.graduation.project.model

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

        @Column(name="address_name")
        var addressName:String,

        @Column(name="address")
        var address:String,

        @Column(name="online")
        var online:Boolean,

        @Column(name="hot")
        var hot:Long,

        @Column(name="point")
        var point:Double,

        @Column(name="time", columnDefinition = "TIMESTAMP")
        var lastTime:Date,
){
    constructor(): this(null, "", "", "","", false, 0,0.0, Date())
    constructor(title: String, content: String, addressName: String, address: String, online: Boolean,point:Double, lastTime: Date): this(null, title, content, addressName, address, online, 0,point, lastTime)
}
