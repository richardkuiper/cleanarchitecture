package com.richardkuiper.cleanarchitecture.stackoverflowusers.data.mapper

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import javax.inject.Inject

class UserToUserEntityMapper @Inject constructor() {
    fun map(from: User): UserEntity {
        return UserEntity(
                accountId = from.accountId,
                age = from.age,
                reputation = from.reputation,
                creationDate = from.creationDate,
                userType = from.userType,
                location = from.location,
                profileImage = from.profileImage,
                displayName = from.displayName
        )
    }
}