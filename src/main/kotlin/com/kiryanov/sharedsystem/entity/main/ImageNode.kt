package com.kiryanov.sharedsystem.entity.main

import com.kiryanov.sharedsystem.service.ImageService
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "image_node")
data class ImageNode(

        @Column(nullable = false)
        val nodeId: ImageService.NodeId,

        @Column(nullable = false)
        val entityId: String,

        @Id
        @Column(nullable = false, updatable = false)
        val id: String = UUID.randomUUID().toString()
)