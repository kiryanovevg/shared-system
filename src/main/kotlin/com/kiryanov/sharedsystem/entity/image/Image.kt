package com.kiryanov.sharedsystem.entity.image

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "image_entity")
data class Image(

        val name: String,

        @Id
        @GeneratedValue(generator = "increment")
        @GenericGenerator(name= "increment", strategy= "increment")
        @Column(nullable = false, updatable = false)
        val id: Long = 0
)