package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.main.CommentRepository
import com.kiryanov.sharedsystem.repository.main.NewsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ImageController @Autowired constructor(
        private val imageRepository: ImageRepository,
        private val newsRepository: NewsRepository,
        private val commentRepository: CommentRepository
) {

    @GetMapping("/image")
    fun mainAction(model: Model): String {
        model.addAttribute(IMAGE_VIEW, ImageView())
        model.addAttribute(IMAGE_LIST, imageRepository.findAll().map { ImageView.map(it) })

        return IMAGE_VIEW
    }

    @PostMapping("/image")
    fun addAction(@ModelAttribute imageView: ImageView, model: Model): String {
        val entityId = try {
            imageView.entityId.toLong()
        } catch (e: Exception) {
            model.addAttribute("exception", e.message)
            return mainAction(model)
        }

        try {
            newsRepository.getOne(entityId).let {
                imageRepository.save(Image(
                        imageView.name,
                        it.id.toString()
                ))
            }

            return mainAction(model)
        } catch (e: Exception) {
            //ignored
        }

        try {
            commentRepository.getOne(entityId).let {
                imageRepository.save(Image(
                        imageView.name,
                        it.id.toString()
                ))
            }

            return mainAction(model)
        } catch (e: Exception) {
            //ignored
        }

        model.addAttribute("exception", "Entity not found")
        return mainAction(model)
    }

    @GetMapping("/image/delete/{imageId}")
    fun deleteAction(@PathVariable imageId: String, model: Model): String {
        imageRepository.deleteById(imageId.toLong())

        return "redirect:/image"
    }

    companion object {
        private const val IMAGE_VIEW = "image"
        private const val IMAGE_LIST = "imageList"
    }

    data class ImageView constructor(
            var id: Long = 0,
            var name: String = "",
            var entityId: String = "",
            var entityType: String = ""
    ) {
        companion object {
            fun map(image: Image) = ImageView(
                    image.id,
                    image.name,
                    image.entityId
            )
        }
    }
}