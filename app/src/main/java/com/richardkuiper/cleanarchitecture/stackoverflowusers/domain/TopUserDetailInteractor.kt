package com.richardkuiper.cleanarchitecture.stackoverflowusers.domain

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserRepository
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Maybe
import javax.inject.Inject

class TopUserDetailInteractor @Inject constructor(
        private val userRepository: UserRepository
) {

    fun getTopUserDetail(accountId: Int): Maybe<UserEntity> =
            userRepository.getUser(accountId)
}