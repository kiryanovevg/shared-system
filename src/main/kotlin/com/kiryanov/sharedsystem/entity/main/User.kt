package com.kiryanov.sharedsystem.entity.main

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_entity")
data class User(

        @Column(nullable = false)
        val name: String,

        @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val news: List<News> = emptyList(),

        @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val comment: List<Comment> = emptyList(),

        @Id
//        @GeneratedValue(generator = "increment")
//        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: String = UUID.randomUUID().toString()
)