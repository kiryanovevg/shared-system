package com.kiryanov.sharedsystem.entity.main

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "news")
data class News(

        @Column(nullable = false)
        val name: String,

        @JoinColumn(name = "user_id")
        @ManyToOne
        val user: User,

        @OneToMany(mappedBy = "news", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
        val comment: List<Comment> = emptyList(),

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)