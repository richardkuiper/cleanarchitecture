package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
        private val userApi: UserApi
) {

    fun getTopUsers() =
            userApi
                    .getTopUsers()
                    .subscribeOn(Schedulers.io())
                    .map { it.users }
}