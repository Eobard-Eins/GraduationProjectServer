package com.example.graduation.project.repository

import com.example.graduation.project.model.Conversation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByUser1IsOrUser2Is(u1: String, u2: String, pageable: Pageable): Page<Conversation>


    fun findByUser1IsAndUser2Is(u1: String, u2: String): Optional<Conversation>

    fun existsByUser1IsAndUser2Is(u1: String, u2: String): Boolean
}