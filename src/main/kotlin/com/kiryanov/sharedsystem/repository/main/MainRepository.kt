package com.kiryanov.sharedsystem.repository.main

import com.kiryanov.sharedsystem.entity.main.Comment
import com.kiryanov.sharedsystem.entity.main.News
import com.kiryanov.sharedsystem.entity.main.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository: JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id")
    fun findUserById(@Param("id") id: String): User?

    @Query("SELECT u FROM User u WHERE u.name = :name")
    fun findUserByName(@Param("name") name: String): User?
}

interface NewsRepository: JpaRepository<News, Long> {

    @Query("SELECT n FROM News n WHERE n.id = :id")
    fun findNewsById(@Param("id") id: String): News?
}

interface CommentRepository: JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.id = :id")
    fun findCommentById(@Param("id") id: String): Comment?
}