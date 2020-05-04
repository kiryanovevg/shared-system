package com.kiryanov.sharedsystem.controller

import com.kiryanov.sharedsystem.entity.image.Image
import com.kiryanov.sharedsystem.entity.main.User
import com.kiryanov.sharedsystem.repository.image.ImageRepository
import com.kiryanov.sharedsystem.repository.main.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController @Autowired constructor(
        private val userRepository: UserRepository,
        private val imageRepository: ImageRepository
){

    @GetMapping
    fun mainAction(): String = "Данила aa <br><br>" + userAction()

//    @GetMapping("user")
    fun userAction(): String {
        userRepository.save(
                User(
                        "User #${userRepository.count()}"
                )
        )

        imageRepository.save(
                Image(
                        "Image #${imageRepository.count()}"
                )
        )

        imageRepository.flush()

        return userRepository.findAll().joinToString { it.name } + "<br><br>" +
                imageRepository.findAll().joinToString { it.name }
    }
}