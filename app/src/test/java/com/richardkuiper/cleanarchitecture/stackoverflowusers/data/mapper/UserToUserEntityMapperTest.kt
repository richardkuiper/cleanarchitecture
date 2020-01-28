package com.richardkuiper.cleanarchitecture.stackoverflowusers.data.mapper

import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class UserToUserEntityMapperTest {

    @Test
    fun testUserIsMappedToUserEntity() {
        assertEquals(
                UserToUserEntityMapper().map(testUser()), expectedUserEntity()
        )
    }

    private fun testUser() =
            User(
                    accountId = 1,
                    isEmployee = true,
                    lastModifiedDate = 2,
                    lastAccessDate = 3,
                    age = 21,
                    reputationChangeYear = 4,
                    reputationChangeQuarter = 5,
                    reputationChangeMonth = 6,
                    reputationChangeWeek = 7,
                    reputationChangeDay = 8,
                    reputation = 9,
                    creationDate = 10,
                    userType = "moderator",
                    userId = 11,
                    acceptRate = 12,
                    location = "London",
                    websiteUrl = "some website",
                    link = "some link",
                    profileImage = "some profile image url",
                    displayName = "Uncle Bob"
            )

    private fun expectedUserEntity() =
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
}