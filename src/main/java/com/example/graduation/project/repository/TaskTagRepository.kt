package com.example.graduation.project.repository

import com.example.graduation.project.model.TaskTag
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TaskTagRepository : JpaRepository<TaskTag, TaskTag.TaskTagPK> {
//    @Query(value = "select tag from task_tag where id=:id", nativeQuery = true)
//    fun getTags(@Param("id") id: Long):List<String>

    fun findAllByPKidId(id: Long): List<TaskTag>
}