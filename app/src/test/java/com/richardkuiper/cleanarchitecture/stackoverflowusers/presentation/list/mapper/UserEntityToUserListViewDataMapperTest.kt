package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.mapper

import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData
import org.junit.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class UserEntityToUserListViewDataMapperTest {

    @Test
    fun testUserEntityIsMappedToUserListViewData() {
        Assert.assertEquals(
                UserEntityToUserListViewDataMapper().map(userEntity()),
                expectedUserListViewData()
        )
    }

    private fun userEntity() =
            UserEntity(
                    accountId = 1,
                    age = 21,
                    reputation = 9,
                    creationDate = 10,
                    userType = "moderator",
                    location = "London",
                    profileImage = "some profile image url",
                    displayName = "Uncle Bob"
            )

    private fun expectedUserListViewData() =
            UserListViewData(
                    displayName = "Uncle Bob",
                    profileImage = "some profile image url",
                    accountId = 1
            )
}