package com.example.graduation.project.repository

import com.example.graduation.project.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface UserRepository : JpaRepository<User, String> {
    @Modifying
    @Query(value = "update user_info set username = :un where email = :ph", nativeQuery = true)
    fun updateUsername(@Param("un") username: String, @Param("ph") email: String): Int // 返回更新条数

    @Modifying
    @Query(value = "update user_info set password = :pw where email = :ph", nativeQuery = true)
    fun updatePassword(@Param("pw") password: String, @Param("ph") email: String): Int

    @Modifying
    @Query(value = "update user_info set avatar = :ava where email = :ph", nativeQuery = true)
    fun updateAvatar(@Param("ava") avatar: String, @Param("ph") email: String): Int

    @Modifying
    @Query(value = "update user_info set point = :pt where email = :ph", nativeQuery = true)
    fun updatePoint(@Param("pt") point: Double, @Param("ph") email: String): Int
}