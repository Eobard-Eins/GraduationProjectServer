package com.example.graduation.project.repository

import com.example.graduation.project.model.TaskInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
@Repository
interface TaskInfoRepository : JpaRepository<TaskInfo, Long> {
    fun findAllByLastTimeBeforeAndStatusIsNotAndStatusIsNot(nowTime: Date, status1: Int, status2: Int): List<TaskInfo>

    fun getTaskStatusByPublishUserIdIsAndStatusIs(publishUserId: String,status: Int, pageable: Pageable):Page<TaskInfo>
    fun getTaskStatusByAccessUserIdIsAndStatusIs(accessUserId: String,status: Int, pageable: Pageable):Page<TaskInfo>

    fun getTaskStatusByPublishUserIdIs(publishUserId: String, pageable: Pageable):Page<TaskInfo>
    fun getTaskStatusByAccessUserIdIs(accessUserId: String, pageable: Pageable):Page<TaskInfo>

}