package com.example.graduation.task.repository

import com.example.graduation.task.model.TaskInfo
import org.springframework.data.jpa.repository.JpaRepository

interface TaskInfoRepository : JpaRepository<TaskInfo, Long> {
}