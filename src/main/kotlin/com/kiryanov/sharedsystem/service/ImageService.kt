package com.kiryanov.sharedsystem.service

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.Comment
import com.kiryanov.sharedsystem.entity.main.ImageNode
import com.kiryanov.sharedsystem.entity.main.News
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.main.ImageNodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ImageService @Autowired constructor(
        private val imageNodeRepository: ImageNodeRepository,
        private val imageSaveStrategy: ImageSaveStrategy
) {

    fun getAll(): List<Image> {
        return imageNodeRepository.findAll().flatMap {
            imageSaveStrategy.getRepository(it.nodeId).getImagesByEntityId(it.entityId)
        }
    }

    fun getImagesByEntityId(entityId: String): List<Image> {
        return imageNodeRepository.getNodeByEntityId(entityId)?.let {
            imageSaveStrategy.getRepository(it.nodeId).getImagesByEntityId(entityId)
        } ?: emptyList()
    }

    fun getImagesByUserAndNodeId(user: User, nodeId: NodeId): List<Image> {
        val newsImages = user.news.filter { news ->
            imageNodeRepository.getNodeByEntityId(news.id)?.nodeId == nodeId
        }.flatMap { news -> imageNodeRepository.getNodeByEntityId(news.id)?.let { node ->
            imageSaveStrategy.getRepository(node.nodeId).getImagesByEntityId(news.id)
        } ?: emptyList() }

        val commentImages = user.comment.filter { comment ->
            imageNodeRepository.getNodeByEntityId(comment.id)?.nodeId == nodeId
        }.flatMap { comment -> imageNodeRepository.getNodeByEntityId(comment.id)?.let { node ->
            imageSaveStrategy.getRepository(node.nodeId).getImagesByEntityId(comment.id)
        } ?: emptyList() }

        return listOf(newsImages, commentImages).flatten()
    }

    fun add(imageName: String, entityId: String): Image {
        val nodeId = imageNodeRepository.getNodeByEntityId(entityId)?.nodeId
                ?: imageSaveStrategy.getNodeIdForSaveImage(entityId)

        return imageSaveStrategy.getRepository(nodeId).save(Image(
                imageName,
                entityId
        )).also { image ->
            imageNodeRepository.getNodeByEntityId(image.entityId)
                    ?: imageNodeRepository.save(ImageNode(
                            nodeId, image.entityId
                    ))
        }
    }

    fun deleteImage(entityId: String, imageId: String) {
        imageNodeRepository.getNodeByEntityId(entityId)?.let { node ->
            imageSaveStrategy.getRepository(node.nodeId).let { repository ->
                repository.findImageById(imageId)?.let { image ->
                    repository.delete(image)
                }
            }

            deleteNodeIfNeeded(node)
        }
    }

    fun deleteImages(comment: Comment) {
        imageNodeRepository.getNodeByEntityId(comment.id)?.let { node ->
            imageSaveStrategy.getRepository(node.nodeId).deleteImageByEntityId(comment.id)
            deleteNodeIfNeeded(node)
        }
    }

    fun deleteImages(news: News) {
        imageNodeRepository.getNodeByEntityId(news.id)?.let { node ->
            imageSaveStrategy.getRepository(node.nodeId).deleteImageByEntityId(news.id)
            deleteNodeIfNeeded(node)
        }
        news.comment.forEach { deleteImages(it) }
    }

    fun deleteImages(user: User) {
        user.news.forEach { deleteImages(it) }
        user.comment.forEach { deleteImages(it) }
    }

    private fun deleteNodeIfNeeded(node: ImageNode) {
        if (imageSaveStrategy.getRepository(node.nodeId).getCountByEntityId(node.entityId) == 0L) {
            imageNodeRepository.delete(node)
        }
    }

    enum class NodeId {
        IMAGE_1, IMAGE_2;
    }
}