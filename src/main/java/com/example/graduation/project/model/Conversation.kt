package com.example.graduation.project.model

import jakarta.persistence.*

@Entity
@Table(name="conversation")
data class Conversation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long?=null,

        @Column(name="user1")
        var user1:String,

        @Column(name="user2")
        var user2:String,
){
    constructor(): this(null, "", "")
    constructor(user1: String, user2: String): this(null, user1, user2)
}
