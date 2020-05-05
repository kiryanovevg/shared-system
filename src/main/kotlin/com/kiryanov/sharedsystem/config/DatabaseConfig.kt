package com.kiryanov.sharedsystem.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.HttpStatus
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "mainTransactionManager",
        basePackages = ["com.kiryanov.sharedsystem.repository.main"]
)
class MainDatabaseConfig(
        @Autowired environment: Environment
) {

    private val hibernateDdl = environment.getProperty("spring.jpa.hibernate.ddl-auto")
    private val hibernateShowSql = environment.getProperty("spring.jpa.show-sql")

    @Bean("mainDataSource")
    @ConfigurationProperties("main-database.datasource")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("mainEntityManagerFactory")
    fun entityManagerFactory(
            @Qualifier("mainDataSource") dataSource: DataSource
    ) = LocalContainerEntityManagerFactoryBean().apply {
        setDataSource(dataSource)
        setPackagesToScan("com.kiryanov.sharedsystem.entity.main")
        jpaVendorAdapter = HibernateJpaVendorAdapter()
        setJpaPropertyMap(mapOf(
                "hibernate.show-sql" to hibernateShowSql,
                "hibernate.hbm2ddl.auto" to hibernateDdl
        ))
    }

    @Bean("mainTransactionManager")
    fun transactionManager(
            @Qualifier("mainEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "imageEntityManagerFactory",
        transactionManagerRef = "imageTransactionManager",
        basePackages = ["com.kiryanov.sharedsystem.repository.image"]
)
class ImageDatabaseConfig(
        @Autowired environment: Environment
) {

    private val hibernateDdl = environment.getProperty("spring.jpa.hibernate.ddl-auto")
    private val hibernateShowSql = environment.getProperty("spring.jpa.show-sql")

    @Bean("imageDataSource")
    @ConfigurationProperties("image-database.datasource")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean("imageEntityManagerFactory")
    fun entityManagerFactory(
            @Qualifier("imageDataSource") dataSource: DataSource
    ) = LocalContainerEntityManagerFactoryBean().apply {
        setDataSource(dataSource)
        setPackagesToScan("com.kiryanov.sharedsystem.entity.image")
        jpaVendorAdapter = HibernateJpaVendorAdapter()
        setJpaPropertyMap(mapOf(
                "hibernate.show-sql" to hibernateShowSql,
                "hibernate.hbm2ddl.auto" to hibernateDdl
        ))
    }

    @Bean("imageTransactionManager")
    fun transactionManager(
            @Qualifier("imageEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}

@Bean
fun webServerCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    return WebServerFactoryCustomizer { container ->
        container.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/"))
    }
}