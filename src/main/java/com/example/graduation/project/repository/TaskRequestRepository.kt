package com.example.graduation.project.repository

import com.example.graduation.project.model.TaskRequest
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface TaskRequestRepository : JpaRepository<TaskRequest, TaskRequest.TaskRequestPK> {
    @Query(value = "select request_user_id from task_request where task_id=:id", nativeQuery = true)
    fun getTaskRequestsByTaskId(@Param("id") id:Long):List<String>

    @Modifying
    @Query(value = "delete from task_request where task_id=:id", nativeQuery = true)
    fun deleteTaskRequestsByTaskIdIs(@Param("id") id:Long):Int
}