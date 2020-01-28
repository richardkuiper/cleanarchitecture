package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.mapper

import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData
import javax.inject.Inject

class UserEntityToUserListViewDataMapper @Inject constructor() {

    fun map(from: UserEntity): UserListViewData {
        return UserListViewData(
                accountId = from.accountId,
                profileImage = from.profileImage,
                displayName = from.displayName
        )
    }
}