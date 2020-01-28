package com.richardkuiper.cleanarchitecture.stackoverflowusers.domain

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserRepository
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Single
import javax.inject.Inject

class TopUsersOrderedByReputationInteractor @Inject constructor(
        private val userRepository: UserRepository
) {

    fun getTopUsersOrderedByReputation(): Single<List<UserEntity>> =
            userRepository
                    .fetchTopUsers()
                    .map { list ->
                        list.sortedByDescending { userEntity ->
                            userEntity.reputation
                        }
                    }
}