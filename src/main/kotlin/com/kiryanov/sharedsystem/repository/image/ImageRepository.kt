package com.kiryanov.sharedsystem.repository.image

import com.kiryanov.sharedsystem.entity.image.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ImageRepository: JpaRepository<Image, Long> {

//    @Query("SELECT u FROM User u WHERE u.name = :name")
//    fun findUserByName(@Param("name") name: String): User?

    @Query("select im from Image im where im.entityId = :entityId")
    fun getImagesByEntityId(@Param("entityId") entityId: String): List<Image>
}