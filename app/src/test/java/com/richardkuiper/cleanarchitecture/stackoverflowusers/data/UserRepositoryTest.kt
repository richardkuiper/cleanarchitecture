package com.richardkuiper.cleanarchitecture.stackoverflowusers.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.richardkuiper.cleanarchitecture.RxImmediateSchedulerRule
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.mapper.UserToUserEntityMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.data.model.User
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @Rule
    @JvmField
    var rxRules = RxImmediateSchedulerRule()

    private val mockUserLocalDataSource: IUserLocalDataSource = mock()
    private val mockUserRemoteDataSource: UserRemoteDataSource = mock()
    private val mockUserToUserEntityMapper: UserToUserEntityMapper = mock()

    private val userRepository = UserRepository(
            mockUserLocalDataSource,
            mockUserRemoteDataSource,
            mockUserToUserEntityMapper
    )

    @Test
    fun testErrorIsReturned_whenErrorRetrievingFromRemoteSource_andErrorRetrievingFromCache() {
        whenever(mockUserRemoteDataSource.getTopUsers())
                .thenReturn(Single.error(Exception("Test error")))
        val cacheException = Exception("Another test error")
        whenever(mockUserLocalDataSource.getUsers())
                .thenReturn(Maybe.error(cacheException))

        val testObserver = userRepository.fetchTopUsers().test()
        testObserver.assertError(cacheException)
        testObserver.assertNotComplete()
        testObserver.dispose()

        verify(mockUserLocalDataSource, never()).saveUsers(any())
    }

    @Test
    fun testEmptyListIsReturned_whenErrorRetrievingFromRemoteSource_andCacheIsEmpty() {
        whenever(mockUserRemoteDataSource.getTopUsers())
                .thenReturn(Single.error(Exception("Test error")))
        whenever(mockUserLocalDataSource.getUsers())
                .thenReturn(Maybe.empty())

        val testObserver = userRepository.fetchTopUsers().test()
        testObserver.assertValue { users ->
            users.isEmpty()
        }
        testObserver.assertComplete()
        testObserver.dispose()

        verify(mockUserLocalDataSource, never()).saveUsers(any())
    }

    @Test
    fun testUserListIsReturned_whenErrorRetrievingFromRemoteSource_andCacheIsNotEmpty() {
        whenever(mockUserRemoteDataSource.getTopUsers())
                .thenReturn(Single.error(Exception("Test error")))

        val testUser = testUser()
        whenever(mockUserLocalDataSource.getUsers())
                .thenReturn(Maybe.just(listOf(testUser)))

        val expectedUserEntity = expectedUserEntity()
        whenever(mockUserToUserEntityMapper.map(testUser))
                .thenReturn(expectedUserEntity)

        val testObserver = userRepository.fetchTopUsers().test()
        testObserver.assertValue { users ->
            users.isNotEmpty() && users[0] == expectedUserEntity
        }
        testObserver.assertComplete()
        testObserver.dispose()

        verify(mockUserLocalDataSource, never()).saveUsers(any())
    }

    @Test
    fun testUserListIsReturned_whenUsersRetrievedFromRemoteSource_andErrorSavingToCache() {
        val testUser = testUser()
        whenever(mockUserRemoteDataSource.getTopUsers())
                .thenReturn(Single.just(listOf(testUser)))

        whenever(mockUserLocalDataSource.saveUsers(any()))
                .thenReturn(Completable.error(Exception("Error saving to cache")))

        val expectedUserEntity = expectedUserEntity()
        whenever(mockUserToUserEntityMapper.map(testUser))
                .thenReturn(expectedUserEntity)

        val testObserver = userRepository.fetchTopUsers().test()
        testObserver.assertValue { users ->
            users.isNotEmpty() && users[0] == expectedUserEntity
        }
        testObserver.assertComplete()
        testObserver.dispose()

        verify(mockUserLocalDataSource, never()).getUsers()
    }

    @Test
    fun testUserListIsReturned_whenUsersRetrievedFromRemoteSource_andUsersAreSavedToCache() {
        val testUser = testUser()
        val userList = listOf(testUser)
        whenever(mockUserRemoteDataSource.getTopUsers())
                .thenReturn(Single.just(userList))

        whenever(mockUserLocalDataSource.saveUsers(userList))
                .thenReturn(Completable.complete())

        val expectedUserEntity = expectedUserEntity()
        whenever(mockUserToUserEntityMapper.map(testUser))
                .thenReturn(expectedUserEntity)

        val testObserver = userRepository.fetchTopUsers().test()
        testObserver.assertValue { users ->
            users.isNotEmpty() && users[0] == expectedUserEntity
        }
        testObserver.assertComplete()
        testObserver.dispose()

        verify(mockUserLocalDataSource, never()).getUsers()
    }

    @Test
    fun testEmptyIsReturned_whenUserIsNotFoundInCache() {
        val testAccountId = 1
        whenever(mockUserLocalDataSource.getUser(testAccountId))
                .thenReturn(Maybe.empty())

        val testObserver = userRepository.getUser(testAccountId).test()
        testObserver.assertNoValues()
        testObserver.assertComplete()
        testObserver.dispose()
    }

    @Test
    fun testUserIsMappedAndReturned_whenUserIsFoundInCache() {
        val testAccountId = 1
        val testUser = testUser()
        whenever(mockUserLocalDataSource.getUser(testAccountId))
                .thenReturn(Maybe.just(testUser))

        val expectedUserEntity = expectedUserEntity()
        whenever(mockUserToUserEntityMapper.map(testUser))
                .thenReturn(expectedUserEntity)

        val testObserver = userRepository.getUser(testAccountId).test()
        testObserver.assertValue { userEntity ->
            userEntity == expectedUserEntity
        }
        testObserver.assertComplete()
        testObserver.dispose()
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