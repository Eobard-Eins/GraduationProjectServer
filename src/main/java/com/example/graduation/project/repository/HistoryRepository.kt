package com.example.graduation.project.repository

import com.example.graduation.project.model.History
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface HistoryRepository : JpaRepository<History, History.HistoryPK> {
//    @Query(value = "select id from user_task_history where email = :el order by time desc", nativeQuery = true)
//    fun findByEmail(@Param("el") email: String, pageable: Pageable): Page<Long>

    fun findAllByPKidUserId(email: String, pageable: Pageable): Page<History>
}