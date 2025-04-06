package com.iskportal.kauth

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EntityScan(basePackages = [
    "com.iskportal.kauth.user.model",
    "com.iskportal.kauth.session.model",
    "com.iskportal.kauth.security.model"
])
@EnableJpaRepositories(basePackages = [
    "com.iskportal.kauth.session.repository",
    "com.iskportal.kauth.security.repository",
    "com.iskportal.kauth.user.repository"
])
@Configuration
class KAuthConfiguration
