package com.example.graduation.project.repository

import com.example.graduation.project.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TaskStatusRepository: JpaRepository<TaskStatus, Long> {
    fun getTaskStatusByPublishUserIdIsAndStatusIs(publishUserId: String,status: Int):List<TaskStatus>
    fun getTaskStatusByAccessUserIdIsAndStatusIs(accessUserId: String,status: Int):List<TaskStatus>

    fun getTaskStatusByPublishUserIdIs(publishUserId: String):List<TaskStatus>
    fun getTaskStatusByAccessUserIdIs(accessUserId: String):List<TaskStatus>

    fun getTaskStatusByTaskIdIs(taskId: Long):TaskStatus
}