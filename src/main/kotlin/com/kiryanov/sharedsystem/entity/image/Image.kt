package com.kiryanov.sharedsystem.entity.image

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "image")
data class Image(

        val name: String,

        @Column(nullable = false)
        val entityId: String,

        @Id
//        @GeneratedValue(generator = "increment")
//        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: String = UUID.randomUUID().toString()
)