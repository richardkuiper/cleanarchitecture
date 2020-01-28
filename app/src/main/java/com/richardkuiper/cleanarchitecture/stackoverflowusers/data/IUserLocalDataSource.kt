package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import io.reactivex.Completable
import io.reactivex.Maybe

interface IUserLocalDataSource {

    fun getUsers(): Maybe<List<User>>
    fun saveUsers(users: List<User>): Completable
    fun getUser(accountId: Int): Maybe<User>
}