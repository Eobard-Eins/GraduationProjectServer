package com.example.graduation.task.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


@Entity
@Table(name="task_img")
data class TaskImage (
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "img")
    var img: String

){
    constructor(): this(-1, "")
}