package com.kiryanov.sharedsystem.repository.image2

import com.kiryanov.sharedsystem.repository.image.ImageRepository

interface ImageRepository2: ImageRepository/* {

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
}*/