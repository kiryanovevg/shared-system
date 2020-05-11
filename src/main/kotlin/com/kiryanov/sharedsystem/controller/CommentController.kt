package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.Comment
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.main.CommentRepository
import com.kiryanov.sharedsystem.repository.main.NewsRepository
import com.kiryanov.sharedsystem.repository.main.UserRepository
import com.kiryanov.sharedsystem.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.persistence.EntityNotFoundException

@Controller
class CommentController @Autowired constructor(
        private val commentRepository: CommentRepository,
        private val userRepository: UserRepository,
        private val newsRepository: NewsRepository,
        private val imageService: ImageService
) {

    @GetMapping("/comment")
    fun mainAction(model: Model): String {
        model.addAttribute(COMMENT_VIEW, CommentView())
        model.addAttribute(COMMENT_LIST, commentRepository.findAll().map {
            CommentView.map(it, imageService.getImagesByEntityId(it.id))
        })

        return COMMENT_VIEW
    }

    @PostMapping("/comment")
    fun addAction(@ModelAttribute commentView: CommentView, model: Model): String {
        val user = userRepository.findUserByName(commentView.userName)
                ?: userRepository.save(User(commentView.userName))

        val news = try {
            newsRepository.findNewsById(commentView.newsId) ?: throw EntityNotFoundException()
        } catch (e: EntityNotFoundException) {
            model.addAttribute("exception", "News not found")
            return mainAction(model)
        } catch (e: Exception) {
            model.addAttribute("exception", "Comment error: ${e.message}")
            return mainAction(model)
        }

        commentRepository.save(Comment(
                commentView.message,
                user, news
        ))

        return mainAction(model)
    }

    @GetMapping("/comment/delete/{commentId}")
    fun deleteAction(@PathVariable commentId: String, model: Model): String {
        commentRepository.findCommentById(commentId)?.let {
            imageService.deleteImages(it)
            commentRepository.delete(it)
        }

        return "redirect:/comment"
    }

    companion object {
        private const val COMMENT_VIEW = "comment"
        private const val COMMENT_LIST = "commentList"
    }

    data class CommentView constructor(
            var id: String = "",
            var message: String = "",
            var userName: String = "",
            var newsId: String = "",
            var newsName: String ="",
            var images: String = ""
    ) {
        companion object {
            fun map(comment: Comment, images: List<Image>) = CommentView(
                    comment.id,
                    comment.message,
                    comment.user.name,
                    comment.news.id,
                    comment.news.name,
                    images.joinToString { it.name }
            )
        }
    }
}