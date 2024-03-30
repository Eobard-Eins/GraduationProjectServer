package com.example.graduation.task.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name="task_tag")
data class TaskTag (
        @Id
        //@GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(name = "tag")
        var tag: String

){
    constructor(): this(-1, "")
}