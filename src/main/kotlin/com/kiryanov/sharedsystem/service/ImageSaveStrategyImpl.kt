package com.kiryanov.sharedsystem.service

import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.image2.ImageRepository2
import com.kiryanov.sharedsystem.repository.main.CommentRepository
import com.kiryanov.sharedsystem.repository.main.NewsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ImageSaveStrategyImpl @Autowired constructor(
        private val newsRepository: NewsRepository,
        private val commentRepository: CommentRepository,
        private val imageRepository: ImageRepository,
        private val imageRepository2: ImageRepository2
) : ImageSaveStrategy {

    override fun getNodeIdForSaveImage(
            entityId: String
    ): ImageService.NodeId = newsRepository.findNewsById(entityId)?.let { ImageService.NodeId.IMAGE_1 }
            ?: commentRepository.findCommentById(entityId)?.let { ImageService.NodeId.IMAGE_2 }
            ?: ImageService.NodeId.IMAGE_1

    override fun getRepository(
            nodeId: ImageService.NodeId
    ): ImageRepository = when(nodeId) {
        ImageService.NodeId.IMAGE_1 -> imageRepository
        ImageService.NodeId.IMAGE_2 -> imageRepository2
    }
}