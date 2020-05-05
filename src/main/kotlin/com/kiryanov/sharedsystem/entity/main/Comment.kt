package com.kiryanov.sharedsystem.entity.main

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "comment")
data class Comment(

        @Column(nullable = false)
        val message: String,

        @JoinColumn(name = "user_id")
        @ManyToOne
        val user: User,

        @JoinColumn(name = "news_id")
        @ManyToOne
        val news: News,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)