package com.kiryanov.sharedsystem.repository.image

import com.kiryanov.sharedsystem.entity.image.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface ImageRepository: JpaRepository<Image, Long> {

    @Query("select im from Image im where im.entityId = :entityId")
    fun getImagesByEntityId(@Param("entityId") entityId: String): List<Image>

    @Modifying
    @Transactional
    @Query("delete from Image im where im.entityId in (:entityIds)")
    fun deleteImageByEntityId(@Param("entityIds") entityIds: List<String>)

    @Modifying
    @Transactional
    @Query("delete from Image im where im.entityId = :entityId")
    fun deleteImageByEntityId(@Param("entityId") entityId: String)

    @Query("SELECT im FROM Image im WHERE im.id = :id")
    fun findImageById(@Param("id") id: String): Image?

    @Query("SELECT COUNT(im) FROM Image im WHERE im.entityId = :entityId")
    fun getCountByEntityId(@Param("entityId") entityId: String): Long
}