package com.iskportal.kauth.session

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession
import javax.sql.DataSource

@Configuration
@EnableJdbcHttpSession
class SessionConfig {

    @Bean
    fun dataSourceInitializer(dataSource: DataSource): DataSourceInitializer {
        val resourceDatabasePopulator = ResourceDatabasePopulator()
        resourceDatabasePopulator.setContinueOnError(true)
        resourceDatabasePopulator.addScript(ClassPathResource("org/springframework/session/jdbc/schema-postgresql.sql"))
        val dataSourceInitializer = DataSourceInitializer()
        dataSourceInitializer.setDataSource(dataSource)
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator)
        return dataSourceInitializer
    }
}