package com.example.graduation.task.model

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

){
    constructor(): this(-1,"",-1)
}
