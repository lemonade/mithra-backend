package com.iskportal.kauth.user.service

import com.iskportal.kauth.security.model.AuthenticationMethod
import com.iskportal.kauth.security.repository.AuthenticationMethodRepo
import com.iskportal.kauth.user.model.*
import com.iskportal.kauth.user.repository.*
import com.iskportal.mithra.exception.AlreadyExistException
import com.iskportal.mithra.exception.NotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepo: UserRepo,
    val amRepo: AuthenticationMethodRepo,
    val userPrivateInfoRepo: UserPrivateInfoRepo,
    val userPublicInfoRepo: UserPublicInfoRepo
) {

    fun getUserById(userId: Long): User =
        userRepo.findByIdOrNull(userId) ?: throw NotFoundException("User with id $userId does not exist")

    @Transactional(propagation = Propagation.REQUIRED)
    fun createNewUser(): User {
        val user = userRepo.save(User())

        val privateInfo = UserPrivateInfo(user)
        userPrivateInfoRepo.save(privateInfo)
        val publicInfo = UserPublicInfo(user, privateInfo)
        userPublicInfoRepo.save(publicInfo)

        return user
    }

    @Transactional
    fun registerUserWithAuthenticationMethod(am: AuthenticationMethod): User =
        try {
            val user = userRepo.save(am.user)
            amRepo.save(am)
            user.authenticationMethods.add(am)
            user
        } catch (e: DataIntegrityViolationException) {
            throw AlreadyExistException("already exist")
        }
}