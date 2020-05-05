package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.News
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.main.NewsRepository
import com.kiryanov.sharedsystem.repository.main.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class NewsController @Autowired constructor(
        private val userRepository: UserRepository,
        private val newsRepository: NewsRepository,
        private val imageRepository: ImageRepository
) {

    @GetMapping("/news")
    fun mainAction(model: Model): String {
        model.addAttribute(NEWS_VIEW, NewsView())
        model.addAttribute(NEWS_LIST, newsRepository.findAll().map {
            NewsView.map(it, imageRepository.getImagesByEntityId(it.id.toString()))
        })

        return NEWS_VIEW
    }

    @PostMapping("/news")
    fun addAction(@ModelAttribute newsView: NewsView, model: Model): String {
        val user = userRepository.findUserByName(newsView.userName)
                ?: userRepository.save(User(newsView.userName))

        /*val savedNews =*/ newsRepository.save(News(
                newsView.name,
                user
        ))

//        model.addAttribute(NEWS_VIEW, NewsView.map(savedNews))
        return mainAction(model)
    }

    @GetMapping("/news/delete/{newsId}")
    fun deleteAction(@PathVariable newsId: String, model: Model): String {
        newsRepository.getOne(newsId.toLong()).let {
            imageRepository.deleteImageByEntityId(it.id.toString())
            imageRepository.deleteImageByEntityId(it.comment.map { comment -> comment.id.toString() })
            newsRepository.delete(it)
        }

        return "redirect:/news"
    }

    companion object {
        private const val NEWS_VIEW = "news"
        private const val NEWS_LIST = "newsList"
    }

    data class NewsView constructor(
            var id: Long = 0,
            var name: String = "",
            var userName: String = "",
            var images: String = ""
    ) {
        companion object {
            fun map(news: News, images: List<Image>) = NewsView(
                    news.id,
                    news.name,
                    news.user.name,
                    images.joinToString { it.name }
            )
        }
    }
}