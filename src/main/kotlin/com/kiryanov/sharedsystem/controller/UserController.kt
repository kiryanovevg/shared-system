package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.main.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class UserController @Autowired constructor(
        private val userRepository: UserRepository
) {

    @GetMapping("/user")
    fun mainAction(model: Model): String {
        model.addAttribute(USER_VIEW, UserView())
        model.addAttribute(USER_LIST, userRepository.findAll().map { UserView.map(it) })

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
        userRepository.deleteById(userId.toLong())

        return "redirect:/user"
    }

    companion object {
        private const val USER_VIEW = "user"
        private const val USER_LIST = "userList"
    }

    data class UserView constructor(
            var id: Long = 0,
            var name: String = ""
    ) {
        companion object {
            fun map(user: User) = UserView(
                    user.id,
                    user.name
            )
        }
    }
}