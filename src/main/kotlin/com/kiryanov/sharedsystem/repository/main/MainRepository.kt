package com.kiryanov.sharedsystem.repository.main

import com.kiryanov.sharedsystem.entity.main.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long>