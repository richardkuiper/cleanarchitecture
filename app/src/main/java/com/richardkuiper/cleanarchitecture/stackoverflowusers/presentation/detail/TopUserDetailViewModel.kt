package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.richardkuiper.cleanarchitecture.common.Status
import com.richardkuiper.cleanarchitecture.common.Status.Error
import com.richardkuiper.cleanarchitecture.common.Status.Success
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.TopUserDetailInteractor
import com.richardkuiper.cleanarchitecture.stackoverflowusers.exception.UserNotFoundException
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.mapper.UserEntityToUserDetailViewDataMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model.UserDetailViewData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TopUserDetailViewModel constructor(
        private val accountId: Int,
        private val topUserDetailInteractor: TopUserDetailInteractor,
        private val userEntityToUserDetailViewDataMapper: UserEntityToUserDetailViewDataMapper
) : ViewModel() {

    private val _user = MutableLiveData<Status<UserDetailViewData>>()
    val user: LiveData<Status<UserDetailViewData>> = _user

    private val subscriptions = CompositeDisposable()

    init {
        getUser()
    }

    private fun getUser() {
        topUserDetailInteractor
                .getTopUserDetail(accountId)
                .observeOn(AndroidSchedulers.mainThread())
                .map { userEntity ->
                    userEntityToUserDetailViewDataMapper.map(userEntity)
                }.subscribe({ userDetailViewData ->
                    _user.value = Success(userDetailViewData)
                }, { throwable ->
                    _user.value = Error(throwable)
                }, {
                    _user.value = Error(UserNotFoundException("User was not found."))
                }).also { subscription ->
                    subscriptions.add(subscription)
                }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}