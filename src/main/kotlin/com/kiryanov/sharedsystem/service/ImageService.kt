package com.kiryanov.sharedsystem.service

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.Comment
import com.kiryanov.sharedsystem.entity.main.ImageNode
import com.kiryanov.sharedsystem.entity.main.News
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.image2.ImageRepository2
import com.kiryanov.sharedsystem.repository.main.ImageNodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ImageService @Autowired constructor(
        private val imageNodeRepository: ImageNodeRepository,
        private val imageRepository: ImageRepository,
        private val imageRepository2: ImageRepository2
) {

    fun getAll(): List<Image> {
        return imageNodeRepository.findAll().flatMap {
            getRepository(it.nodeId).getImagesByEntityId(it.entityId)
        }
    }

    fun getImagesByEntityId(entityId: String): List<Image> {
        return imageNodeRepository.getNodeByEntityId(entityId)?.let {
            getRepository(it.nodeId).getImagesByEntityId(entityId)
        } ?: emptyList()
    }

    fun getImagesByUserAndNodeId(user: User, nodeId: NodeId): List<Image> {
        val newsImages = user.news.filter { news ->
            imageNodeRepository.getNodeByEntityId(news.id)?.nodeId == nodeId
        }.flatMap { news -> imageNodeRepository.getNodeByEntityId(news.id)?.let { node ->
            getRepository(node.nodeId).getImagesByEntityId(news.id)
        } ?: emptyList() }

        val commentImages = user.comment.filter { comment ->
            imageNodeRepository.getNodeByEntityId(comment.id)?.nodeId == nodeId
        }.flatMap { comment -> imageNodeRepository.getNodeByEntityId(comment.id)?.let { node ->
            getRepository(node.nodeId).getImagesByEntityId(comment.id)
        } ?: emptyList() }

        return listOf(newsImages, commentImages).flatten()
    }

    fun add(imageName: String, entityId: String): Image {
        return imageRepository.save(Image(
                imageName,
                entityId
        )).also { image ->
            imageNodeRepository.getNodeByEntityId(image.entityId)
                    ?: imageNodeRepository.save(ImageNode(
                            NodeId.IMAGE_1, image.entityId
                    ))
        }
    }

    fun deleteImage(entityId: String, imageId: String) {
        imageNodeRepository.getNodeByEntityId(entityId)?.let { node ->
            getRepository(node.nodeId).let { repository ->
                repository.findImageById(imageId)?.let { image ->
                    repository.delete(image)
                }
            }

            deleteNodeIfNeeded(node)
        }
    }

    fun deleteImages(comment: Comment) {
        imageNodeRepository.getNodeByEntityId(comment.id)?.let { node ->
            getRepository(node.nodeId).deleteImageByEntityId(comment.id)
            deleteNodeIfNeeded(node)
        }
    }

    fun deleteImages(news: News) {
        imageNodeRepository.getNodeByEntityId(news.id)?.let { node ->
            getRepository(node.nodeId).deleteImageByEntityId(news.id)
            deleteNodeIfNeeded(node)
        }
        news.comment.forEach { deleteImages(it) }
    }

    fun deleteImages(user: User) {
        user.news.forEach { deleteImages(it) }
        user.comment.forEach { deleteImages(it) }
    }

    private fun deleteNodeIfNeeded(node: ImageNode) {
        if (getRepository(node.nodeId).getCountByEntityId(node.entityId) == 0L) {
            imageNodeRepository.delete(node)
        }
    }

    private fun getRepository(nodeId: NodeId): ImageRepository = when(nodeId) {
        NodeId.IMAGE_1 -> imageRepository
        NodeId.IMAGE_2 -> imageRepository2
    }

    private fun getNodeId(repository: ImageRepository): NodeId = when(repository) {
        is ImageRepository2 -> NodeId.IMAGE_2
        else -> NodeId.IMAGE_1
    }

    enum class NodeId {
        IMAGE_1, IMAGE_2;
    }
}