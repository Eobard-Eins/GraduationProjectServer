package com.example.graduation.project.repository

import com.example.graduation.project.model.History
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface HistoryRepository : JpaRepository<History, History.HistoryPK> {
    @Query(value = "select id from user_task_history where email=:el order by time desc limit :num ", nativeQuery = true)
    fun findByEmail(@Param("el") email: String,@Param("num") num: Int): List<History>
}