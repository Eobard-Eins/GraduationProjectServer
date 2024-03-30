package com.example.graduation.task.repository

import com.example.graduation.task.model.TaskTag
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskTagRepository : JpaRepository<TaskTag, Long> {
    @Query(value = "select tag from task_tag where id=:id", nativeQuery = true)
    fun getTags(@Param("id") id: Long):List<String>
}