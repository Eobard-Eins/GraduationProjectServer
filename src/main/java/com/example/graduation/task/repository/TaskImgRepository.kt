package com.example.graduation.task.repository

import com.example.graduation.task.model.TaskImage
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskImgRepository : JpaRepository<TaskImage, Long> {
    @Query(value = "select img from task_img where id=:id", nativeQuery = true)
    fun getImgs(@Param("id") id: Long):List<String>
}