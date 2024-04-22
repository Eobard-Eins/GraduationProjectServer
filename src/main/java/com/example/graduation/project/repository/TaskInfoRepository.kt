package com.example.graduation.project.repository

import com.example.graduation.project.model.TaskInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TaskInfoRepository : JpaRepository<TaskInfo, Long> {
    fun findAllByLastTimeBefore(nowTime: Date): List<TaskInfo>
}