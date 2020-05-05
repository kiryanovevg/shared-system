package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.main.Comment
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.main.CommentRepository
import com.kiryanov.sharedsystem.repository.main.NewsRepository
import com.kiryanov.sharedsystem.repository.main.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class CommentController @Autowired constructor(
        private val commentRepository: CommentRepository,
        private val userRepository: UserRepository,
        private val newsRepository: NewsRepository
) {

    @GetMapping("/comment")
    fun mainAction(model: Model): String {
        model.addAttribute(COMMENT_VIEW, CommentView())
        model.addAttribute(COMMENT_LIST, commentRepository.findAll().map { CommentView.map(it) })

        return COMMENT_VIEW
    }

    @PostMapping("/comment")
    fun addAction(@ModelAttribute commentView: CommentView, model: Model): String {
        val user = userRepository.findUserByName(commentView.userName)
                ?: userRepository.save(User(commentView.userName))

        val news = try {
            newsRepository.getOne(commentView.newsId.toLong())
        } catch (e: JpaObjectRetrievalFailureException) {
            model.addAttribute("exception", "News not found")
            return mainAction(model)
        } catch (e: Exception) {
            model.addAttribute("exception", "News error: ${e.message}")
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
        commentRepository.deleteById(commentId.toLong())

        return "redirect:/comment"
    }

    companion object {
        private const val COMMENT_VIEW = "comment"
        private const val COMMENT_LIST = "commentList"
    }

    data class CommentView constructor(
            var id: Long = 0,
            var message: String = "",
            var userName: String = "",
            var newsId: String = "",
            var newsName: String ="",
            var imageName: String = ""
    ) {
        companion object {
            fun map(comment: Comment) = CommentView(
                    comment.id,
                    comment.message,
                    comment.user.name,
                    comment.news.id.toString(),
                    comment.news.name
            )
        }
    }
}