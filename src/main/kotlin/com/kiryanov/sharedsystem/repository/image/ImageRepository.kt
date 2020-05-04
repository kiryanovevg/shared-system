package com.kiryanov.sharedsystem.repository.image

import com.kiryanov.sharedsystem.entity.image.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository: JpaRepository<Image, Long>