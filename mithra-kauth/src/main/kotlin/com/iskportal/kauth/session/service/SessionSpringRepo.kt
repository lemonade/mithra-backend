package com.iskportal.kauth.session.service

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.session.FindByIndexNameSessionRepository

//@Primary
//@Configuration
class SessionSpringRepo: FindByIndexNameSessionRepository<SpringSession?> {
    override fun createSession(): SpringSession? {
        TODO("Not yet implemented")
    }

    override fun save(session: SpringSession?) {
        TODO("Not yet implemented")
    }

    override fun findById(id: String?): SpringSession? {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: String?) {
        TODO("Not yet implemented")
    }

    override fun findByIndexNameAndIndexValue(
        indexName: String?,
        indexValue: String?
    ): MutableMap<String, SpringSession?> {
        TODO("Not yet implemented")
    }
}