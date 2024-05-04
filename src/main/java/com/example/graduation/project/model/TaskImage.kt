package com.example.graduation.project.model

import jakarta.persistence.*
import java.io.Serializable


@Entity
@Table(name="task_img")
data class TaskImage (
    @EmbeddedId val PKid: TaskImgPK,
){
    @Embeddable
    data class TaskImgPK(
            @Column(name = "id")
            val id: Long,
            @Column(name = "img")
            val img: String
    ) : Serializable {
        constructor() : this(-1,"") {}

    }
    constructor(id:Long,url:String) : this(TaskImgPK(id,url)) {}
    constructor() : this(TaskImgPK()) {}
}
