package com.example.graduation.project.repository

import com.example.graduation.project.model.Chat
import com.example.graduation.project.model.Conversation
import com.example.graduation.project.model.TaskInfo
import com.example.graduation.utils.status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {
    fun findTopByConvIdIsOrderByTimeDesc(convId: Long): Optional<Chat>

    fun findAllByConvIdIsOrderByTimeDesc(convId: Long, pageable: Pageable): Page<Chat>

    fun findAllByConvIdIsAndStatusIs(id: Long, status: Int): List<Chat>

}