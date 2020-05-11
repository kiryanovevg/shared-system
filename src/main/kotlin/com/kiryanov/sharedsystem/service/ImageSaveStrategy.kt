package com.kiryanov.sharedsystem.service

import com.kiryanov.sharedsystem.repository.image.ImageRepository

interface ImageSaveStrategy {

    fun getNodeIdForSaveImage(entityId: String): ImageService.NodeId
    fun getRepository(nodeId: ImageService.NodeId): ImageRepository
//    fun getRepository(entityId: String): ImageRepository
}