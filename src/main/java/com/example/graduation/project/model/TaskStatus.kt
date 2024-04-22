package com.example.graduation.project.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="task_status")
data class TaskStatus(
        @Id
        var taskId:Long,

        @Column(name="publish_user_id")
        var publishUserId:String,

        @Column(name="status")
        var status:Int,

        @Column(name="access_user_id")
        var accessUserId:String

){
    constructor(): this(-1,"",-1,"")
    constructor(taskId: Long,publishUserId: String,status: Int):this(taskId,publishUserId,status,"")
}
