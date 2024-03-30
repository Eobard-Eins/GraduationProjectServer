package com.example.graduation.task.repository

import com.example.graduation.task.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository

interface TaskStatusRepository: JpaRepository<TaskStatus, Long> {
}