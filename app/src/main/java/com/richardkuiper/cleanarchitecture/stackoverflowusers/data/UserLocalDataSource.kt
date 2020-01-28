package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

private const val TAG = "UserLocalDataSource"
private const val USER_CACHE_KEY = "USER_CACHE_KEY"

class UserLocalDataSource @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val gson: Gson
) : IUserLocalDataSource {

    override fun getUsers(): Maybe<List<User>> {
        return Maybe.fromCallable {
            Log.d(TAG, "Retrieving users from cache")
            sharedPreferences.getString(USER_CACHE_KEY, null)?.let { serializedUsers ->
                val usersListTokenType = object : TypeToken<List<User>>() {}.type
                gson.fromJson<List<User>>(serializedUsers, usersListTokenType)
            }
        }
    }

    override fun saveUsers(users: List<User>): Completable {
        return Completable.fromCallable {
            Log.d(TAG, "Saving users to cache. Size=${users.size}")
            sharedPreferences.edit()
                    .putString(USER_CACHE_KEY, gson.toJson(users))
                    .apply()
        }
    }

    override fun getUser(accountId: Int): Maybe<User> {
        return Maybe.fromCallable {
            sharedPreferences.getString(USER_CACHE_KEY, null)?.let { serializedUsers ->
                val usersListTokenType = object : TypeToken<List<User>>() {}.type
                gson.fromJson<List<User>>(serializedUsers, usersListTokenType)
            }?.firstOrNull { user ->
                user.accountId == accountId
            }
        }
    }
}