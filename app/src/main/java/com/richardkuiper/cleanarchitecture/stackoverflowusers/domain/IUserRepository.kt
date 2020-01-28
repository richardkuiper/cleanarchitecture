package com.richardkuiper.cleanarchitecture.stackoverflowusers.domain

import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Maybe
import io.reactivex.Single

interface IUserRepository {
    fun fetchTopUsers(): Single<List<UserEntity>>
    fun getUser(accountId: Int): Maybe<UserEntity>
}