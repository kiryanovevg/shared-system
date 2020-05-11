package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.main.UserRepository
import com.kiryanov.sharedsystem.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class UserController @Autowired constructor(
        private val userRepository: UserRepository,
        private val imageService: ImageService
) {

    @GetMapping("/user")
    fun mainAction(model: Model): String {
        model.addAttribute(USER_VIEW, UserView())
        model.addAttribute(USER_LIST, userRepository.findAll().map {
            UserView.map(
                    it,
                    imageService.getImagesByUserAndNodeId(it, ImageService.NodeId.IMAGE_1),
                    imageService.getImagesByUserAndNodeId(it, ImageService.NodeId.IMAGE_2)
            )
        })

        return USER_VIEW
    }

    @PostMapping("/user")
    fun addAction(@ModelAttribute userView: UserView, model: Model): String {
        val user: User? = userRepository.findUserByName(userView.name)
        model.addAttribute("userExist", user != null)
        user ?: userRepository.save(User(userView.name))

        return mainAction(model)
    }

    @GetMapping("/user/delete/{userId}")
    fun deleteAction(@PathVariable userId: String, model: Model): String {
        userRepository.findUserById(userId)?.let { deletedUser ->
            imageService.deleteImages(deletedUser)
            userRepository.delete(deletedUser)
        }

        return "redirect:/user"
    }

    companion object {
        private const val USER_VIEW = "user"
        private const val USER_LIST = "userList"
    }

    data class UserView constructor(
            var id: String = "",
            var name: String = "",
            var images1: String = "",
            var images2: String = ""
    ) {
        companion object {
            fun map(user: User, images1: List<Image>, images2: List<Image>) = UserView(
                    user.id,
                    user.name,
                    images1.joinToString { it.name },
                    images2.joinToString { it.name }
            )
        }
    }
}