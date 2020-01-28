package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.mapper

import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model.UserDetailViewData
import javax.inject.Inject

class UserEntityToUserDetailViewDataMapper @Inject constructor() {

    fun map(from: UserEntity): UserDetailViewData {
        return UserDetailViewData(
                name = from.displayName,
                reputation = from.reputation,
                creationDate = from.creationDate,
                userType = from.userType,
                location = from.location,
                profileImage = from.profileImage
        )
    }
}