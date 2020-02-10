package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import android.util.Log
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.mapper.UserToUserEntityMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.IUserRepository
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val userLocalDataSource: IUserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userToUserEntityMapper: UserToUserEntityMapper
) : IUserRepository {

    override fun fetchTopUsers(): Single<List<UserEntity>> {
        return getUsersFromRemote()
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext { throwable ->
                Log.e(TAG, "Error retrieving users from remote data source", throwable)
                getUsersFromCache()
            }.flatMap { remoteResult ->
                saveToCache(remoteResult)
            }
            .map { users ->
                users.map { user ->
                    userToUserEntityMapper.map(user)
                }
            }
    }

    private fun getUsersFromRemote(): Single<RemoteResult> =
        userRemoteDataSource
            .getTopUsers()
            .map { users ->
                RemoteResult.Success(users)
            }

    private fun getUsersFromCache(): Single<RemoteResult> =
        userLocalDataSource
            .getUsers()
            .toSingle(emptyList())
            .map { users ->
                RemoteResult.Error(users)
            }

    private fun saveToCache(remoteResult: RemoteResult) =
        when (remoteResult) {
            is RemoteResult.Success -> {
                userLocalDataSource
                    .saveUsers(remoteResult.users)
                    .toSingleDefault(remoteResult.users)
                    .onErrorResumeNext { throwable ->
                        // don't throw an error on saving to the cache, we already have the users
                        // so just return them
                        Log.e(TAG, "Error savings users to local data source", throwable)
                        Single.just(remoteResult.users)
                    }
            }
            is RemoteResult.Error -> Single.just(remoteResult.users)
        }

    override fun getUser(accountId: Int): Maybe<UserEntity> {
        return userLocalDataSource
            .getUser(accountId)
            .subscribeOn(Schedulers.io())
            .map { user ->
                userToUserEntityMapper.map(user)
            }
    }

    sealed class RemoteResult {
        data class Success(val users: List<User>) : RemoteResult()
        data class Error(val users: List<User>) : RemoteResult()
    }
}