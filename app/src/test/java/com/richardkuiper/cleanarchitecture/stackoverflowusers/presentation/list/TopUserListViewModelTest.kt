package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.richardkuiper.cleanarchitecture.RxImmediateSchedulerRule
import com.richardkuiper.cleanarchitecture.common.Status
import com.richardkuiper.cleanarchitecture.common.Status.Error
import com.richardkuiper.cleanarchitecture.common.Status.Success
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.TopUsersOrderedByReputationInteractor
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.mapper.UserEntityToUserListViewDataMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData
import io.reactivex.Single
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class TopUserListViewModelTest {

    @Rule
    @JvmField
    var rxRules = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockTopUsersOrderedByReputationInteractor: TopUsersOrderedByReputationInteractor =
            mock()
    private val mockUserEntityToUserListViewDataMapper: UserEntityToUserListViewDataMapper =
            mock()
    private val mockObserver: Observer<Status<List<UserListViewData>>> = mock()

    @Test
    fun testSuccessResultIsReturned_withUserListViewData_whenUserIsObservedSuccessfully() {
        val userEntity = userEntity()
        whenever(mockTopUsersOrderedByReputationInteractor.getTopUsersOrderedByReputation())
                .thenReturn(Single.just(listOf(userEntity)))

        val userListViewData = expectedUserListViewData()
        whenever(mockUserEntityToUserListViewDataMapper.map(userEntity))
                .thenReturn(userListViewData)

        val viewModel = TopUserListViewModel(
                mockTopUsersOrderedByReputationInteractor,
                mockUserEntityToUserListViewDataMapper
        )
        viewModel.users.observeForever(mockObserver)

        argumentCaptor<Status<List<UserListViewData>>>().apply {
            verify(mockObserver).onChanged(capture())
            assertTrue(firstValue is Success)
            assertEquals(userListViewData, (firstValue as Success).data[0])
        }

        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun testErrorResultIsReturned_whenErrorObservingUsers() {
        val exception = Exception("some error")
        whenever(mockTopUsersOrderedByReputationInteractor.getTopUsersOrderedByReputation())
                .thenReturn(Single.error(exception))

        val viewModel = TopUserListViewModel(
                mockTopUsersOrderedByReputationInteractor,
                mockUserEntityToUserListViewDataMapper
        )
        viewModel.users.observeForever(mockObserver)

        argumentCaptor<Status<List<UserListViewData>>>().apply {
            verify(mockObserver).onChanged(capture())
            assertTrue(firstValue is Error)
            assertEquals(exception, (firstValue as Error).cause)
        }

        verifyNoMoreInteractions(mockObserver)
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