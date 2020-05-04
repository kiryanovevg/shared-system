package com.kiryanov.sharedsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScans

@ComponentScans
@SpringBootApplication(
		exclude = [
//			DataSourceAutoConfiguration::class,
//			HibernateJpaAutoConfiguration::class,
//			DataSourceTransactionManagerAutoConfiguration::class
		]
)
class SharedsystemApplication

fun main(args: Array<String>) {
	runApplication<SharedsystemApplication>(*args)
}
