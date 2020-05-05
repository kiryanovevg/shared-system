package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.main.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController @Autowired constructor(
        private val userRepository: UserRepository,
        private val imageRepository: ImageRepository
) {

    @GetMapping("/")
    fun mainAction(): String = "index"
}