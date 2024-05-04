package com.example.graduation.project.model

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name="task_request")
data class TaskRequest (
        @EmbeddedId val PKid: TaskRequestPK,

){
    @Embeddable
    data class TaskRequestPK(
            @Column(name = "task_id")
            val id: Long,
            @Column(name = "request_user_id")
            val uid: String
    ) : Serializable {
        constructor() : this(-1,"") {}

    }
    constructor(id:Long,uid:String) : this(TaskRequestPK(id,uid)) {}
    constructor() : this(TaskRequestPK()) {}
}
