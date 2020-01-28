package com.richardkuiper.cleanarchitecture.stackoverflowusers.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.richardkuiper.cleanarchitecture.RxImmediateSchedulerRule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.UserRepository
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Single
import org.junit.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class TopUsersOrderedByReputationInteractorTest {

    @Rule
    @JvmField
    var rxRules = RxImmediateSchedulerRule()

    private val mockUserRepository: UserRepository = mock()

    private val interactor = TopUsersOrderedByReputationInteractor(
            mockUserRepository
    )

    @Test
    fun testUsersAreOrderedByReputation() {
        val userEntityOne = UserEntity(
                accountId = 1,
                age = 21,
                reputation = 9,
                creationDate = 10,
                userType = "moderator",
                location = "London",
                profileImage = "some profile image url",
                displayName = "Uncle Bob"
        )

        val userEntityTwo = UserEntity(
                accountId = 2,
                age = 56,
                reputation = 9000,
                creationDate = 12,
                userType = "user",
                location = "Estonia",
                profileImage = "some profile image url",
                displayName = "Not Uncle Bob"
        )

        whenever(mockUserRepository.fetchTopUsers())
                .thenReturn(Single.just(listOf(userEntityOne, userEntityTwo)))

        val testObserver = interactor.getTopUsersOrderedByReputation().test()
        testObserver.assertValue { userEntities ->
            userEntities[0] == userEntityTwo &&
                    userEntities[1] == userEntityOne
        }
        testObserver.assertComplete()
        testObserver.dispose()
    }
}