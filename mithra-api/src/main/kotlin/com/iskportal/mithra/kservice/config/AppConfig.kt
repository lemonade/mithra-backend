package com.iskportal.mithra.kservice.config

import com.iskportal.kauth.security.providers.UserAuthenticationToken
import com.iskportal.kauth.user.model.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditor")
@EnableJpaRepositories(
    basePackages = [
        "com.iskportal.mithra.kservice.repository"
    ]
)
@EntityScan(
    basePackages = [
        "com.iskportal.mithra.kservice.model"
    ]
)
class AppConfig(
    val env: Environment
) {
    private val username: String = System.getenv("DB_USER")
    private val password: String = System.getenv("DB_PWD")
    private val allowOrigins: String? = env.getProperty("mithra.cors.allow-origins")

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(
                        *(allowOrigins ?: "").split(",".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray())
                    .allowedMethods("GET", "POST", "PUT", "OPTION", "DELETE")
                    .allowCredentials(true)
            }
        }
    }

    @Bean
    fun dataSource(): DataSource {
        val hikariConfig = HikariConfig()

        hikariConfig.jdbcUrl = env.getProperty("spring.datasource.url")
        hikariConfig.username = username
        hikariConfig.password = password

        hikariConfig.maximumPoolSize = 20
        hikariConfig.minimumIdle = 5
        hikariConfig.idleTimeout = 10000
        hikariConfig.connectionTimeout = 20000
        hikariConfig.maxLifetime = 30000
        hikariConfig.isAutoCommit = false

        return HikariDataSource(hikariConfig)
    }

    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway.configure().table(env.getProperty("spring.flyway.table")).baselineOnMigrate(true)
            .baselineVersion("0")
            .dataSource(dataSource())
            .locations(env.getProperty("spring.flyway.locations"), "classpath:/db/migration/common")
            .load()
    }

    @Bean
    fun auditor(): AuditorAware<User> =
        AuditorAware {
            Optional.ofNullable(
                (SecurityContextHolder.getContext().authentication as? UserAuthenticationToken?)
                ?.userId
                ?.let { User.of(it) }
            )
        }

}