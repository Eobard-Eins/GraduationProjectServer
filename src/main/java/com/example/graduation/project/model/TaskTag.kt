package com.example.graduation.project.model

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name="task_tag")
data class TaskTag (
        @EmbeddedId val PKid: TaskTagPK
){
    @Embeddable
    data class TaskTagPK(
            val id: Long,
            val tag: String
    ) : Serializable {
        constructor() : this(-1,"") {}

    }
    constructor(id:Long,tag:String) : this(TaskTagPK(id,tag)) {}
    constructor() : this(TaskTagPK()) {}
}