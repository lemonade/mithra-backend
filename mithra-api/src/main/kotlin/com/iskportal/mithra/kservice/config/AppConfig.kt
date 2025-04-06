package com.iskportal.mithra.com.iskportal.mithra.kservice.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource

@Configuration
class AppConfig(
    val env: Environment
) {
    private val username: String = System.getenv("DB_USER")
    private val password: String = System.getenv("DB_PWD")

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
}