package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail

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
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.TopUserDetailInteractor
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.exception.UserNotFoundException
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.mapper.UserEntityToUserDetailViewDataMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model.UserDetailViewData
import io.reactivex.Maybe
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.*
import org.junit.runner.*
import org.mockito.junit.*

private const val TEST_ACCOUNT_ID = 1

@RunWith(MockitoJUnitRunner::class)
class TopUserDetailViewModelTest {

    @Rule
    @JvmField
    var rxRules = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val mockTopUserDetailInteractor: TopUserDetailInteractor = mock()
    private val mockUserEntitytToUserDetailViewDataMapper: UserEntityToUserDetailViewDataMapper =
            mock()
    private val mockObserver: Observer<Status<UserDetailViewData>> = mock()

    @Test
    fun testSuccessResultIsReturned_withUserDetailViewData_whenUserIsObservedSuccessfully() {
        val userEntity = userEntity()
        whenever(mockTopUserDetailInteractor.getTopUserDetail(TEST_ACCOUNT_ID))
                .thenReturn(Maybe.just(userEntity))

        val userDetailViewData = expectedUserDetailViewData()
        whenever(mockUserEntitytToUserDetailViewDataMapper.map(userEntity))
                .thenReturn(userDetailViewData)

        val viewModel = TopUserDetailViewModel(
                TEST_ACCOUNT_ID,
                mockTopUserDetailInteractor,
                mockUserEntitytToUserDetailViewDataMapper
        )
        viewModel.user.observeForever(mockObserver)

        verify(mockObserver).onChanged(Success(userDetailViewData))
        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun testErrorResultIsReturned_whenErrorRetrievingUser() {
        val exception = Exception("Some error")
        whenever(mockTopUserDetailInteractor.getTopUserDetail(TEST_ACCOUNT_ID))
                .thenReturn(Maybe.error(exception))

        val viewModel = TopUserDetailViewModel(
                TEST_ACCOUNT_ID,
                mockTopUserDetailInteractor,
                mockUserEntitytToUserDetailViewDataMapper
        )
        viewModel.user.observeForever(mockObserver)

        verify(mockObserver).onChanged(Error(exception))
        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun testErrorResultIsReturned_whenNoUserIsReturned() {
        whenever(mockTopUserDetailInteractor.getTopUserDetail(TEST_ACCOUNT_ID))
                .thenReturn(Maybe.empty())

        val viewModel = TopUserDetailViewModel(
                TEST_ACCOUNT_ID,
                mockTopUserDetailInteractor,
                mockUserEntitytToUserDetailViewDataMapper
        )
        viewModel.user.observeForever(mockObserver)

        argumentCaptor<Status<UserDetailViewData>>().apply {
            verify(mockObserver).onChanged(capture())
            assertTrue((firstValue as Error).cause is UserNotFoundException)
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

    private fun expectedUserDetailViewData() =
            UserDetailViewData(
                    name = "Uncle Bob",
                    reputation = 9,
                    creationDate = 10,
                    userType = "moderator",
                    location = "London",
                    profileImage = "some profile image url"
            )
}