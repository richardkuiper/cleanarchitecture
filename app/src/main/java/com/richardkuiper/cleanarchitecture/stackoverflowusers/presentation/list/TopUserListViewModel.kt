package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.richardkuiper.cleanarchitecture.common.Status
import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.TopUsersOrderedByReputationInteractor
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.mapper.UserEntityToUserListViewDataMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.list.model.UserListViewData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "TopUserListViewModel"

class TopUserListViewModel @Inject constructor(
        private val topUsersOrderedByReputationInteractor: TopUsersOrderedByReputationInteractor,
        private val userEntityToUserListViewDataMapper: UserEntityToUserListViewDataMapper
) : ViewModel() {

    private val _users = MutableLiveData<Status<List<UserListViewData>>>()
    val users: LiveData<Status<List<UserListViewData>>> = _users

    private val subscriptions = CompositeDisposable()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        topUsersOrderedByReputationInteractor.getTopUsersOrderedByReputation()
                .map { userEntityList ->
                    userEntityList.map { userEntity ->
                        userEntityToUserListViewDataMapper.map(userEntity)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    _users.value = Status.Success(userList)
                }, { error ->
                    Log.e(TAG, "Error retrieving list of top users", error)
                    _users.value = Status.Error(error)
                }).also {
                    subscriptions.add(it)
                }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}