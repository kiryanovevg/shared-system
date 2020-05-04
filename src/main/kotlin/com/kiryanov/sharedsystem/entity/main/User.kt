package com.kiryanov.sharedsystem.entity.main

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "user_entity")
data class User(

        val name: String,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)