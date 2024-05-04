package com.example.graduation.project.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "user_task_history")
data class History(
     @EmbeddedId
     var PKid: HistoryPK,

     @Column(name="time", columnDefinition = "TIMESTAMP")
     var time: Date,

     @Column(name="`like`")
     var like: Boolean,

     @Column(name="dislike")
     var dislike: Boolean,
){
    @Embeddable
    data class HistoryPK(
            @Column(name = "email")
            val userId: String,
            @Column(name = "id")
            val taskId: Long
    ) : Serializable {
        constructor() : this("", -1) {}
    }
    constructor(id:Long, userId:String, time: Date, like: Boolean, dislike: Boolean) : this(HistoryPK(userId,id), time, like, dislike)
    constructor(): this(HistoryPK(), Date(), false, false)
}